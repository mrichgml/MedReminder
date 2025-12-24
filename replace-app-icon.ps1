param(
    [Parameter(Mandatory=$true)]
    [string]$SourceImage
)

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  MedReminder App Icon Replacement Script  " -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Check if source image exists
if (-not (Test-Path $SourceImage)) {
    Write-Host "ERROR: Source image not found: $SourceImage" -ForegroundColor Red
    exit 1
}

Write-Host "Source Image: $SourceImage" -ForegroundColor Green
Write-Host ""

# Define output directories and sizes
$resPath = "D:\Git\Teckon\app\src\main\res"
$sizes = @{
    "mipmap-mdpi"    = 48
    "mipmap-hdpi"    = 72
    "mipmap-xhdpi"   = 96
    "mipmap-xxhdpi"  = 144
    "mipmap-xxxhdpi" = 192
}

# Check if we have ImageMagick or another tool
Write-Host "Checking for image processing tools..." -ForegroundColor Yellow

# Try to use .NET System.Drawing (built into Windows)
Add-Type -AssemblyName System.Drawing

try {
    $sourceImg = [System.Drawing.Image]::FromFile((Resolve-Path $SourceImage).Path)
    Write-Host "Successfully loaded image: $($sourceImg.Width)x$($sourceImg.Height) pixels" -ForegroundColor Green
    Write-Host ""

    foreach ($folder in $sizes.Keys) {
        $size = $sizes[$folder]
        $outputDir = Join-Path $resPath $folder
        $outputFile = Join-Path $outputDir "ic_launcher.png"

        # Create directory if it doesn't exist
        if (-not (Test-Path $outputDir)) {
            New-Item -ItemType Directory -Path $outputDir -Force | Out-Null
        }

        # Backup existing icon
        if (Test-Path $outputFile) {
            $backupFile = Join-Path $outputDir "ic_launcher_backup.png"
            Copy-Item $outputFile $backupFile -Force
            Write-Host "Backed up existing icon: $backupFile" -ForegroundColor Yellow
        }

        # Resize and save
        $resizedImg = New-Object System.Drawing.Bitmap $size, $size
        $graphics = [System.Drawing.Graphics]::FromImage($resizedImg)
        $graphics.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
        $graphics.DrawImage($sourceImg, 0, 0, $size, $size)
        $graphics.Dispose()

        # Save as PNG
        $resizedImg.Save($outputFile, [System.Drawing.Imaging.ImageFormat]::Png)
        $resizedImg.Dispose()

        Write-Host "✓ Created ${size}x${size} icon: $outputFile" -ForegroundColor Green
    }

    $sourceImg.Dispose()

    Write-Host ""
    Write-Host "============================================" -ForegroundColor Cyan
    Write-Host "  Icon replacement complete!                " -ForegroundColor Green
    Write-Host "============================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Next steps:" -ForegroundColor Yellow
    Write-Host "1. Clean build: .\gradlew.bat clean" -ForegroundColor White
    Write-Host "2. Rebuild app: .\gradlew.bat assembleDebug" -ForegroundColor White
    Write-Host "3. Install app: .\gradlew.bat installDebug" -ForegroundColor White
    Write-Host ""

} catch {
    Write-Host ""
    Write-Host "ERROR: Failed to process image using .NET" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    Write-Host ""
    Write-Host "ALTERNATIVE METHOD:" -ForegroundColor Yellow
    Write-Host "Please use the Android Asset Studio online tool:" -ForegroundColor White
    Write-Host "https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html" -ForegroundColor Cyan
    Write-Host ""
    exit 1
}

