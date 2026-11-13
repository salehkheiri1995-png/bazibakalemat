# Builds a signed release APK for Cafe Bazaar.
# Reads the signing credentials (and optional Tapsell / Bazaar RSA keys) from secrets.local,
# which is git-ignored and must be backed up offline.
$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$secrets = Join-Path $root "secrets.local"
if (-not (Test-Path $secrets)) {
    Write-Host "secrets.local not found. Create it first (see RELEASE.md)." -ForegroundColor Red
    exit 1
}

Get-Content $secrets | ForEach-Object {
    if ($_ -match '^\s*([A-Z0-9_]+)\s*=\s*(.*)\s*$') {
        $name = $matches[1]
        $value = $matches[2]
        # Only export non-empty values so empty placeholders fall back to the build defaults.
        if ($value) {
            Set-Item -Path ("Env:" + $name) -Value $value
        }
    }
}

if (-not $env:KEYSTORE_PATH) { Write-Host "KEYSTORE_PATH missing in secrets.local"; exit 1 }
if (-not $env:STORE_PASSWORD) { Write-Host "STORE_PASSWORD missing in secrets.local"; exit 1 }
if (-not $env:KEY_PASSWORD) { Write-Host "KEY_PASSWORD missing in secrets.local"; exit 1 }

Write-Host "Using keystore: $env:KEYSTORE_PATH" -ForegroundColor Cyan
& (Join-Path $root "gradlew.bat") assembleRelease --no-configuration-cache --console=plain
