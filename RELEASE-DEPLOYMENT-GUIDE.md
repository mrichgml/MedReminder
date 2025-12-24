# MedReminder - Release Deployment Guide

## Quick Release Build (For Personal Use/Testing)

### Option 1: Simple Release Build (No Signing Required)
```powershell
# Build release APK
.\gradlew.bat assembleRelease

# The APK will be at:
# app\build\outputs\apk\release\app-release.apk
```

### Option 2: Build and Install Directly
```powershell
# Build and install release version on connected device
.\gradlew.bat installRelease
```

---

## Full Production Release (For Play Store/Distribution)

### Step 1: Create a Signing Key (First Time Only)

#### Using keytool (comes with Java):
```powershell
keytool -genkey -v -keystore medreminder-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias medreminder
```

**You'll be asked for:**
- Keystore password (remember this!)
- Key password (remember this!)
- Your name, organization, location

**IMPORTANT:** Save the `.jks` file and passwords securely! You'll need them for all future updates.

#### Example keystore creation:
```powershell
# Navigate to project root
cd D:\Git\Teckon

# Create keystore
keytool -genkey -v -keystore medreminder-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias medreminder

# Follow prompts:
# Enter keystore password: YourSecurePassword123
# Re-enter new password: YourSecurePassword123
# What is your first and last name? [Your Name]
# What is the name of your organizational unit? [Your Team/Personal]
# What is the name of your organization? [Your Company/Personal]
# What is the name of your City or Locality? [Your City]
# What is the name of your State or Province? [Your State]
# What is the two-letter country code for this unit? [US/UK/etc]
# Is CN=..., correct? yes
# Enter key password: (press ENTER to use same as keystore)
```

### Step 2: Configure Signing in build.gradle

Update `app/build.gradle`:

```groovy
signingConfigs {
    release {
        storeFile file("../medreminder-release-key.jks")
        storePassword "YourKeystorePassword"
        keyAlias "medreminder"
        keyPassword "YourKeyPassword"
    }
}
```

**SECURITY NOTE:** For production apps, use environment variables or gradle.properties instead of hardcoding passwords.

### Step 3: Update Version Number

In `app/build.gradle`, increment version:

```groovy
defaultConfig {
    versionCode 2        // Increment for each release
    versionName "1.1.0"  // Semantic versioning
}
```

### Step 4: Build Release APK

```powershell
# Clean previous builds
.\gradlew.bat clean

# Build release APK
.\gradlew.bat assembleRelease

# Or build App Bundle (for Play Store)
.\gradlew.bat bundleRelease
```

### Step 5: Find Your Release Build

**APK Location:**
```
D:\Git\Teckon\app\build\outputs\apk\release\app-release.apk
```

**App Bundle Location (for Play Store):**
```
D:\Git\Teckon\app\build\outputs\bundle\release\app-release.aab
```

---

## Version Numbering Guide

### versionCode (Integer)
- Increment by 1 for every release
- Example: 1, 2, 3, 4, 5...
- Play Store uses this to determine newest version

### versionName (String - Semantic Versioning)
- Format: MAJOR.MINOR.PATCH
- **MAJOR**: Breaking changes (1.0.0 → 2.0.0)
- **MINOR**: New features (1.0.0 → 1.1.0)
- **PATCH**: Bug fixes (1.0.0 → 1.0.1)

**Examples:**
- Initial release: `1.0.0` (versionCode: 1)
- Bug fix: `1.0.1` (versionCode: 2)
- New feature: `1.1.0` (versionCode: 3)
- Major update: `2.0.0` (versionCode: 4)

---

## Distribution Methods

### Method 1: Direct APK Installation (Easiest)
1. Build APK: `.\gradlew.bat assembleRelease`
2. Copy `app-release.apk` to your phone
3. Enable "Install from Unknown Sources" in phone settings
4. Tap APK file to install

### Method 2: ADB Installation
```powershell
# Install on connected device
adb install app\build\outputs\apk\release\app-release.apk

# Or use gradle
.\gradlew.bat installRelease
```

### Method 3: Google Play Store
1. Create Developer Account ($25 one-time fee)
2. Build App Bundle: `.\gradlew.bat bundleRelease`
3. Upload `.aab` file to Play Console
4. Complete store listing (screenshots, description, etc.)
5. Submit for review

### Method 4: Alternative App Stores
- Amazon Appstore
- Samsung Galaxy Store
- F-Droid (for open source)

### Method 5: Direct Download (Personal Website/GitHub)
- Upload APK to your hosting/GitHub releases
- Users download and install manually

---

## Testing Release Build Before Distribution

### Install on Test Device
```powershell
# Install release version
.\gradlew.bat installRelease

# Check it works properly
# - All features functional
# - No crashes
# - Notifications work
# - Database migrations work
```

### Verify Release Build
```powershell
# Check APK signature
jarsigner -verify -verbose -certs app\build\outputs\apk\release\app-release.apk

# Should show: "jar verified."
```

---

## Release Checklist

Before building release:
- [ ] Update versionCode and versionName
- [ ] Test all features in debug build
- [ ] Update CHANGELOG/README if applicable
- [ ] Test database migrations
- [ ] Test on multiple devices/Android versions
- [ ] Check ProGuard rules work (no crashes)
- [ ] Verify permissions are correct
- [ ] Test notifications
- [ ] Commit all changes to git
- [ ] Tag release in git: `git tag v1.0.0`

After building release:
- [ ] Install on test device
- [ ] Test all features
- [ ] Check APK size (should be smaller due to optimization)
- [ ] Keep `.jks` keystore file safe
- [ ] Document passwords securely
- [ ] Upload to distribution method
- [ ] Create release notes

---

## Current Build Configuration

**App Details:**
- **Package Name:** com.example.medreminder
- **Current Version:** 1.0.0 (versionCode: 1)
- **Min Android:** 8.0 (API 26)
- **Target Android:** 14 (API 34)

**Release Features:**
- ✅ Code minification (ProGuard)
- ✅ Resource shrinking
- ✅ Optimization enabled
- ✅ Signing configured

**APK Size Estimate:**
- Debug: ~5-8 MB
- Release: ~3-5 MB (optimized)

---

## Troubleshooting

### "Keystore not found"
- Ensure `.jks` file path is correct in build.gradle
- Use relative path: `file("../medreminder-release-key.jks")`

### "Cannot install - signatures do not match"
- Uninstall existing debug version first
- Or use different applicationId for release

### "ProGuard errors"
- Check proguard-rules.pro file
- Add keep rules for Room, WorkManager, etc.

### Build takes too long
- Normal for release builds (optimization takes time)
- First build is slowest
- Use `--parallel` flag for faster builds

---

## Quick Commands Reference

```powershell
# Build release APK
.\gradlew.bat assembleRelease

# Build release App Bundle
.\gradlew.bat bundleRelease

# Install release on device
.\gradlew.bat installRelease

# Clean + build release
.\gradlew.bat clean assembleRelease

# Check version
.\gradlew.bat -q app:dependencies --configuration releaseRuntimeClasspath

# Create keystore
keytool -genkey -v -keystore medreminder-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias medreminder
```

---

## Next Steps

1. **For Personal Use:** Just run `.\gradlew.bat assembleRelease` and install the APK
2. **For Play Store:** Create signing key, update version, build App Bundle
3. **For Updates:** Increment versionCode/versionName, rebuild, redistribute

**Your release build is configured and ready to go!** 🚀

