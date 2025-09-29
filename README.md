# SIRIM QR Scanner Android MVP

This repository contains a Kotlin-based Android 15 (API 35) MVP that follows the Clean Architecture plan defined in `docs/sirim_qr_scanner_mvp_plan.md`. The app is built with Jetpack Compose, Hilt, Room, CameraX, and ML Kit barcode scanning to capture and manage SIRIM compliance QR codes completely offline with optional background sync hooks.

## Highlights

- **Android 15 ready:** `compileSdk`/`targetSdk` 35, Material 3 UI, and CameraX + ML Kit scanning.
- **Modular architecture:** MVVM + Use Cases + Repository abstraction backed by Room.
- **Secure foundations:** Hilt dependency injection, WorkManager-based sync scheduler, and placeholders for biometric and authentication flows.
- **Extensible exports:** Compose screens for Excel/PDF export actions with TODO hooks for implementation.

## Project Structure

```
app/
 ├─ build.gradle.kts        # Android application configuration
 └─ src/main/
     ├─ AndroidManifest.xml
     ├─ java/com/sirimqrscanner/app/
     │   ├─ data/…           # Room database and repository implementation
     │   ├─ di/…             # Hilt modules
     │   ├─ domain/…         # Models and use cases
     │   ├─ presentation/…   # Compose UI screens & navigation
     │   └─ sync/…           # WorkManager scheduling and workers
     └─ res/…                # Material 3 theming and adaptive icons
```

## Getting Started

1. Open the project in **Android Studio Ladybug (or newer)** with Android 15 SDK installed.
2. Let Android Studio download the missing Gradle wrapper when prompted (or run `gradle wrapper --gradle-version 8.7` from the command line).
3. Sync the Gradle project and run the `app` configuration on an Android 15 emulator or device.

### Runtime Notes

- The scanner requires camera permission; grant it manually the first time you run the app.
- ML Kit parsing populates the Room database and surfaces the latest scan in the UI.
- Background sync is scheduled via `WorkManager` and ready to connect to a real backend.

For the full product roadmap and security posture, see [`docs/sirim_qr_scanner_mvp_plan.md`](docs/sirim_qr_scanner_mvp_plan.md).
