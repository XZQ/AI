#!/usr/bin/env bash
set -euo pipefail

OUT_DIR="/tmp/appstore-main"
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

javac --release 11 -d "$OUT_DIR" $(find core feature app/launcher -path '*/src/main/java/*.java' -o -path '*/src/main/java/*/*.java' 2>/dev/null)
java -cp "$OUT_DIR" com.car.appstore.app.launcher.AppLauncher
