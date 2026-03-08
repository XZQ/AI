#!/usr/bin/env bash
set -euo pipefail

OUT_DIR="/tmp/appstore-main"
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

# Compile production sources
javac --release 11 -d "$OUT_DIR" $(find core feature app/launcher -name "*.java" -path "*/src/main/java/*" 2>/dev/null)

# Compile test sources (no external dependencies)
javac --release 11 -cp "$OUT_DIR" -d "$OUT_DIR" \
  core/core-update-engine/src/test/java/com/car/appstore/core/updateengine/UpdateStateMachineTest.java \
  tests/src/DataLayerSmokeTest.java \
  tests/src/LauncherE2ESmokeTest.java

# Execute tests
java -cp "$OUT_DIR" com.car.appstore.core.updateengine.UpdateStateMachineTest
java -cp "$OUT_DIR" DataLayerSmokeTest
java -cp "$OUT_DIR" LauncherE2ESmokeTest
