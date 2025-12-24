# Play Store Production Keystore Creation Script
# This script will create a secure keystore for Google Play Store releases

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  MedReminder Play Store Keystore Creator  " -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "This will create a production keystore for Play Store submissions." -ForegroundColor Yellow
Write-Host ""

# Get keystore details
Write-Host "Enter details for your keystore:" -ForegroundColor Cyan
Write-Host "(This information will be embedded in your signing certificate)" -ForegroundColor Gray
Write-Host ""

Write-Host "Your name (e.g., John Smith): " -ForegroundColor Yellow -NoNewline
$name = Read-Host

Write-Host "Organizational Unit (e.g., Development Team or just 'Personal'): " -ForegroundColor Yellow -NoNewline
$orgUnit = Read-Host

Write-Host "Organization (e.g., Company Name or 'Personal'): " -ForegroundColor Yellow -NoNewline
$org = Read-Host

Write-Host "City: " -ForegroundColor Yellow -NoNewline
$city = Read-Host

Write-Host "State/Province: " -ForegroundColor Yellow -NoNewline
$state = Read-Host

Write-Host "Country Code (2 letters, e.g., US, UK, AU): " -ForegroundColor Yellow -NoNewline
$country = Read-Host

Write-Host ""
Write-Host "Create a secure password for your keystore:" -ForegroundColor Cyan
Write-Host "(Remember this! You'll need it for all future updates)" -ForegroundColor Red
Write-Host ""

$password = Read-Host "Keystore Password" -AsSecureString
$passwordConfirm = Read-Host "Confirm Password" -AsSecureString

# Convert to plain text for comparison
$pass1 = [Runtime.InteropServices.Marshal]::PtrToStringAuto([Runtime.InteropServices.Marshal]::SecureStringToBSTR($password))
$pass2 = [Runtime.InteropServices.Marshal]::PtrToStringAuto([Runtime.InteropServices.Marshal]::SecureStringToBSTR($passwordConfirm))

if ($pass1 -ne $pass2) {
    Write-Host "Passwords do not match! Exiting." -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Creating keystore..." -ForegroundColor Yellow
Write-Host ""

# Build the distinguished name
$dn = "CN=$name, OU=$orgUnit, O=$org, L=$city, ST=$state, C=$country"

# Create keystore file path
$keystorePath = "medreminder-release-key.jks"

# Run keytool command
$process = Start-Process -FilePath "keytool" -ArgumentList @(
    "-genkey",
    "-v",
    "-keystore", $keystorePath,
    "-keyalg", "RSA",
    "-keysize", "2048",
    "-validity", "10000",
    "-alias", "medreminder-key",
    "-storepass", $pass1,
    "-keypass", $pass1,
    "-dname", $dn
) -Wait -NoNewWindow -PassThru

if ($process.ExitCode -eq 0) {
    Write-Host ""
    Write-Host "============================================" -ForegroundColor Green
    Write-Host "  ✓ Keystore created successfully!         " -ForegroundColor Green
    Write-Host "============================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Keystore file: $keystorePath" -ForegroundColor Cyan
    Write-Host "Alias: medreminder-key" -ForegroundColor Cyan
    Write-Host ""

    # Save credentials to a secure text file
    $credFile = "keystore-credentials.txt"
    @"
MedReminder Play Store Keystore Credentials
============================================
Created: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')

Keystore File: medreminder-release-key.jks
Alias: medreminder-key
Password: $pass1

Distinguished Name:
$dn

IMPORTANT:
- Keep this file SECURE and PRIVATE
- Do NOT commit to Git/GitHub
- Backup the .jks file and this text file
- You'll need these for ALL future app updates
- If lost, you cannot update your app on Play Store

Next Steps:
1. Backup keystore file and credentials securely
2. Update app/build.gradle with these credentials
3. Build App Bundle: .\gradlew.bat bundleRelease
4. Upload .aab to Play Console
"@ | Out-File $credFile -Encoding UTF8

    Write-Host "✓ Credentials saved to: $credFile" -ForegroundColor Green
    Write-Host ""
    Write-Host "IMPORTANT WARNINGS:" -ForegroundColor Red
    Write-Host "1. BACKUP both files now to a secure location!" -ForegroundColor Yellow
    Write-Host "2. DO NOT commit these files to Git/GitHub!" -ForegroundColor Yellow
    Write-Host "3. If you lose these, you cannot update your app!" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Next: Update your build.gradle file" -ForegroundColor Cyan
    Write-Host ""

} else {
    Write-Host ""
    Write-Host "ERROR: Failed to create keystore" -ForegroundColor Red
    Write-Host "Exit code: $($process.ExitCode)" -ForegroundColor Red
    exit 1
}

# Clear password from memory
$pass1 = $null
$pass2 = $null

