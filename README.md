# Car AppStore Architecture Skeleton

这是“车载应用商店”按分层 + 模块化落地的第一步实现：

- 建立 Java 多模块工程骨架；
- 建立 `core-common` 领域模型与统一 `DomainResult`；
- 建立 `home/detail/search/settings/update-center` 的 Domain 接口与 UseCase 契约。

## 当前范围

仅包含 Domain 契约层，不含 Data / Platform / UI 具体实现。

## 下一步建议

1. ✅ 已新增 `core-update-engine`，实现更新任务状态机与迁移规则；
2. ✅ 已为每个 feature 增加 `data` 层仓储实现（当前为 in-memory 版本）；
3. 接入一个最小可运行入口模块进行端到端联调。


## 本地测试

执行：

```bash
./scripts/run_tests.sh
```

该脚本会编译全部 main 源码，并执行：
- `UpdateStateMachineTest`
- `DataLayerSmokeTest`

## 用户指定标记

123456
