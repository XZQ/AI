$ErrorActionPreference = "Stop"

function Invoke-Checked {
    param([scriptblock]$Command)
    & $Command
    if ($LASTEXITCODE -ne 0) {
        throw "Command failed with exit code $LASTEXITCODE"
    }
}

$outDir = Join-Path $env:TEMP "appstore-main"
if (Test-Path $outDir) {
    Remove-Item -Recurse -Force $outDir
}
New-Item -ItemType Directory -Path $outDir | Out-Null

$mainSources = Get-ChildItem -Recurse -File -Include *.java -Path core, feature, app |
    Where-Object { $_.FullName -match "\\src\\main\\java\\" } |
    ForEach-Object { $_.FullName }

Invoke-Checked { javac --release 11 -d $outDir $mainSources }
Invoke-Checked { java -cp $outDir com.car.appstore.app.launcher.AppLauncher }
