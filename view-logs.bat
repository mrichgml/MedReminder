@echo off
REM View MedReminder app logs
set ADB=C:\Users\mrich\AppData\Local\Android\Sdk\platform-tools\adb.exe

echo ========================================
echo   MedReminder - Live Logs
echo ========================================
echo Press Ctrl+C to stop viewing logs
echo.

"%ADB%" logcat -s "AndroidRuntime:E" "MedReminder:V" "*:E"

