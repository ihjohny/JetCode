# 🚀 JetCode - Modern Android Development Boilerplate

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![Kotlin](https://img.shields.io/badge/kotlin-2.0.21-blue.svg?logo=kotlin)](http://kotlinlang.org)

A modern, production-ready Android application template showcasing **Clean Architecture** with **MVI pattern**, **Jetpack Compose**, and modern Android development best practices. Built as a learning platform that dynamically loads JSON-based educational content.

## 📱 Screenshots

| Home Screen | Learning Module | Practice | Profile |
|-------------|-----------------|----------|---------|
| ![Home](docs/screenshots/1.png) | ![Learning](docs/screenshots/2.png) | ![Practice](docs/screenshots/3.png) | ![Profile](docs/screenshots/4.png) |

## ✨ Basic Features

### 🎯 Core Functionality
- **Learning Platform**: Dynamic JSON-based lesson content loading from GitHub
- **Skill Management**: Hierarchical structure (Skill → Topic → Lesson → Material)
- **Content Types**: 
  - 📝 Notes (Text/Markdown)
  - 💻 Code examples (Kotlin/Compose)
  - 🖼️ Images and visual content
  - 🎥 Video materials
- **Practice System**: 
  - Multiple Choice Questions (MCQs)
  - Code challenges
  - Output prediction exercises
- **User Profile**: Progress tracking and personalization
- **Offline Support**: Local caching with Room database
- **Background Sync**: Automatic content updates via WorkManager

### 🎨 UI/UX Features
- **Modern Design**: Material Design 3 with Jetpack Compose
- **Theme Support**: Light/Dark themes with multiple brand colors
- **Responsive Layout**: Adaptive UI for different screen sizes
- **Smooth Navigation**: Jetpack Navigation 3 with deep linking
- **Accessibility**: Full accessibility support

## 🛠️ Setup Guide

### Prerequisites
- **Android Studio**: Arctic Fox or newer
- **JDK**: 11 or higher
- **Android SDK**: API 24+ (Android 7.0)
- **Gradle**: 8.11.0

### 🚀 Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/jetcode.git
   cd jetcode
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory and select it

3. **Sync the project**
   - Android Studio will automatically start syncing
   - Wait for the Gradle sync to complete

4. **Configure API endpoints** (Optional)
   - Update `local.properties` with your configuration:
   ```properties
   # Add your custom configurations here
   api.base.url="https://your-api-endpoint.com"
   ```

5. **Run the application**
   - Select your device/emulator
   - Click the "Run" button or use `Ctrl+R` (Windows/Linux) / `Cmd+R` (Mac)

### 🔧 Build Variants
```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test

# Run UI tests
./gradlew connectedAndroidTest
```

## 🏗️ Architecture

### 📐 Clean Architecture Overview
```
┌─────────────────────────────────────────────────────────────┐
│                     Presentation Layer                     │
│  ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐ │
│  │   📱 Compose UI  │ │  🎯 ViewModels  │ │ 🧭 Navigation  │ │
│  └─────────────────┘ └─────────────────┘ └─────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                               │
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                          │
│  ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐ │
│  │   🔄 Use Cases  │ │   🏛️ Repository │ │   📋 Models    │ │
│  └─────────────────┘ └─────────────────┘ └─────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                               │
┌─────────────────────────────────────────────────────────────┐
│                       Data Layer                           │
│  ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐ │
│  │  🌐 Remote API  │ │  💾 Local DB    │ │ 🔧 Data Sources│ │
│  └─────────────────┘ └─────────────────┘ └─────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### 🧩 Modular Structure

```
📦 JetCode/
├── 📱 app/                          # Main application module
├── 🎯 features/                     # Feature modules
│   ├── learning/                    # Learning feature
│   ├── onboarding/                  # User onboarding
│   ├── practice/                    # Practice exercises
│   └── profile/                     # User profile
├── 🏗️ core/                        # Core shared modules
│   ├── analytics/                   # Analytics framework
│   ├── common/                      # Shared utilities
│   ├── designsystem/                # Design system & theming
│   ├── network/                     # Network layer
│   └── ui/                          # Shared UI components
├── 💾 data/                         # Data layer modules
│   ├── database/                    # Local database (Room)
│   └── repository/                  # Repository implementations
└── 🏛️ domain/                       # Business logic
```

### 🔄 MVI Pattern Implementation

```kotlin
// State Management Flow
UI Event → ViewModel → Use Case → Repository → Data Source
    ↑                                                ↓
UI State ← State Reduction ← Business Logic ← API Response
```

**Key Components:**
- **State**: Immutable data class representing UI state
- **Event**: User interactions and system events
- **Effect**: Side effects like navigation, snackbars
- **Reducer**: Pure functions for state transformation

## 🔧 Technologies & Libraries

### 🏗️ Architecture & Pattern
- **🏛️ Clean Architecture**: Separation of concerns with clear boundaries
- **🔄 MVI Pattern**: Unidirectional data flow for predictable state management
- **🧩 Multi-module**: Scalable modular architecture
- **💉 Koin**: Lightweight dependency injection

### 🎨 UI & Design
- **🎨 Jetpack Compose**: Modern declarative UI toolkit
- **🧭 Navigation Compose**: Type-safe navigation with deep linking
- **🎨 Material Design 3**: Latest Material Design system
- **🌙 Theme Support**: Dynamic theming with light/dark modes
- **🖼️ Coil**: Efficient image loading for Compose

### 💾 Data & Storage
- **🏠 Room Database**: Local data persistence with SQLite
- **💾 DataStore**: Modern preferences management
- **🌐 Ktor Client**: Powerful HTTP client for API communication
- **📄 Kotlinx Serialization**: Type-safe JSON serialization

### ⚡ Async & Background
- **🔄 Kotlin Coroutines**: Asynchronous programming
- **🔄 Kotlin Flow**: Reactive streams for data flow
- **⚙️ WorkManager**: Reliable background task execution

### 🧪 Testing
- **🧪 JUnit 4**: Unit testing framework
- **🎭 MockK**: Mocking library for Kotlin
- **🧪 Compose Testing**: UI testing for Jetpack Compose
- **🧪 Coroutines Test**: Testing coroutines and flows

### 🔧 Development Tools
- **🔍 Detekt**: Static code analysis for Kotlin
- **🎨 Ktlint**: Kotlin linting and code formatting
- **📝 Timber**: Flexible logging library
- **📊 Analytics Ready**: Structured analytics framework

### 📦 Version Catalog
All dependencies are managed through Gradle Version Catalog (`gradle/libs.versions.toml`) for:
- **Centralized version management**
- **Type-safe dependency declarations**
- **Easy maintenance and updates**

### 🔧 Key Dependencies
| Category | Library | Version |
|----------|---------|---------|
| **Kotlin** | Kotlin | 2.0.21 |
| **Android** | AGP | 8.11.0 |
| **Compose** | Compose BOM | 2024.09.00 |
| **Navigation** | Navigation Compose | 2.8.0 |
| **DI** | Koin | 3.5.6 |
| **Networking** | Ktor | 2.3.12 |
| **Database** | Room | 2.6.1 |
| **Testing** | MockK | 1.13.12 |

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details.

### 📋 Development Workflow
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### 🧪 Code Quality
- Run `./gradlew detekt` for static analysis
- Run `./gradlew ktlintCheck` for code formatting
- Ensure all tests pass with `./gradlew test`
- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Google** for Android and Jetpack libraries
- **JetBrains** for Kotlin and amazing tooling
- **Open Source Community** for incredible libraries and inspiration

## 📞 Support

- 📚 [Documentation](docs/)
- 🐛 [Report Issues](https://github.com/yourusername/jetcode/issues)
- 💬 [Discussions](https://github.com/yourusername/jetcode/discussions)

---

⭐ **Star this repository if it helped you!**

Built with ❤️ for the Android community
