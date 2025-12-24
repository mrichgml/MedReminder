# App Icon Replacement Guide

## Quick Method: Use Android Asset Studio (Easiest)

### Option 1: Online Tool (Recommended)
1. Go to: https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html
2. Upload your PNG file
3. Adjust padding/scaling if needed
4. Click "Download" to get a zip file
5. Extract the zip file
6. Copy all the `mipmap-*` folders to: `D:\Git\Teckon\app\src\main\res\`
7. Replace existing folders when prompted

### Option 2: Manual Replacement
If you want to manually replace the icons, you need these sizes:

- **mipmap-mdpi**: 48x48 px
- **mipmap-hdpi**: 72x72 px
- **mipmap-xhdpi**: 96x96 px
- **mipmap-xxhdpi**: 144x144 px
- **mipmap-xxxhdpi**: 192x192 px

## Using PowerShell Script (Below)

I've created a PowerShell script that will help you resize and copy your icon.
Just run: `.\replace-app-icon.ps1 -SourceImage "path\to\your\icon.png"`

---

## Current Icon Locations in Project:
```
D:\Git\Teckon\app\src\main\res\
├── mipmap-mdpi\ic_launcher.png
├── mipmap-hdpi\ic_launcher.png
├── mipmap-xhdpi\ic_launcher.png
├── mipmap-xxhdpi\ic_launcher.png
├── mipmap-xxxhdpi\ic_launcher.png
└── mipmap-anydpi-v26\
    ├── ic_launcher.xml
    └── ic_launcher_foreground.xml
```

## After Replacing Icons:
1. Clean the build: `.\gradlew.bat clean`
2. Rebuild the app: `.\gradlew.bat assembleDebug`
3. Install on device: `.\gradlew.bat installDebug`

The new icon will appear on your device!

