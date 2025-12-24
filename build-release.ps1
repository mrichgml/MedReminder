param(
    [Parameter(Mandatory=$false)]
    [string]$BuildType = "apk"  # Options: apk, bundle, install
)

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  MedReminder Release Builder              " -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Check current version
Write-Host "Checking current version..." -ForegroundColor Yellow
$buildGradle = Get-Content "app\build.gradle" -Raw
if ($buildGradle -match 'versionCode\s+(\d+)') {
    $versionCode = $matches[1]
    Write-Host "Current versionCode: $versionCode" -ForegroundColor Green
}
if ($buildGradle -match 'versionName\s+"([^"]+)"') {
    $versionName = $matches[1]
    Write-Host "Current versionName: $versionName" -ForegroundColor Green
}
Write-Host ""

# Ask if user wants to update version
Write-Host "Do you want to update the version? (y/n): " -ForegroundColor Yellow -NoNewline
$updateVersion = Read-Host
if ($updateVersion -eq "y" -or $updateVersion -eq "Y") {
    Write-Host "Current versionCode: $versionCode" -ForegroundColor Cyan
    Write-Host "Enter new versionCode (or press Enter to auto-increment): " -NoNewline
    $newVersionCode = Read-Host
    if ([string]::IsNullOrWhiteSpace($newVersionCode)) {
        $newVersionCode = [int]$versionCode + 1
    }

    Write-Host "Current versionName: $versionName" -ForegroundColor Cyan
    Write-Host "Enter new versionName (e.g., 1.1.0): " -NoNewline
    $newVersionName = Read-Host

    if (-not [string]::IsNullOrWhiteSpace($newVersionName)) {
        Write-Host "Updating version numbers..." -ForegroundColor Yellow
        $buildGradle = $buildGradle -replace "versionCode\s+\d+", "versionCode $newVersionCode"
        $buildGradle = $buildGradle -replace 'versionName\s+"[^"]+"', "versionName `"$newVersionName`""
        Set-Content "app\build.gradle" $buildGradle
        Write-Host "✓ Updated to versionCode: $newVersionCode, versionName: $newVersionName" -ForegroundColor Green
        Write-Host ""
    }
}

# Clean build
Write-Host "Cleaning previous builds..." -ForegroundColor Yellow
& .\gradlew.bat clean | Out-Null
Write-Host "✓ Clean complete" -ForegroundColor Green
Write-Host ""

# Build based on type
switch ($BuildType.ToLower()) {
    "bundle" {
        Write-Host "Building App Bundle for Play Store..." -ForegroundColor Yellow
        Write-Host ""
        & .\gradlew.bat bundleRelease
        if ($LASTEXITCODE -eq 0) {
            Write-Host ""
            Write-Host "============================================" -ForegroundColor Green
            Write-Host "  ✓ App Bundle built successfully!        " -ForegroundColor Green
            Write-Host "============================================" -ForegroundColor Green
            Write-Host ""
            Write-Host "Location:" -ForegroundColor Cyan
            Write-Host "app\build\outputs\bundle\release\app-release.aab" -ForegroundColor White
            Write-Host ""
            Write-Host "Next steps:" -ForegroundColor Yellow
            Write-Host "1. Upload .aab to Google Play Console" -ForegroundColor White
            Write-Host "2. Complete store listing" -ForegroundColor White
            Write-Host "3. Submit for review" -ForegroundColor White
        }
    }
    "install" {
        Write-Host "Building and installing release version..." -ForegroundColor Yellow
        Write-Host ""
        & .\gradlew.bat installRelease
        if ($LASTEXITCODE -eq 0) {
            Write-Host ""
            Write-Host "============================================" -ForegroundColor Green
            Write-Host "  ✓ Release installed on device!          " -ForegroundColor Green
            Write-Host "============================================" -ForegroundColor Green
        }
    }
    default {
        Write-Host "Building Release APK..." -ForegroundColor Yellow
        Write-Host ""
        & .\gradlew.bat assembleRelease
        if ($LASTEXITCODE -eq 0) {
            Write-Host ""
            Write-Host "============================================" -ForegroundColor Green
            Write-Host "  ✓ Release APK built successfully!       " -ForegroundColor Green
            Write-Host "============================================" -ForegroundColor Green
            Write-Host ""
            Write-Host "Location:" -ForegroundColor Cyan
            Write-Host "app\build\outputs\apk\release\app-release.apk" -ForegroundColor White
            Write-Host ""
            Write-Host "Installation methods:" -ForegroundColor Yellow
            Write-Host "1. Copy APK to phone and install manually" -ForegroundColor White
            Write-Host "2. Use ADB: adb install app\build\outputs\apk\release\app-release.apk" -ForegroundColor White
            Write-Host "3. Run: .\gradlew.bat installRelease" -ForegroundColor White
            Write-Host ""

            # Check file size
            $apkPath = "app\build\outputs\apk\release\app-release.apk"
            if (Test-Path $apkPath) {
                $size = (Get-Item $apkPath).Length / 1MB
                Write-Host "APK Size: $([math]::Round($size, 2)) MB" -ForegroundColor Cyan
            }
        }
    }
}

Write-Host ""
Write-Host "Build date: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Gray
Write-Host ""

