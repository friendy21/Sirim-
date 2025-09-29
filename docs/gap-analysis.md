# SIRIM QR Scanner Gap Analysis

This document is retained as a running ledger of capabilities that still require attention after the latest implementation pass.
The major blockers called out previously (camera scanning, biometric hardening, data export, and background sync) are now
addressed in code. The remaining items focus on polish, robustness, and production-readiness tasks.

## 1. Core Functionality
- ✅ **Camera-based QR scanning with ML Kit and CameraX** is now in place, including runtime permission handling and live OCR
  assistance for label text.
- 🔄 **Advanced QR heuristics** (e.g., confidence scoring, duplicate detection across sessions, tamper detection) are still
  potential enhancements.

## 2. Security & Authentication
- ✅ **Biometric authentication** via `BiometricPrompt` allows returning users to unlock with stored credentials.
- ✅ **Jetpack Security encrypted preferences** protect cached tokens and credentials, and passwords are persisted with bcrypt
  hashing for verification.
- 🔄 **Device attestation, hardware-backed keys, and encrypted file exports** remain future hardening opportunities.

## 3. Data Export & Sharing
- ✅ **Excel, PDF, and ZIP exports** are generated with Apache POI, iText 7, and the standard ZIP utilities.
- ✅ **Android ShareSheet integration** enables secure file sharing through a `FileProvider` sandbox.
- 🔄 **Custom export templating** (branding, filtering, column selection) can be layered on in subsequent iterations.

## 4. Repository & Synchronization Logic
- ✅ **Authentication and synchronization repositories** now execute real network flows, refreshes, and WorkManager-powered
  periodic sync jobs.
- 🔄 **Conflict resolution strategies** (per-field merges, diff visualisation) and richer offline queues are still on the roadmap.

## 5. UI/UX Functionality
- ✅ **Scanner screen** renders a live camera preview with realtime detection feedback and editable fallback fields.
- ✅ **Export workflows** surface progress indicators, format pickers, and error messaging.
- 🔄 **Accessibility reviews, localisation, and richer sync progress UIs** remain to be completed.

## 6. Testing & Quality Assurance
- ❌ Unit, integration, and UI automation are not yet implemented.
- ❌ Static analysis (Detekt/KtLint), dependency auditing, and CI/CD automation are still absent.

## 7. Production Readiness
- ❌ Hardened ProGuard/R8 rules, release signing documentation, crash/analytics wiring, and performance profiling are still
  outstanding.

## 8. Documentation
- ❌ Dedicated API, setup, deployment, and end-user documentation continues to be a gap and should follow once the above areas are
  stabilised.

These remaining items should be prioritized to deliver a production-ready application that aligns with the original specification.
