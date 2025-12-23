# Contributing to MedReminder

Thank you for your interest in contributing to MedReminder! We welcome bug reports, feature requests, and pull requests.

## Getting Started

### Prerequisites
- Android Studio 2024.1 or later
- JDK 17 or later
- Kotlin 1.9+
- Git

### Setup
1. Fork the repository
2. Clone your fork: `git clone https://github.com/YOUR_USERNAME/MedReminder.git`
3. Create a feature branch: `git checkout -b feature/your-feature-name`
4. Open the project in Android Studio

## Development Workflow

### Code Style
- Follow Kotlin style guidelines
- Use meaningful variable and function names
- Add comments for complex logic
- Keep functions small and focused

### Commit Messages
- Use clear, descriptive commit messages
- Start with a verb: "Add feature", "Fix bug", "Improve performance"
- Reference related issues: "Fixes #123"

### Testing
Before submitting a PR:
1. Test on Android emulator and/or physical device
2. Test with various Android versions (API 26+)
3. Verify no crashes or warnings

## Reporting Issues

### Bug Reports
Use the bug report template and include:
- Clear description of the issue
- Steps to reproduce
- Expected vs actual behavior
- Device and Android version information
- Screenshots if applicable

### Feature Requests
Use the feature request template and include:
- Clear description of the desired feature
- Problem statement
- Proposed solution
- Use cases

## Submitting a Pull Request

1. Ensure your code follows project style guidelines
2. Update README.md or documentation if needed
3. Add a clear description of your changes
4. Reference any related issues
5. Ensure the project builds without errors

## Project Structure

```
MedReminder/
├── app/src/main/java/com/example/medreminder/
│   ├── data/              # Database and Repository
│   ├── logic/             # Business Logic
│   ├── models/            # Data Models
│   ├── ui/                # UI Components
│   └── utils/             # Utility Functions
└── app/src/main/res/      # Resources
```

## Questions?

Feel free to open a discussion or issue if you have questions about contributing.

## License

By contributing, you agree that your contributions will be licensed under the MIT License.
