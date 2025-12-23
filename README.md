
# MedReminder Android App

A simple yet effective Android application for tracking medication intake. Users can configure up to 8 medications and receive visual feedback on dose availability.

## Features

- **Configure Up to 8 Medications**: Add and manage multiple medications with custom settings
- **Dosage Tracking**: Track dosage information for each medication
- **Smart Dose Availability**: 
  - Green button: Dose is available (minimum time elapsed and daily limit not reached)
  - Red button: Dose is not available (too soon or daily limit reached)
- **Time Between Doses**: Set minimum time required between doses
- **Daily Limits**: Configure maximum doses per 24-hour period
- **Dose History**: View when each medication was last taken
- **Local Data Persistence**: All data is stored locally on device

## Project Structure

```
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
│   │   │   │   ├── ui/                # UI Components
│   │   │   │   │   ├── MainActivity.kt
│   │   │   │   │   ├── MedicationButtonView.kt
│   │   │   │   │   └── MedicationDialog.kt
│   │   │   │   └── utils/             # Utilities
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

- **Language**: Kotlin
- **Database**: Room (SQLite)
- **Async**: Coroutines & Flow
- **UI**: Android Material Design
- **Architecture**: Repository Pattern with Clean Architecture principles

## Getting Started

### Prerequisites

- Android Studio 2024.1 or later
- JDK 17 or later
- Android SDK 26+ (API level 26)
- Gradle 8.0+

### Build & Run

1. Clone the repository
2. Open in Android Studio
3. Build the project:
   ```bash
   ./gradlew build
   ```
4. Run on emulator or device:
   ```bash
   ./gradlew installDebug
   ```

### Build Variants

- **Debug**: Development build with debugging enabled
- **Release**: Optimized release build with ProGuard

## How to Use

### Adding a Medication

1. Tap the "Add Medication" button
2. Enter medication details:
   - **Name**: Medication name (e.g., Aspirin)
   - **Dosage**: Dosage information (e.g., 500mg)
   - **Min Time Between Doses**: Minimum minutes between doses
   - **Max Doses Per 24 Hours**: Maximum number of doses allowed in a day
3. Tap "Save"

### Taking a Dose

1. When a button is **green**, the dose is available
2. Tap the medication button to record taking the dose
3. The button will turn **red** if dose limits are reached or minimum time hasn't elapsed
4. The display shows the medication name and time of last dose

### Editing/Deleting Medications

- Tap "Edit" on a medication to modify its settings
- Tap "Delete" to remove a medication and all associated dose history

## Database Schema

### Medications Table
- `id`: Primary key
- `name`: Medication name
- `dosage`: Dosage information
- `minTimeBetweenDoses`: Minimum minutes between doses
- `maxDosesPerDay`: Max doses in 24 hours
- `createdAt`: Timestamp of creation
- `isActive`: Active status flag

### Doses Table
- `id`: Primary key
- `medicationId`: Foreign key to Medication
- `timestamp`: When dose was taken

## Architecture

### Repository Pattern
The `MedicationRepository` provides a clean abstraction layer between the UI and database operations, handling all data access logic.

### Availability Logic
`DoseAvailabilityChecker` encapsulates the business logic for determining if a dose can be taken based on:
- Whether the minimum time between doses has elapsed
- Whether the daily dose limit has been reached

## Future Enhancement Ideas

- Notifications/reminders for due medications
- Dose history statistics and graphs
- Multiple users/profiles
- Cloud synchronization
- Backup and restore functionality

## License

This project is open source and available under the MIT License.

## Support

For issues, questions, or feature requests, please contact the development team or create an issue on GitHub.
