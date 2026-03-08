# 车载应用商店架构方案（V1）

## 1. 目标

- 提供稳定、可扩展、可维护的车载应用分发与升级能力。
- 支持首页、详情、搜索、设置、更新中心等核心业务。
- 在弱网或临时离线场景下保持可恢复与可观测。

## 2. 架构分层

采用 `Clean Architecture + Feature Module`：

- Presentation：页面状态与交互编排（本仓库暂未实现 UI）。
- Domain：业务规则、用例契约、仓储接口。
- Data：仓储实现、数据聚合（当前为 in-memory 版本）。
- Platform：系统能力封装（下载、安装、通知、调度，V1 预留）。

## 3. 模块划分

```text
app/launcher                 # 最小可运行入口
core/core-common             # 领域模型、错误模型、统一结果
core/core-update-engine      # 更新状态机
feature/*/domain             # 各业务域契约
feature/*/data               # 各业务域 in-memory 实现
tests/src                    # smoke 测试
```

## 4. 关键领域对象

- `AppInfo`：应用基础信息。
- `AppDetail`：应用详情信息。
- `UpdateInfo`：更新可用信息。
- `InstallTask`：更新/安装任务状态。
- `UserSetting`：用户设置项。

## 5. 关键流程

### 5.1 主链路（Step3）

`Home -> Detail -> Settings -> Update`

由 `AppFlowOrchestrator` 进行流程编排，并返回结构化 `FlowReport`。

### 5.2 更新状态机

`IDLE -> QUEUED -> DOWNLOADING -> VERIFYING -> INSTALLING -> SUCCEEDED`

并支持 `PAUSE/RESUME/CANCEL/RETRY` 分支。

## 6. 错误模型

- 统一使用 `DomainResult.Success / DomainResult.Failure`。
- 标准错误码集中在 `ErrorCodes`，例如：
  - `DETAIL_NOT_FOUND`
  - `UPDATE_NOT_FOUND`

## 7. 测试基线

- `UpdateStateMachineTest`：状态机转移规则。
- `DataLayerSmokeTest`：各 data 仓储基础行为。
- `LauncherE2ESmokeTest`：主链路成功与失败分支。

## 8. V1 后续建议

- 接入真实远端/本地数据源并增加缓存策略。
- 将 `batchUpdate` 失败项可观测化（成功、失败明细同时返回）。
- 补充 Gradle Wrapper 与 CI，统一构建/测试入口。
