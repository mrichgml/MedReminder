# Testing MedReminder Without Android Studio UI

Since you don't have access to the green run button in Android Studio, here are **3 simple ways** to test your app.

## 🎯 Option 1: Use Your Android Phone (EASIEST!)

This is the simplest method - just use your own Android phone:

### Step 1: Enable Developer Mode on Your Phone
1. Go to **Settings** → **About Phone**
2. Tap **"Build Number"** 7 times (you'll see "You are now a developer!")
3. Go back to **Settings** → **Developer Options**
4. Enable **"USB Debugging"**

### Step 2: Connect Your Phone
1. Connect your phone to your computer with a USB cable
2. On your phone, tap **"Allow"** when asked to authorize USB debugging

### Step 3: Install and Run
Simply double-click this file:
```
D:\Git\Teckon\test-app.bat
```

**That's it!** The script will:
- Detect your phone
- Install the app
- Launch it automatically

The MedReminder app will open on your phone!

---

## 🎯 Option 2: Install APK Directly on Phone

The app is already built! Just copy it to your phone:

### Method A: Via USB
1. Connect phone to computer
2. Copy this file to your phone:
   ```
   D:\Git\Teckon\app\build\outputs\apk\debug\app-debug.apk
   ```
3. On your phone, open the APK file
4. Tap "Install" (allow installation from unknown sources if prompted)

### Method B: Via Email/Cloud
1. Email yourself the APK file
2. Open email on your phone
3. Download and install the APK

---

## 🎯 Option 3: Command Line (Manual Control)

If you prefer to run commands yourself:

### Check if device is connected:
```powershell
C:\Users\mrich\AppData\Local\Android\Sdk\platform-tools\adb.exe devices
```
You should see your device listed (phone or emulator)

### Install the app:
```powershell
cd D:\Git\Teckon
.\gradlew.bat installDebug
```

### Launch the app:
```powershell
C:\Users\mrich\AppData\Local\Android\Sdk\platform-tools\adb.exe shell am start -n com.example.medreminder/.ui.MainActivity
```

### View logs (optional):
```powershell
C:\Users\mrich\AppData\Local\Android\Sdk\platform-tools\adb.exe logcat | Select-String "medreminder"
```

---

## 📱 If You Want to Use an Emulator

You don't have any emulators configured yet. Here's how to create one:

### Option A: Use Android Studio (Recommended)
1. Open Android Studio
2. Click **"Device Manager"** icon (looks like a phone, in the right sidebar or top toolbar)
3. Click **"+"** or **"Create Device"**
4. Choose **"Pixel 4"** or any phone
5. Click **Next**
6. Download a system image (choose **API 33 - Android 13.0**)
7. Click **Finish**
8. Click the **▶ Play button** next to your new emulator to start it
9. Run `test-app.bat` to install and launch your app

### Option B: Command Line (Advanced)
Run this to download and create an emulator:
```powershell
# Download system image
C:\Users\mrich\AppData\Local\Android\Sdk\cmdline-tools\latest\bin\sdkmanager.bat "system-images;android-33;google_apis;x86_64"

# Create emulator
C:\Users\mrich\AppData\Local\Android\Sdk\cmdline-tools\latest\bin\avdmanager.bat create avd -n test_phone -k "system-images;android-33;google_apis;x86_64" -d pixel

# Start emulator
C:\Users\mrich\AppData\Local\Android\Sdk\emulator\emulator.exe -avd test_phone
```

---

## 🚀 Quick Start Scripts Created

I've created these helpful scripts for you:

1. **`test-app.bat`** - Automatically installs and runs your app (USE THIS!)
2. **`view-logs.bat`** - Shows live app logs for debugging
3. **`start-emulator.bat`** - Info about starting emulators

---

## ✅ Recommended: Use Your Phone!

**The absolute easiest way is Option 1 - using your Android phone:**

1. Enable USB debugging (see Step 1 above)
2. Connect phone to computer
3. Double-click `test-app.bat`
4. Done! 🎉

No need for Android Studio UI, emulators, or complex setup!

---

## 🐛 Troubleshooting

### "No devices found" error
- Make sure your phone is connected via USB
- Check that USB debugging is enabled
- Try unplugging and reconnecting
- Run: `adb devices` to verify connection

### Phone not authorized
- Look at your phone screen for authorization prompt
- Tap "Always allow from this computer" and "OK"

### Installation failed
- Uninstall old version first:
  ```
  C:\Users\mrich\AppData\Local\Android\Sdk\platform-tools\adb.exe uninstall com.example.medreminder
  ```
- Then try again

### App crashes
- View logs with `view-logs.bat`
- Or run: `adb logcat | Select-String "AndroidRuntime"`

---

## 📦 APK Info

Your pre-built APK is here:
```
D:\Git\Teckon\app\build\outputs\apk\debug\app-debug.apk
```
- Size: 5.8 MB
- Min Android version: 8.0 (API 26)
- Ready to install!

You can share this file with anyone to test on their Android device.

---

## 💡 Pro Tip

If you have an Android phone nearby, that's literally the fastest way to test. Just:
1. Enable USB debugging
2. Run `test-app.bat`
3. Test on real hardware!

Much faster than setting up emulators!

