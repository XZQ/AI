# Project Context

This file captures the current working state of the repository so future Codex sessions can recover context quickly.

## What this project is

- Project name: `car-appstore`
- Goal: an Android car app store demo with layered modules
- Root path: `D:\workspace\codex\AI`
- Current Android app module: `:app:car-store`
- Current pure Java demo module: `:app:launcher`

## Current status

- The repository can be opened from the root directory in Android Studio.
- The Android module `:app:car-store` can build successfully.
- The Java smoke tests still pass through the scripts in `scripts/`.
- The repo includes a Gradle Wrapper and should be synced through `gradlew` / `gradlew.bat`.

## Important modules

- `app/car-store`
  - Real Android application module
  - Contains `MainActivity`, `AndroidManifest.xml`, resources, and theme
- `app/launcher`
  - Pure Java launcher used for flow demo and smoke validation
- `core/core-common`
  - Shared domain models and result/error types
- `core/core-update-engine`
  - Update state machine
- `feature/*/(domain|data)`
  - Feature contracts and in-memory implementations

## How to run

### Android Studio

1. Open the root folder: `D:\workspace\codex\AI`
2. Wait for Gradle sync
3. Set Gradle JDK to `17`
4. Run configuration: `app.car-store`

### Command line

Build Android debug APK:

```powershell
.\gradlew.bat :app:car-store:assembleDebug
```

Run Java smoke tests:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\run_tests.ps1
```

Run Java launcher demo:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\run_launcher.ps1
```

## Expected APK output

APK path:

`app\car-store\build\outputs\apk\debug\app-car-store-debug.apk`

## Environment assumptions

- Gradle JDK: `17`
- Java language level in shared Java modules: `11`
- Android `compileSdkVersion`: `33`
- Android `targetSdkVersion`: `33`
- Android `minSdkVersion`: `24`

## Important project decisions already made

- The Android entry point is `:app:car-store`, not `:app:launcher`
- Java 17-specific language features were removed from shared modules so the domain/data code stays Java 11 compatible
- Data modules expose their domain dependencies with `api` so Android code can see domain types
- Subprojects are assigned unique Maven coordinates through the root Gradle config to avoid dependency resolution collisions between many `data` / `domain` modules
- Gradle Wrapper was upgraded to `8.5`

## Known gotchas

- Running root-level `assemble` is easy to confuse with the Android APK task. The reliable task is:
  - `:app:car-store:assembleDebug`
- If Android Studio uses a JDK without `jlink`, Android builds may fail during toolchain setup
- The Java smoke scripts intentionally exclude Android sources and only compile pure Java modules

## Current branch and PR context

- Working branch used for Android conversion: `codex/android-car-app-store`
- This branch already has a PR associated with it

## Good first files to read next session

- `README.md`
- `docs/PROJECT_CONTEXT.md`
- `settings.gradle`
- `build.gradle`
- `app/car-store/build.gradle`
- `app/car-store/src/main/java/com/car/appstore/android/MainActivity.java`
