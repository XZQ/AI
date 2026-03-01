# Android 高可用移动网络连接优化案例（可落地版）

> 面向：Android（MVVM + Retrofit + OkHttp）
> 
> 目标：在弱网、网络切换（Wi‑Fi/4G/5G）和后台恢复场景下，把“请求成功率、关键接口 P95、重连成功率”做成可量化提升。

---

## 1. 业务场景与目标

某内容类 App 在地铁、电梯、商场等弱网环境表现不稳定：

- 冷启动首屏配置接口超时较多，P95 > 2s。
- 前后台切换后首个请求失败率偏高。
- 错误类型分散（`UnknownHostException`、`SocketTimeoutException`、`SSLException`），排障效率低。

### 目标 KPI（示例）

- 请求成功率：`96.2% -> 99.2%+`
- 首屏关键接口 P95：`2200ms -> < 900ms`
- 前后台切换后首个关键请求成功率：`< 97% -> 99.5%`

---

## 2. 先做观测：分层埋点，不只看总耗时

高可用优化第一步不是“调参数”，而是“把链路拆开看”。

### 2.1 单请求分段指标

每个请求至少拆成以下阶段：

1. DNS
2. TCP Connect
3. TLS Handshake
4. Request Upload
5. TTFB
6. Response Download
7. JSON/Proto 反序列化

### 2.2 关键标签（维度）

- 网络类型：`WIFI / LTE / NR(5G)`
- 网络是否验证：`NET_CAPABILITY_VALIDATED`
- 前后台：`foreground/background`
- 信号强度等级（可选）
- 是否漫游/计费网络（`isRoaming/isMetered`）

### 2.3 实现建议（OkHttp EventListener）

用 `EventListener` 采集 DNS/TCP/TLS/TTFB 时间，统一上报到埋点系统；否则只能拿到“总耗时”，无法判断瓶颈在 DNS 还是 TLS。

---

## 3. 典型根因（真实项目最常见）

1. **OkHttpClient 被频繁 new**：连接池复用失效，TLS 会话无法复用。
2. **仅依赖失败回调感知断网**：网络恢复慢半拍，错过“快速重试窗口”。
3. **重试策略一刀切**：POST 盲重试引入重复写风险。
4. **DNS 无兜底**：运营商 DNS 波动时 `UnknownHostException` 激增。
5. **没有优先级治理**：埋点和首屏请求抢链路。

---

## 4. 三层优化方案（核心）

## 4.1 连接与传输层

### A. 单例 OkHttpClient + 连接池治理

```kotlin
object NetworkStack {
    private val connectionPool = ConnectionPool(
        maxIdleConnections = 10,
        keepAliveDuration = 5,
        timeUnit = TimeUnit.MINUTES
    )

    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectionPool(connectionPool)
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .callTimeout(8, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false) // 交由业务重试策略接管
            .build()
    }
}
```

要点：

- 全局一个 `OkHttpClient`。
- `callTimeout` 必须设置，防止长尾卡死。
- 移动网络下 `connectTimeout` 不要过长，宁可快速失败进入可控重试。

### B. HTTP/2 + TLS Session 复用

- 服务端开启 ALPN（HTTP/2）。
- 客户端使用现代 TLS 配置。
- 启动后可在空闲窗口预热核心域名（低频率，避免打爆服务端）。

### C. DNS 兜底

- 先查内存短 TTL 缓存。
- 失败回退系统 DNS。
- 核心域名可接入 DoH 作为备选（注意合规和链路成本）。

---

## 4.2 网络感知与切换层

### A. 用 `registerDefaultNetworkCallback` 实时感知网络变化

```kotlin
class NetworkMonitor(private val cm: ConnectivityManager) {
    @Volatile var isValidated: Boolean = false
        private set

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            // 只表示有网络，不代表外网可达
        }

        override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) {
            isValidated = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            // 当 isValidated 从 false->true，可触发关键请求恢复
        }

        override fun onLost(network: Network) {
            isValidated = false
        }
    }

    fun start() = cm.registerDefaultNetworkCallback(callback)
    fun stop() = cm.unregisterNetworkCallback(callback)
}
```

关键点：

- **`onAvailable` ≠ 可上网**，最终以 `VALIDATED` 为准。
- 切换网络（Wi‑Fi -> 5G）时，旧连接可能失效，需主动触发关键请求补偿。

### B. 两阶段重连

1. 快速重试：`100ms, 300ms`
2. 指数退避：`1s, 2s, 4s (+jitter)`

目的：兼顾“短抖动快速恢复”和“持续弱网防重试风暴”。

