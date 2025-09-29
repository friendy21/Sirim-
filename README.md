# SIRIM QR Code Scanner

## Project Structure

The repository now contains a full multi-module Android application that implements the architecture described below. Key
modules:

- `app/` – Presentation layer built with Jetpack Compose and Hilt-powered navigation & screen view models.
- `core/domain/` – Kotlin-only module with business entities, repositories, and use cases for the clean architecture domain
  layer.
- `core/data/` – Android library implementing repositories, Retrofit networking, DataStore token handling, and Room-backed
  persistence abstractions.
- `core/database/` – Room database module with DAOs, entities, and converters dedicated to local storage.
- `core/network/` – Kotlin networking module with Retrofit DTOs and service definitions.
- `core/common/` – Shared utilities such as coroutine dispatcher providers.

Gradle is configured with Kotlin DSL files (`build.gradle.kts`) and Android Gradle Plugin 8.2.2. Use Android Studio Giraffe or
newer to open the project, then sync Gradle and run the `app` configuration on a device/emulator running Android 10 (API 29) or
higher. You can also build from the command line:

```bash
./gradlew assembleDebug
```

## Current Implementation Status

The latest build now ships with live CameraX/ML Kit scanning, biometric sign-in, encrypted credential storage, background
WorkManager sync, and Excel/PDF/ZIP exports with ShareSheet integration. Refer to
[`docs/gap-analysis.md`](docs/gap-analysis.md) for the smaller follow-up items (testing, CI, release hardening, advanced
telemetry) that remain outstanding.

## System Architecture and Technical Specifications

### 1. System Architecture

The proposed system architecture for the SIRIM QR Code Scanner application will follow a modern, multi-layered approach based on Google's recommended Android app architecture guidelines. This ensures a scalable, maintainable, and testable codebase. The architecture is composed of three primary layers: the Presentation Layer, the Domain Layer, and the Data Layer.

#### 1.1. Presentation Layer

This layer is responsible for displaying the application's user interface (UI) and handling user interactions. It is built using **Jetpack Compose**, Google's modern toolkit for building native Android UI. The **Model-View-ViewModel (MVVM)** architectural pattern separates the UI logic from the business logic.

- **Views (Composable Functions):** These are the UI elements that the user sees and interacts with. They observe data from the ViewModels and render it on the screen.
- **ViewModels:** These hold and manage UI-related data in a lifecycle-conscious way. They expose data to the UI through observable data holders (like `StateFlow` or `LiveData`) and handle user actions by calling the appropriate business logic in the Domain Layer.

#### 1.2. Domain Layer

The Domain Layer contains the core business logic of the application. It encapsulates complex business rules and data manipulation logic, making it independent of both the UI and the data sources. This layer consists of **Use Cases** (also known as Interactors) that represent single, discrete business operations.

- **Use Cases:** Each use case is responsible for a specific task, such as scanning a QR code, saving data to the local database, or exporting data to a file. They are called by the ViewModels and interact with the Data Layer to fetch or store data.

#### 1.3. Data Layer

The Data Layer provides the application with data from various sources, such as a local database or a remote network server. It consists of **Repositories** that abstract the data sources from the rest of the application.

- **Repositories:** The repositories provide a clean API for the Domain Layer to access data. They fetch data from the appropriate data source (local or remote) and handle data synchronization.
- **Data Sources:**
  - **Local Data Source:** Implemented using the **Room persistence library**, which provides an abstraction layer over SQLite. It is the primary source of truth for the application's data, enabling offline functionality.
  - **Remote Data Source (Optional):** For the optional online synchronization feature, a remote data source communicates with a backend server via a RESTful API. **Retrofit** is used for making network requests.

### 2. Technical Specifications

