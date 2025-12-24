# Android Emulator Hypervisor Error - Complete Fix Guide

## Problem
```
Android emulator hypervisor is not installed
```

This error means your Android emulator cannot access hardware virtualization, which is required for fast emulation.

## Solutions (Choose One)

### Solution 1: Enable Hyper-V (Windows Pro/Enterprise/Education Only)

**Requirements**: Windows 10/11 Pro, Enterprise, or Education edition

**Steps**:

1. **Open Windows Features**
   - Press `Win + R`
   - Type `optionalfeatures`
   - Press Enter

2. **Enable Hyper-V**
   - Check the box next to **"Hyper-V"**
   - Click OK
   - Restart your computer when prompted

3. **Verify Installation**
   - Open PowerShell as Administrator
   - Run: `Get-WindowsOptionalFeature -Online -FeatureName Microsoft-Hyper-V`
   - Look for: `State : Enabled`

4. **Update Android Emulator Settings**
   - Open Android Studio
   - File → Settings → Tools → Emulator
   - Check: ✓ "Use native Linux Hyper-V hypervisor"
   - Click Apply/OK

---

### Solution 2: Use WHPX (Windows Hypervisor Platform) - Alternative

**Requirements**: Windows 10 version 1803+ or Windows 11

**Advantages**: 
- Works on Windows Home edition
- Lightweight alternative to Hyper-V
- Better Windows 11 support

**Steps**:

1. **Enable Windows Hypervisor Platform**
   - Press `Win + R`
   - Type `optionalfeatures`
   - Press Enter
   - Check: **"Windows Hypervisor Platform"**
   - Click OK
   - Restart when prompted

2. **Configure Android Emulator**
   - Open Android Studio
   - File → Settings → Tools → Emulator
   - Check: ✓ "Use Windows Hypervisor Platform"
   - Click Apply/OK

3. **Delete and Recreate Emulator**
   - Device Manager → Select `Medium_Phone_API_36.1`
   - Click delete (trash icon)
   - Click "Create Device"
   - Select a device template
   - Choose API 33 or higher
   - Select **x86_64 ABI** (important!)
   - Finish

---

### Solution 3: Use Software Emulation (Slowest - Last Resort)

If you cannot enable virtualization due to system limitations:

1. **Create a New Emulator with ARM ABI**
   - Open Android Studio
   - Device Manager → Create Device
   - Select device template
   - Choose API 33 or higher
   - Select **armeabi-v7a** or **arm64-v8a** ABI
   - Click Finish

2. **Note**: This will be significantly slower because it emulates ARM architecture on x86 CPU via software

---

## Check Virtualization Support

### Method 1: Check BIOS Settings
Your CPU must support virtualization:

**Intel CPUs**: Look for "Intel VT-x" or "VT-x"
**AMD CPUs**: Look for "AMD-V" or "SVM"

**Steps**:
1. Restart your computer
2. Enter BIOS (usually press `Del`, `F2`, `F10`, or `Esc` during startup)
3. Look for virtualization setting (location varies by motherboard)
4. Enable it and save
5. Restart Windows

### Method 2: Check Windows Support
Run as Administrator in Command Prompt:
```cmd
systeminfo
```

Look for:
- "Hyper-V Capable: Yes" - Can use Hyper-V
- "Virtualization Enabled In Firmware: Yes" - CPU virtualization is on

---

## Troubleshooting

### Still Getting Error After Enabling?

1. **Restart your computer** (full restart, not sleep mode)

2. **Disable Conflicting Software**
   - Some antivirus software conflicts with hypervisors
   - Temporarily disable: Norton, McAfee, Kaspersky, etc.
   - Try VirtualBox if installed (uninstall or disable)

3. **Update Android Emulator**
   - Open Android Studio
   - Tools → SDK Manager
   - SDK Tools tab
   - Update "Android Emulator" to latest version
   - Click Apply

4. **Delete and Recreate Emulator**
   ```powershell
   # List all emulators
   & "C:\Users\mrich\AppData\Local\Android\Sdk\emulator\emulator.exe" -list-avds
   
   # Delete old emulator
   & "C:\Users\mrich\AppData\Local\Android\Sdk\cmdline-tools\latest\bin\avdmanager.exe" delete avd -n Medium_Phone_API_36.1
   ```

5. **Create Fresh Emulator**
   - Open Android Studio
   - Device Manager
   - Click "Create Device"
   - API 33+ (preferably API 34 or 35)
   - ABI: x86_64
   - Finish

---

## Recommended Quick Start

**For most users, follow this order**:

1. ✅ Restart your computer (full shutdown, not sleep)
2. ✅ Check BIOS for virtualization enabled
3. ✅ Enable Windows Hypervisor Platform (if Windows Home)
   - OR Enable Hyper-V (if Windows Pro/Enterprise)
4. ✅ Restart again
5. ✅ Delete old emulator in Device Manager
6. ✅ Create new emulator with x86_64 ABI
7. ✅ Start emulator and wait for boot

---

## Testing Your Setup

Once virtualization is enabled:

```powershell
# Start emulator
& "C:\Users\mrich\AppData\Local\Android\Sdk\emulator\emulator.exe" -avd Medium_Phone_API_36.1

# In another terminal, once emulator boots:
adb devices

# You should see your emulator listed:
# Medium_Phone_API_36.1   device
```

Then install and run your app:
```powershell
cd D:\Git\Teckon
& .\gradlew installDebug
& .\gradlew run
```

---

## Quick Reference

| Windows Edition | Recommended | Alternative |
|---|---|---|
| **Home** | Windows Hypervisor Platform (WHPX) | Software emulation (slow) |
| **Pro/Enterprise/Education** | Hyper-V | WHPX |

---

## Resources
- [Android Emulator Acceleration](https://developer.android.com/studio/run/emulator-acceleration)
- [Windows Hypervisor Platform Setup](https://docs.microsoft.com/en-us/virtualization/hyper-v-on-windows/quick-start/enable-hyper-v)
- [Enable Virtualization in BIOS](https://www.intel.com/content/www/us/en/support/articles/000007139/processors.html)

