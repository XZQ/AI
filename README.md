# Car AppStore Architecture Skeleton

Project context for future sessions:

- `docs/PROJECT_CONTEXT.md`

这是一个车载应用商店的分层 + 模块化骨架工程（Java 11），已包含可运行的 Android App 模块。

## 当前范围

- 已完成 `core-common` 领域模型与统一 `DomainResult`。
- 已完成 `home/detail/search/settings/update-center` 的 Domain 接口与 in-memory Data 实现。
- 已完成 `core-update-engine` 更新状态机及基础测试。
- 已完成最小可运行入口 `app:launcher`（主链路编排 + 状态机演示）。

## 工程结构

```text
app/
  car-store/                # Android 入口应用（可在 Android Studio 运行）
  launcher/
core/
  core-common/
  core-update-engine/
feature/
  feature-home/{domain,data}
  feature-detail/{domain,data}
  feature-search/{domain,data}
  feature-settings/{domain,data}
  feature-update-center/{domain,data}
tests/
  src/
scripts/
```

## 本地运行与测试

### Linux / macOS / Git Bash

```bash
./scripts/run_tests.sh
./scripts/run_launcher.sh
```

### Windows PowerShell

```powershell
./scripts/run_tests.ps1
./scripts/run_launcher.ps1
```

## 在 Android Studio 运行

1. 使用 Android Studio 打开项目根目录。
2. 等待 Gradle Sync 完成（项目已包含 `gradlew` 和 Android 模块）。
3. 在 Android Studio 中将 Gradle JDK 设为 17（例如 JetBrains Runtime 17）。
4. 选择运行配置 `app:car-store`。
5. 在模拟器或真机（Android 7.0+）上运行。

测试脚本会执行：

- `UpdateStateMachineTest`
- `DataLayerSmokeTest`
- `LauncherE2ESmokeTest`

## PR 提交流程（简版）

```bash
git checkout main
git pull origin main
git checkout -b feature/<your-topic>
# coding + test
git add .
git commit -m "feat: <summary>"
git push -u origin feature/<your-topic>
```