| Category | Technology/Specification | Justification |
| :--- | :--- | :--- |
| **Platform** | Android (Kotlin) | The application targets Android devices, and Kotlin is the officially recommended language for Android development, offering modern features and improved safety. |
| **Minimum SDK** | API Level 29 (Android 10) | Ensures compatibility with a wide range of modern Android devices while allowing the use of recent Android features. |
| **Architecture Pattern** | MVVM (Model-View-ViewModel) | Separates the UI from business logic, improving testability, maintainability, and scalability. |
| **UI Toolkit** | Jetpack Compose | A modern, declarative UI toolkit that simplifies and accelerates UI development on Android. |
| **QR Code Scanning & OCR** | Google ML Kit (Barcode Scanning API) | Provides a powerful and easy-to-use API for barcode scanning with high accuracy and features like auto-zoom, which is crucial for the app's core functionality. |
| **Local Storage** | Room Database | A robust and recommended persistence library for Android that provides an abstraction layer over SQLite, simplifying database operations and ensuring compile-time query verification. |
| **Data Export** |  |  |
| &nbsp;&nbsp;&nbsp;&nbsp;Excel | Apache POI | A mature and powerful Java library for creating and manipulating Microsoft Office file formats, including Excel. |
| &nbsp;&nbsp;&nbsp;&nbsp;PDF | iText 7 Community | A widely-used and feature-rich library for creating and manipulating PDF documents. |
| &nbsp;&nbsp;&nbsp;&nbsp;ZIP | `java.util.zip` | The standard Java library for creating and managing ZIP archives, providing a reliable and straightforward solution. |
| **Authentication** | Biometric Authentication (BiometricPrompt API) & Username/Password | Provides a secure and convenient way for users to authenticate using their fingerprint or face, with a traditional username/password fallback for devices without biometric sensors. |
| **Security** |  |  |
| &nbsp;&nbsp;&nbsp;&nbsp;Data at Rest | Jetpack Security (EncryptedSharedPreferences, EncryptedFile) | Provides best-practice security for encrypting app data at rest, protecting sensitive information stored on the device. |
| &nbsp;&nbsp;&nbsp;&nbsp;Key Management | Android Keystore System | Provides a secure container for storing and managing cryptographic keys, protecting them from extraction. |
| **Optional Online Sync** |  |  |
| &nbsp;&nbsp;&nbsp;&nbsp;Backend API | RESTful API (Ktor or Spring Boot) | A standard and flexible approach for client-server communication. Ktor is a modern Kotlin-based framework, while Spring Boot is a robust and widely adopted Java framework. |
| &nbsp;&nbsp;&nbsp;&circ;Database | PostgreSQL or MySQL | Powerful, open-source relational database systems capable of handling the requirements of the application. |
| &nbsp;&nbsp;&nbsp;&circ;Synchronization | WorkManager & Retrofit | WorkManager is the recommended solution for deferrable, asynchronous tasks, making it ideal for background data synchronization. Retrofit is a type-safe HTTP client for Android and Java. |


## Feature Specifications and User Experience Design

### 1. Detailed Feature Specifications

This section outlines the detailed features of the SIRIM QR Code Scanner application, broken down by functional area.

#### 1.1. Authentication

- **User Registration:** A secure registration process for new users, requiring a username and a strong password.
- **Login:** Users can log in using their registered username and password. The app supports biometric authentication (fingerprint or face ID) for faster and more convenient access.
- **Logout:** A clear and accessible logout option securely terminates the user's session.
- **Session Management:** The app implements secure session management to ensure that user data is protected, with session timeouts and secure token handling.

#### 1.2. QR Code Scanning and OCR

- **Real-time Scanning:** Provides a real-time camera preview for scanning SIRIM QR codes. The scanning process is initiated with a single tap from the main screen.
- **Accurate Recognition:** Leveraging Google's ML Kit, the app accurately detects and decodes SIRIM QR codes, as well as performs OCR on any associated text on the label.
- **Auto-zoom:** The camera automatically zooms in on the QR code if it is too far away, ensuring a high success rate for scanning.
- **Data Verification:** After a successful scan, the app displays the extracted information for the user to verify before saving it to the local database.

#### 1.3. Data Management

- **Local Storage:** All scanned data is stored locally on the device in a Room database, ensuring that the app is fully functional offline.
- **Data Viewing:** Users can view a list of all scanned records, with key information displayed for each record.
- **Search and Filter:** Powerful search and filtering functionality allows users to quickly find specific records based on various criteria (e.g., serial number, date, brand).
- **Record Details:** Tapping on a record opens a detailed view with all the information extracted from the QR code.
- **Data Editing and Deletion:** Users can edit or delete records as needed, with appropriate confirmation dialogs to prevent accidental data loss.

#### 1.4. Data Export

- **Multiple Formats:** Users can export one or more records to various formats, including Excel (.xlsx), PDF (.pdf), and a compressed ZIP archive.
- **Customizable Exports:** The app allows users to select which fields to include in the exported files.
- **Sharing:** After exporting, users can easily share the generated files through email, messaging apps, or other installed applications.

#### 1.5. Optional Online Synchronization