---

## 4.3 请求治理层

### A. 幂等安全重试（强约束）

- 自动重试默认只允许：`GET/HEAD/OPTIONS`
- 状态码：`429/503`（优先遵循 `Retry-After`）
- 异常：连接超时、连接重置、EOF 等临时错误
- `POST` 仅在携带业务幂等 Key 时允许重试

### B. 熔断 + 降级 + 限流

- 连续失败超阈值触发短时熔断（如 30s）。
- 熔断期间直接返回缓存或默认配置，保证“可用优先”。
- 降低低优先级请求并发，优先保障首屏/登录/支付。

### C. 请求合并（Single Flight）

同一资源 key 的并发请求只放行 1 个，其余等待结果，减少弱网下重复流量。

---

## 5. 可直接参考的重试拦截器（改进版）

```kotlin
class RetryInterceptor(
    private val maxRetries: Int = 3,
    private val baseDelayMs: Long = 200L,
    private val random: java.util.Random = java.util.Random()
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var attempt = 0
        var lastException: IOException? = null

        while (attempt <= maxRetries) {
            try {
                val response = chain.proceed(request)
                if (!shouldRetry(response, request.method, attempt)) return response
                response.close()
            } catch (e: IOException) {
                lastException = e
                if (!shouldRetry(e, request.method, attempt)) throw e
            }

            sleepWithBackoff(attempt)
            attempt++
        }

        throw lastException ?: IOException("retry failed")
    }

    private fun shouldRetry(response: Response, method: String, attempt: Int): Boolean {
        if (attempt >= maxRetries) return false
        if (!isIdempotent(method)) return false

        return when (response.code) {
            429, 503 -> true
            else -> false
        }
    }

    private fun shouldRetry(e: IOException, method: String, attempt: Int): Boolean {
        if (attempt >= maxRetries) return false
        if (!isIdempotent(method)) return false

        return e is java.net.SocketTimeoutException ||
            e is java.net.ConnectException ||
            e is java.io.EOFException ||
            e is java.net.SocketException
    }

    private fun isIdempotent(method: String): Boolean {
        return method == "GET" || method == "HEAD" || method == "OPTIONS"
    }

    private fun sleepWithBackoff(attempt: Int) {
        val exp = baseDelayMs * (1L shl attempt)
        val jitter = random.nextInt(150)
        Thread.sleep(exp + jitter)
    }
}
```

> 注意：拦截器线程阻塞式 `sleep` 仅适用于 OkHttp 同步链路；若你有更高并发诉求，可在调用层做异步重试调度，避免占用 Dispatcher 线程。

---

## 6. 灰度发布与验证闭环

### 6.1 放量策略

- `5% -> 20% -> 50% -> 100%` 分批上线。
- 每批观察 24h：
  - 成功率
  - 核心 API P95/P99
  - ANR/Crash
  - 服务端 QPS/错误码结构

### 6.2 回滚阈值（必须预设）

- 成功率下降 > 0.3%
- 核心 API P95 恶化 > 15%
- 崩溃率明显上升

触发任一即自动回滚上一稳定配置。

### 6.3 示例结果

- 请求成功率：`96.2% -> 99.35%`
- 首屏接口 P95：`2200ms -> 840ms`
- `UnknownHostException` 占比：`18% -> 3.2%`

---

## 7. 一周落地计划（建议）

- Day 1：接入分段埋点（EventListener + 统一错误码映射）
- Day 2：统一 `OkHttpClient`、连接池、超时参数
- Day 3：接入 `NetworkCallback` + 前后台恢复补偿
- Day 4：幂等重试 + 熔断降级 + single-flight
- Day 5：灰度 5%，观测并修正
- Day 6-7：扩大灰度并固化参数

---

## 8. 直接可执行 Checklist

- [ ] 只保留一个 `OkHttpClient`
- [ ] 配置 `connect/read/write/call` 四类超时
- [ ] 打通 DNS/TCP/TLS/TTFB 分段埋点
- [ ] 以 `NET_CAPABILITY_VALIDATED` 作为可达性判断
- [ ] 重试仅限幂等请求 + 可恢复错误
- [ ] 低优先级任务迁移到 `WorkManager`（`NetworkType.CONNECTED`）
- [ ] 设定熔断阈值与自动回滚开关

---

如果你需要，我可以下一步把这份方案拆成**可复制到工程的代码骨架**（`NetworkModule`、`NetworkStateRepository`、`RetryPolicy`、`CircuitBreaker`、`MetricsReporter`），并给出包结构和初始化顺序。
