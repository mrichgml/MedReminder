# MedReminder Android App

A simple yet effective Android application for tracking medication intake. Users can configure up to 8 medications and receive visual feedback on dose availability.

## Features

- Configure up to 8 medications with custom settings
- Dosage tracking per medication
- Smart dose availability with clear colors:
  - Green button: dose is available (minimum time elapsed and daily limit not reached)
  - Red button: dose is not available (too soon or daily limit reached)
- Minimum time between doses in hours (supports fractional hours, e.g. 0.5, 1.5)
- Daily limits: maximum doses per rolling 24 hours
- Per‑medication notifications when the next dose becomes available (optional)
- Automatic UI refresh: buttons update every minute while the app is open
- Time display options:
  - Show actual “Earliest Next” time or a countdown (e.g. "3h 45m")
  - Toggle between 12‑hour (AM/PM) and 24‑hour clock
- Local data persistence (Room/SQLite)

## What each medication button shows

- Medication name
- Taken: the time the last dose was taken
- Earliest Next: either
  - the next available time (respecting the minimum interval), or
  - a live countdown until that time (if countdown mode is enabled)

Buttons automatically turn green at the eligible time—no manual refresh needed.

## Project Structure

```text
MedReminder/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/medreminder/
│   │   │   │   ├── data/              # Database and Repository
│   │   │   │   │   ├── MedReminderDatabase.kt
│   │   │   │   │   ├── MedicationDao.kt
│   │   │   │   │   ├── DoseDao.kt
│   │   │   │   │   └── MedicationRepository.kt
│   │   │   │   ├── logic/             # Business Logic
│   │   │   │   │   └── DoseAvailabilityChecker.kt
│   │   │   │   ├── models/            # Data Models
│   │   │   │   │   ├── Medication.kt
│   │   │   │   │   └── Dose.kt
│   │   │   │   ├── notifications/     # Background checks + notifications
│   │   │   │   │   ├── MedicationCheckWorker.kt
│   │   │   │   │   ├── NotificationHelper.kt
│   │   │   │   │   └── NotificationScheduler.kt
│   │   │   │   ├── ui/                # UI Components
│   │   │   │   │   ├── MainActivity.kt
│   │   │   │   │   ├── MedicationButtonView.kt
│   │   │   │   │   └── MedicationDialog.kt
│   │   │   │   └── utils/             # Utilities & preferences
│   │   │   │       ├── PreferencesManager.kt
│   │   │   │       └── TimeFormatter.kt
│   │   │   ├── res/                   # Resources
│   │   │   │   ├── layout/
│   │   │   │   ├── values/
│   │   │   │   └── xml/
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
└── README.md
```

## Technology Stack

- Language: Kotlin
- Database: Room (SQLite)
- Async: Coroutines & Flow
- UI: Android Material Design
- Background work: WorkManager (periodic checks for notifications)
- Architecture: Repository Pattern with Clean Architecture principles

## Getting Started

### Prerequisites

- Android Studio 2024.1 or later
- JDK 17 or later
- Android SDK 26+ (API level 26)
- Gradle 8.0+

### Build & Run

On Windows PowerShell:

```powershell
# Build debug
.\gradlew.bat assembleDebug

# Install on connected device
.\gradlew.bat installDebug
```

On macOS/Linux:

```bash
./gradlew assembleDebug
./gradlew installDebug
```

### Build Variants

- Debug: development build with debugging enabled
- Release: optimized, signed build (ProGuard + resource shrinking)

## How to Use

### Adding a medication

1. Tap “Add Medication”
2. Enter details:
   - Name (e.g., Aspirin)
   - Dosage (e.g., 500mg)
   - Min Time Between Doses (hours; decimals allowed, e.g., 0.5)
   - Max Doses Per 24 Hours
3. Optionally enable notifications for that medication
4. Tap “Save”

### Taking a dose

- When a button is green, tap it to record a dose
- Button turns red after taking a dose or when limits are reached
- The button displays:
  - Medication name
  - Taken: last dose time
  - Earliest Next: next time or a countdown (depending on your setting)

### Editing/deleting

- Tap “Edit” to modify settings
- Tap “Delete” to remove the medication (and its dose history)

## Settings & Preferences

Open the menu (⋮) to toggle:

- Show Countdown: switch between showing a countdown or the exact "Earliest Next" time
- Use 24‑Hour Format: switch between 12‑hour (AM/PM) and 24‑hour time

These preferences are saved and applied across the app.

## Notifications

- Optional per‑medication notifications alert you when the next dose becomes available
- Background checks run periodically using WorkManager (no server required)
- On Android 13+, the app requests notification permission on first launch

## Database Schema

### Medications Table
- `id`: Primary key
- `name`: Medication name
- `dosage`: Dosage information
- `minTimeBetweenDoses`: Minimum hours between doses (Double; supports fractional hours)
- `maxDosesPerDay`: Max doses in rolling 24 hours
- `createdAt`: Timestamp of creation
- `isActive`: Active status flag
- `notificationsEnabled`: Whether notifications are enabled for this medication

### Doses Table
- `id`: Primary key
- `medicationId`: Foreign key to Medication
- `timestamp`: When dose was taken (epoch millis)

## Availability Logic

`DoseAvailabilityChecker` determines if a dose can be taken based on:
- Minimum time (hours) since last dose
- Daily maximum doses per 24 hours

## Future Enhancement Ideas

- Dose history statistics and graphs
- Multiple users/profiles
- Cloud synchronization
- Backup and restore functionality

## License

This project is open source and available under the MIT License.

## Support

For issues, questions, or feature requests, please create an issue on GitHub.