- **Manual and Automatic Sync:** The app provides both manual and automatic synchronization options. Users can trigger a sync manually, or the app can sync data in the background at regular intervals.
- **Conflict Resolution:** A robust conflict resolution strategy handles cases where data has been modified both locally and on the server.
- **Secure API Communication:** All communication with the backend server is encrypted using HTTPS to protect data in transit.

### 2. User Experience (UX) Design

The user experience is designed to be intuitive, efficient, and user-friendly, with a focus on simplicity and clarity.

#### 2.1. Onboarding and Authentication

- **Welcome Screen:** A simple and welcoming onboarding screen introduces the app's key features and benefits.
- **Permissions:** The app clearly explains why it needs certain permissions (e.g., camera access for scanning) and requests them at the appropriate time.
- **Login/Registration:** The login and registration screens are clean and straightforward, with clear instructions and error messages.

#### 2.2. Main Dashboard

- **Central Hub:** The main dashboard serves as the central hub of the app, providing quick access to all key features.
- **Scan Button:** A prominent and easily accessible button allows users to start the QR code scanning process with a single tap.
- **Recent Activity:** The dashboard displays a summary of recent activity, such as the number of records scanned and the last sync time.

#### 2.3. Scanning Flow

- **Intuitive Interface:** The scanning interface is simple and intuitive, with a clear viewfinder to guide the user.
- **Real-time Feedback:** The app provides real-time feedback during the scanning process, such as a progress indicator or a message indicating that it is searching for a QR code.
- **Confirmation and Editing:** After a successful scan, the app displays the extracted data in an editable format, allowing the user to make any necessary corrections before saving.

#### 2.4. Data Management and Export

- **Clear and Organized:** The list of scanned records is presented in a clear and organized manner, with sorting and filtering options to help users find what they need.
- **Effortless Export:** The data export process is simple and straightforward, with a step-by-step wizard to guide the user through selecting records, choosing an export format, and sharing the file.


## Security Framework and Data Protection Strategy

### 1. Authentication and Authorization

- **Strong Password Policy:** The application enforces a strong password policy for user registration, requiring a minimum length, a mix of character types (uppercase, lowercase, numbers, and special characters), and preventing the use of common or easily guessable passwords.
- **Biometric Authentication:** To provide a seamless and secure user experience, the app integrates with Android's `BiometricPrompt` API, allowing users to authenticate using their fingerprint or face ID. This is offered as a convenient alternative to password-based login.
- **Secure Token Management:** For session management, the application uses secure, short-lived JSON Web Tokens (JWTs). These tokens are stored securely in the Android Keystore, and the app implements a token refresh mechanism to ensure that sessions remain active without compromising security.
- **Role-Based Access Control (RBAC):** Although the initial MVP may have a single user role, the architecture is designed to support RBAC for future scalability. This allows for different levels of access and permissions to be assigned to different user roles (e.g., administrators, supervisors, and operators).

### 2. Data Encryption

- **Data at Rest:** All sensitive data stored on the device, including the local Room database and any exported files, is encrypted using the **Jetpack Security** library. Specifically, `EncryptedSharedPreferences` is used for storing sensitive key-value data (like authentication tokens), and `EncryptedFile` is used for encrypting the database and exported files. The encryption keys are managed by the **Android Keystore System**, which provides hardware-backed protection for cryptographic keys.
- **Data in Transit:** For the optional online synchronization feature, all communication between the mobile application and the backend server is encrypted using **Transport Layer Security (TLS) 1.3**. The app enforces HTTPS for all network requests and implements certificate pinning to prevent man-in-the-middle (MITM) attacks.

### 3. Secure Coding Practices

- **Input Validation:** All user input is rigorously validated on both the client-side and server-side to prevent common vulnerabilities such as SQL injection and cross-site scripting (XSS).
- **Code Obfuscation and Tamper Detection:** ProGuard or R8 is used to obfuscate the application's code, making it more difficult for attackers to reverse-engineer. The app also implements tamper detection mechanisms to check for any unauthorized modifications to the application's code or resources.
- **Principle of Least Privilege:** The application adheres to the principle of least privilege, requesting only the permissions that are absolutely necessary for its functionality. Permissions are requested contextually, with clear explanations to the user about why they are needed.
- **Secure Logging:** The application avoids logging any sensitive information, such as passwords, authentication tokens, or personal data. Any logging implemented for debugging purposes is disabled in the production release.

### 4. Dependency Management

