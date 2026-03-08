#!/usr/bin/env bash
set -euo pipefail

OUT_DIR="/tmp/appstore-main"
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

# Compile production sources
javac --release 17 -d "$OUT_DIR" $(find core feature -path '*/src/main/java/*.java' -o -path '*/src/main/java/*/*.java' 2>/dev/null)

# Compile test sources (no external dependencies)
javac --release 17 -cp "$OUT_DIR" -d "$OUT_DIR" \
  core/core-update-engine/src/test/java/com/car/appstore/core/updateengine/UpdateStateMachineTest.java \
  tests/src/DataLayerSmokeTest.java

# Execute tests
java -cp "$OUT_DIR" com.car.appstore.core.updateengine.UpdateStateMachineTest
java -cp "$OUT_DIR" DataLayerSmokeTest
