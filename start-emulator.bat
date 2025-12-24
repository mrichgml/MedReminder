@echo off
REM Start Android Emulator
set SDK_PATH=C:\Users\mrich\AppData\Local\Android\Sdk
set EMULATOR=%SDK_PATH%\emulator\emulator.exe

echo ========================================
echo   Starting Android Emulator
echo ========================================
echo.

echo Listing available emulators...
"%EMULATOR%" -list-avds
echo.

echo.
echo No emulators found? You need to create one first!
echo.
echo Option 1: Use Android Studio
echo   - Open Android Studio
echo   - Click "Device Manager" (phone icon)
echo   - Click "Create Device"
echo   - Follow the wizard
echo.
echo Option 2: Use command line (advanced)
echo   - Run: "%SDK_PATH%\cmdline-tools\latest\bin\avdmanager.exe" create avd -n test_device -k "system-images;android-33;google_apis;x86_64"
echo.
echo Once you have an emulator, you can start it with:
echo   "%EMULATOR%" -avd [emulator_name]
echo.
pause

