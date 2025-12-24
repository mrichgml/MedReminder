@echo off
REM MedReminder - Test App Script
echo ========================================
echo   MedReminder App - Quick Test
echo ========================================
echo.

set SDK_PATH=C:\Users\mrich\AppData\Local\Android\Sdk
set ADB=%SDK_PATH%\platform-tools\adb.exe
set EMULATOR=%SDK_PATH%\emulator\emulator.exe

echo [1] Checking for connected devices...
"%ADB%" devices
echo.

echo [2] Waiting for device (make sure emulator is running or phone is connected)...
"%ADB%" wait-for-device
echo    Device detected!
echo.

echo [3] Installing MedReminder app...
call gradlew.bat installDebug
if %ERRORLEVEL% NEQ 0 (
    echo    ERROR: Installation failed!
    pause
    exit /b 1
)
echo    Installation successful!
echo.

echo [4] Launching MedReminder app...
"%ADB%" shell am start -n com.example.medreminder/.ui.MainActivity
if %ERRORLEVEL% NEQ 0 (
    echo    ERROR: Launch failed!
    pause
    exit /b 1
)
echo.

echo ========================================
echo   App launched successfully!
echo ========================================
echo.
echo The MedReminder app should now be running on your device.
echo.
echo To view logs, run: view-logs.bat
echo.
pause