- **Regular Audits:** The project's dependencies are regularly audited for known vulnerabilities using tools like the **OWASP Dependency-Check** plugin or GitHub's built-in security alerts.
- **Automated Updates:** A dependency management tool like **Dependabot** automatically creates pull requests to update dependencies to their latest, most secure versions.
- **Vetting Third-Party Libraries:** Before adding any new third-party libraries to the project, they are carefully vetted for their security posture, maintenance history, and community reputation.

### 5. Privacy

- **Data Minimization:** The application only collects and stores data that is essential for its functionality. No unnecessary user data is collected.
- **Privacy Policy:** A clear and comprehensive privacy policy is provided to users, explaining what data is collected, how it is used, and how it is protected. The privacy policy is easily accessible from within the application.
- **User Consent:** The application obtains explicit user consent before collecting any personal data or using any device features that require permissions.


## Database Schema and Synchronization Architecture

### 1. Local Database Schema (Room)

The local database is implemented using the Room persistence library, providing a robust and efficient solution for offline data storage. The schema stores all the information extracted from the SIRIM QR codes, as well as metadata for synchronization and auditing purposes.

#### 1.1. `sirim_records` Table

This table is the core of the local database, storing the details of each scanned SIRIM record.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY`, `AUTOINCREMENT` | A unique identifier for each record. |
| `sirim_serial_no` | `TEXT` | `NOT NULL`, `UNIQUE` | The SIRIM serial number (max 12 characters). This is used as a key for synchronization. |
| `batch_no` | `TEXT` |  | The batch number (max 200 characters). |
| `brand_trademark` | `TEXT` |  | The brand or trademark (max 1024 characters). |
| `model` | `TEXT` |  | The product model (max 1500 characters). |
| `type` | `TEXT` |  | The product type (max 1500 characters). |
| `rating` | `TEXT` |  | The product rating (max 600 characters). |
| `size` | `TEXT` |  | The product size (max 1500 characters). |
| `created_at` | `INTEGER` | `NOT NULL` | The timestamp (in milliseconds) when the record was created. |
| `updated_at` | `INTEGER` | `NOT NULL` | The timestamp (in milliseconds) when the record was last updated. |
| `is_synced` | `INTEGER` | `NOT NULL`, `DEFAULT 0` | A flag to indicate whether the record has been synchronized with the remote server (0 for false, 1 for true). |

#### 1.2. `users` Table

This table stores user information for authentication.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY`, `AUTOINCREMENT` | A unique identifier for each user. |
| `username` | `TEXT` | `NOT NULL`, `UNIQUE` | The user's username. |
| `password_hash` | `TEXT` | `NOT NULL` | The hashed and salted password for the user. |

### 2. Optional Remote Database Schema

For the optional online synchronization feature, a remote database is used on the backend server. The schema mirrors the local database schema to ensure seamless data transfer.

#### 2.1. `sirim_records` Table (Remote)

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `SERIAL` | `PRIMARY KEY` | A unique identifier for each record. |
| `sirim_serial_no` | `VARCHAR(12)` | `NOT NULL`, `UNIQUE` | The SIRIM serial number. |
| `batch_no` | `VARCHAR(200)` |  | The batch number. |
| `brand_trademark` | `VARCHAR(1024)` |  | The brand or trademark. |
| `model` | `VARCHAR(1500)` |  | The product model. |
| `type` | `VARCHAR(1500)` |  | The product type. |
| `rating` | `VARCHAR(600)` |  | The product rating. |
| `size` | `VARCHAR(1500)` |  | The product size. |
| `created_at` | `TIMESTAMP` | `NOT NULL` | The timestamp when the record was created. |
| `updated_at` | `TIMESTAMP` | `NOT NULL` | The timestamp when the record was last updated. |
| `user_id` | `INTEGER` | `FOREIGN KEY` to `users.id` | The ID of the user who created the record. |

#### 2.2. `users` Table (Remote)

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `SERIAL` | `PRIMARY KEY` | A unique identifier for each user. |
| `username` | `VARCHAR(255)` | `NOT NULL`, `UNIQUE` | The user's username. |
| `password_hash` | `VARCHAR(255)` | `NOT NULL` | The hashed and salted password for the user. |

### 3. Synchronization Architecture

The synchronization architecture is designed to be robust, efficient, and battery-friendly, using modern Android components.

