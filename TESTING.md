# MedReminder - Testing Guide

## Prerequisites
- Android Studio installed
- Android SDK installed (already detected at: `C:\Users\mrich\AppData\Local\Android\Sdk`)
- Either an Android emulator or physical Android device

## Testing Methods

### Method 1: Using Android Studio (Recommended)

1. **Open the project in Android Studio:**
   ```
   File → Open → Navigate to D:\Git\Teckon
   ```

2. **Wait for Gradle sync to complete**

3. **Start an emulator or connect a device:**
   - For emulator: Click the device dropdown → "Device Manager" → Create or start an emulator
   - For physical device: Enable USB debugging and connect via USB

4. **Run the app:**
   - Click the green "Run" button (▶) in the toolbar
   - Or press `Shift + F10`
   - Or menu: `Run → Run 'app'`

### Method 2: Using Command Line

#### Option A: Install on Running Emulator/Device

1. **Start an Android emulator** (if not already running):
   ```powershell
   # List available emulators
   $env:ANDROID_HOME\emulator\emulator.exe -list-avds
   
   # Start an emulator (replace <avd_name> with actual name)
   $env:ANDROID_HOME\emulator\emulator.exe -avd <avd_name>
   ```

2. **Install the app:**
   ```powershell
   cd D:\Git\Teckon
   .\gradlew.bat installDebug
   ```

3. **Launch the app:**
   ```powershell
   $env:ANDROID_HOME\platform-tools\adb.exe shell am start -n com.example.medreminder/.ui.MainActivity
   ```

#### Option B: Direct APK Installation

1. **Install using adb:**
   ```powershell
   $env:ANDROID_HOME\platform-tools\adb.exe install app\build\outputs\apk\debug\app-debug.apk
   ```

2. **Or copy APK to device and install manually:**
   - Copy `app\build\outputs\apk\debug\app-debug.apk` to your Android device
   - Open the APK file on your device
   - Allow installation from unknown sources if prompted
   - Install the app

### Method 3: Using Gradle Tasks

```powershell
# Build and install
.\gradlew.bat installDebug

# Build, install, and run
.\gradlew.bat installDebug
# Then manually open the app on device
```

## Testing the App Features

Once the app is running, test these features:

### 1. **Add a Medication**
   - Tap the "+ Add Medication" button
   - Fill in:
     - Medication name (e.g., "Ibuprofen")
     - Dosage amount (e.g., "200mg")
     - Minimum hours between doses (e.g., "4")
     - Maximum doses per 24 hours (e.g., "6")
   - Tap "Save"

### 2. **View Medication Status**
   - Medications appear as buttons in a grid
   - **Green button** = dose available (can take)
   - **Red button** = dose not available (must wait)
   - Shows last taken time or "Never taken"

### 3. **Take a Dose**
   - Tap a green medication button
   - Button should turn red (indicating dose was recorded)
   - Time should update to show when it was taken

### 4. **Edit a Medication**
   - Tap the "Edit" button (pencil icon) on any medication
   - Modify the details
   - Tap "Save"

### 5. **Delete a Medication**
   - Tap the "Delete" button (trash icon) on any medication
   - Confirm deletion in the dialog

### 6. **Test Dose Timing Logic**
   - Add a medication with 4 hours minimum between doses
   - Take a dose (button turns red)
   - Wait and check that button turns green after 4 hours
   - Try taking max doses per day and verify button stays red

### 7. **Test Data Persistence**
   - Add medications and take some doses
   - Force close the app
   - Reopen the app
   - Verify all data is still there

## Troubleshooting

### No devices available
- **For emulator:** Open Android Studio → Device Manager → Create/Start an AVD
- **For physical device:** 
  1. Enable Developer Options (Settings → About Phone → Tap "Build Number" 7 times)
  2. Enable USB Debugging (Settings → Developer Options → USB Debugging)
  3. Connect device and authorize computer

### App crashes on startup
- Check logcat for errors:
  ```powershell
  $env:ANDROID_HOME\platform-tools\adb.exe logcat | Select-String "medreminder"
  ```

### Build issues
- Clean and rebuild:
  ```powershell
  .\gradlew.bat clean build
  ```

## Useful Commands

```powershell
# Check connected devices
$env:ANDROID_HOME\platform-tools\adb.exe devices

# View app logs
$env:ANDROID_HOME\platform-tools\adb.exe logcat -s "MedReminder"

# Uninstall app
$env:ANDROID_HOME\platform-tools\adb.exe uninstall com.example.medreminder

# Clear app data
$env:ANDROID_HOME\platform-tools\adb.exe shell pm clear com.example.medreminder

# Take screenshot
$env:ANDROID_HOME\platform-tools\adb.exe shell screencap -p /sdcard/screenshot.png
$env:ANDROID_HOME\platform-tools\adb.exe pull /sdcard/screenshot.png
```

## Testing Checklist

- [ ] App installs successfully
- [ ] App launches without crashes
- [ ] Can add medications (up to 8)
- [ ] Medication buttons display correctly
- [ ] Green/red status colors work properly
- [ ] Can record doses
- [ ] Dose timing restrictions work
- [ ] Can edit medications
- [ ] Can delete medications
- [ ] Data persists after app restart
- [ ] Time formatting displays correctly
- [ ] Maximum daily doses limit works
- [ ] Minimum time between doses works

## APK Locations

- **Debug APK:** `app\build\outputs\apk\debug\app-debug.apk` (5.8 MB)
- **Release APK:** `app\build\outputs\apk\release\app-release-unsigned.apk` (2.1 MB)

The release APK needs to be signed before distribution.

## Next Steps After Testing

1. **Report any bugs or issues**
2. **Add unit tests:** Create tests in `app\src\test\java\`
3. **Add instrumented tests:** Create tests in `app\src\androidTest\java\`
4. **Sign release APK** for distribution
5. **Add app icon** to replace placeholder
6. **Test on different screen sizes**
7. **Test with multiple users/devices**

---

**Note:** The app requires Android 8.0 (API 26) or higher.