- **Synchronization Trigger:** Synchronization can be triggered either manually by the user or automatically in the background. **WorkManager** schedules the background synchronization tasks, ensuring that they are executed even if the app is not running.
- **Data Transfer:** **Retrofit** handles communication with the backend RESTful API. Data is transferred in a lightweight JSON format.
- **Synchronization Logic:**
  1. The app first fetches all records from the local database that have the `is_synced` flag set to 0.
  2. These records are sent to the backend server in a batch.
  3. The server processes the batch, inserting new records and updating existing ones.
  4. After a successful sync, the server returns a list of the successfully synchronized records.
  5. The app updates the `is_synced` flag for these records in the local database.
- **Conflict Resolution:** A **last-write-wins** strategy is used for conflict resolution. The `updated_at` timestamp determines which version of a record is the most recent. If a record has been updated on both the client and the server, the version with the later `updated_at` timestamp is kept, and the other is discarded.


## Implementation Roadmap and Development Plan

### 1. Development Methodology

The project follows an **Agile (Scrum)** development methodology. The development process is divided into two-week sprints, with a clear set of goals for each sprint. This approach allows for flexibility, continuous feedback, and iterative development.

### 2. Sprint Breakdown

#### Sprint 1: Project Setup and Core Infrastructure (Weeks 1-2)

- **Goals:**
  - Set up the Android Studio project and version control (Git).
  - Implement the core application architecture (MVVM, Dagger Hilt for dependency injection).
  - Set up the Room database with the initial schema.
  - Implement the basic user authentication flow (registration, login, logout).
- **Deliverables:**
  - A basic Android application with a functional login screen.
  - A local database for storing user data.

#### Sprint 2: QR Code Scanning and Data Capture (Weeks 3-4)

- **Goals:**
  - Integrate the Google ML Kit library for barcode scanning.
  - Implement the real-time camera preview for scanning QR codes.
  - Develop the logic for extracting data from the scanned QR codes.
  - Implement the UI for displaying the scanned data for user verification.
- **Deliverables:**
  - A functional QR code scanner that can accurately read SIRIM QR codes.
  - A screen that displays the extracted data to the user.

#### Sprint 3: Data Management and Viewing (Weeks 5-6)

- **Goals:**
  - Implement the functionality to save scanned data to the local Room database.
  - Develop the UI for displaying a list of all scanned records.
  - Implement the search and filtering functionality for the record list.
  - Develop the detailed view for individual records.
- **Deliverables:**
  - A screen that displays a list of all saved records.
  - The ability to search, filter, and view the details of each record.

#### Sprint 4: Data Export and Sharing (Weeks 7-8)

- **Goals:**
  - Integrate the necessary libraries for exporting data to Excel (Apache POI) and PDF (iText).
  - Implement the functionality to export selected records to the chosen format.
  - Develop the UI for the export process, including field selection.
  - Implement the sharing functionality to allow users to share the exported files.
- **Deliverables:**
  - The ability to export data to Excel, PDF, and ZIP formats.
  - A seamless sharing experience for the exported files.

#### Sprint 5: Optional Online Synchronization (Weeks 9-10)

- **Goals:**
  - Set up the backend server and database.
  - Develop the RESTful API for data synchronization.
  - Implement the synchronization logic in the Android app using WorkManager and Retrofit.
  - Develop the UI for managing the synchronization settings.
- **Deliverables:**
  - A functional backend server for storing and managing data.
  - The ability to synchronize data between the mobile app and the backend server.

#### Sprint 6: Testing, Refinement, and Deployment (Weeks 11-12)

- **Goals:**
  - Conduct thorough testing of the entire application, including unit tests, integration tests, and UI tests.
  - Fix any bugs or issues identified during testing.
  - Refine the UI and UX based on user feedback.
  - Prepare the application for deployment to the Google Play Store.
- **Deliverables:**
  - A stable, well-tested, and polished application.
  - A signed APK or App Bundle ready for release.

### 3. Timeline

| Phase | Duration | Key Milestones |
| :--- | :--- | :--- |
| **Sprint 1** | 2 Weeks | Project setup, core architecture, basic authentication. |
| **Sprint 2** | 2 Weeks | QR code scanning and data capture. |
| **Sprint 3** | 2 Weeks | Data management and viewing. |
| **Sprint 4** | 2 Weeks | Data export and sharing. |
| **Sprint 5** | 2 Weeks | Optional online synchronization. |
| **Sprint 6** | 2 Weeks | Testing, refinement, and deployment. |
| **Total** | **12 Weeks** | **MVP Release** |
