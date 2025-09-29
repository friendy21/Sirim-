# SIRIM QR Code Scanner Android App - Comprehensive MVP Plan

**Author:** Manus AI  
**Date:** September 29, 2025  
**Target Platform:** Android 15 (API Level 35)

---

## Executive Summary

This comprehensive plan outlines the development of a sophisticated Android application designed to capture and process Malaysia SIRIM QR codes using advanced OCR technology. The application will provide robust offline functionality with optional cloud synchronization, secure authentication, and multiple data export formats. Built for Android 15, the app leverages the latest platform capabilities to deliver enterprise-grade security and performance.

---

# System Architecture and Technical Specifications

## 1. System Architecture

The proposed system architecture for the SIRIM QR Code Scanner application follows a modern, multi-layered approach based on Google's recommended Android app architecture guidelines. This ensures a scalable, maintainable, and testable codebase optimized for Android 15's enhanced capabilities. The architecture comprises three primary layers: the Presentation Layer, the Domain Layer, and the Data Layer.

### 1.1. Presentation Layer

This layer handles the application's user interface and user interactions, built using **Jetpack Compose** with Android 15's enhanced UI capabilities. The **Model-View-ViewModel (MVVM)** architectural pattern separates UI logic from business logic, taking advantage of Android 15's improved lifecycle management and state handling.

The Presentation Layer utilizes **Composable Functions** as UI elements that observe data from ViewModels and render content on screen. **ViewModels** manage UI-related data in a lifecycle-conscious manner, exposing data through observable holders like `StateFlow` and handling user actions by calling appropriate business logic in the Domain Layer.

### 1.2. Domain Layer

The Domain Layer encapsulates the core business logic, remaining independent of both UI and data sources. This layer consists of **Use Cases** (Interactors) that represent discrete business operations such as QR code scanning, data storage, and file export operations.

### 1.3. Data Layer

The Data Layer provides data access through **Repositories** that abstract data sources from the application. It includes both **Local Data Sources** implemented using Room persistence library with Android 15's enhanced SQLite performance, and optional **Remote Data Sources** for cloud synchronization using Retrofit with Android 15's improved network security features.

## 2. Technical Specifications

| Category | Technology/Specification | Justification |
| :--- | :--- | :--- |
| **Platform** | Android 15 (Kotlin) | Latest Android platform with enhanced security, performance improvements, and modern development features. Kotlin remains the officially recommended language. |
| **Minimum SDK** | API Level 35 (Android 15) | Targets the latest Android version for maximum security, performance, and access to cutting-edge features like enhanced biometric authentication and improved camera APIs. |
| **Target SDK** | API Level 35 (Android 15) | Ensures compliance with Google Play Store requirements and access to all Android 15 features. |
| **Architecture Pattern** | MVVM with Clean Architecture | Proven pattern enhanced by Android 15's improved lifecycle management and state preservation capabilities. |
| **UI Toolkit** | Jetpack Compose (Latest) | Modern declarative UI toolkit with Android 15 optimizations for better performance and new UI components. |
| **QR Code Scanning & OCR** | Google ML Kit (Latest) | Enhanced ML Kit with Android 15 optimizations, improved accuracy, and better camera integration. |
| **Camera Integration** | CameraX with Android 15 enhancements | Leverages Android 15's Low Light Boost and improved camera controls for better QR code scanning in various lighting conditions. |
| **Local Storage** | Room Database (Latest) | Enhanced Room with Android 15's improved SQLite performance and better encryption support. |
| **Data Export** | | |
| &nbsp;&nbsp;&nbsp;&nbsp;Excel | Apache POI (Latest) | Mature library with Android 15 compatibility for Excel file generation. |
| &nbsp;&nbsp;&nbsp;&nbsp;PDF | iText 7 Community | Enhanced PDF generation with Android 15's improved PDF rendering capabilities. |
| &nbsp;&nbsp;&nbsp;&nbsp;ZIP | `java.util.zip` + Android 15 enhancements | Standard library with Android 15's improved compression algorithms. |
| **Authentication** | Android 15 Enhanced Biometric API | Utilizes Android 15's improved biometric authentication with better security and user experience. |
| **Security** | | |
| &nbsp;&nbsp;&nbsp;&nbsp;Data at Rest | Android 15 Enhanced Encryption | Leverages Android 15's improved encryption capabilities and hardware security module integration. |
| &nbsp;&nbsp;&nbsp;&nbsp;Key Management | Android Keystore (Enhanced) | Android 15's enhanced Keystore with better hardware-backed security. |
| &nbsp;&nbsp;&nbsp;&nbsp;App Security | Android 15 Theft Protection | Integrates with Android 15's new theft protection features for enhanced device security. |
| **Optional Online Sync** | | |
| &nbsp;&nbsp;&nbsp;&nbsp;Backend API | RESTful API (Ktor/Spring Boot) | Modern API frameworks with Android 15's enhanced network security protocols. |
| &nbsp;&nbsp;&nbsp;&nbsp;Database | PostgreSQL/MySQL | Robust database systems compatible with Android 15's synchronization capabilities. |
| &nbsp;&nbsp;&nbsp;&nbsp;Synchronization | WorkManager (Enhanced) | Android 15's improved WorkManager with better background processing and battery optimization. |

---

# Feature Specifications and User Experience Design

## 1. Core Features

### 1.1. Enhanced Authentication System

The authentication system leverages Android 15's advanced security features to provide multiple layers of protection. Users can register with secure credentials following enhanced password policies that integrate with Android 15's password management improvements. The system supports **biometric authentication** using Android 15's enhanced BiometricPrompt API, which provides improved fingerprint and face recognition capabilities with better security and user experience.

**Session management** implements secure, short-lived JSON Web Tokens stored in Android 15's enhanced Keystore system. The application includes automatic session timeout and secure token refresh mechanisms that work seamlessly with Android 15's improved background processing capabilities.

### 1.2. Advanced QR Code Scanning and OCR

The scanning functionality utilizes Google ML Kit's latest capabilities enhanced for Android 15, providing **real-time scanning** with improved accuracy and performance. The system leverages Android 15's **Low Light Boost** feature for better QR code recognition in challenging lighting conditions, automatically adjusting camera exposure for optimal scanning results.

**Auto-zoom functionality** has been enhanced with Android 15's improved camera controls, providing more precise focusing and better success rates. The application includes **data verification** screens that allow users to review and edit extracted information before storage, with Android 15's improved input validation and error handling.

### 1.3. Comprehensive Data Management

All scanned data is stored locally using Room database with Android 15's enhanced SQLite performance and security features. The **data viewing interface** provides intuitive list displays with advanced **search and filtering** capabilities that leverage Android 15's improved text processing and search algorithms.

**Record management** includes detailed views for individual records, with editing and deletion capabilities protected by confirmation dialogs. The system implements Android 15's enhanced data protection features to ensure secure local storage and prevent unauthorized access.

### 1.4. Multi-Format Data Export

The export system supports multiple formats including **Excel (.xlsx)**, **PDF (.pdf)**, and **ZIP archives**. Users can customize exports by selecting specific fields and records, with the system leveraging Android 15's improved file handling and sharing capabilities.

**Sharing functionality** integrates with Android 15's enhanced sharing system, providing seamless file distribution through email, messaging apps, and cloud storage services with improved security and privacy controls.

### 1.5. Optional Cloud Synchronization

The synchronization system provides both **manual and automatic sync** options, utilizing Android 15's enhanced WorkManager for efficient background processing. **Conflict resolution** implements a sophisticated last-write-wins strategy with timestamp-based decision making.

All **API communication** uses HTTPS with Android 15's enhanced TLS implementation and certificate pinning for maximum security during data transmission.

## 2. User Experience Design

### 2.1. Modern Interface Design

The user interface follows Android 15's Material Design 3 guidelines, providing a clean, intuitive experience optimized for the latest platform capabilities. The **onboarding process** introduces key features with clear explanations of permissions and functionality.

### 2.2. Streamlined Workflow

The **main dashboard** serves as a central hub with quick access to all features, prominently displaying a scan button for immediate QR code capture. **Recent activity summaries** provide users with quick insights into their scanning history and synchronization status.

### 2.3. Efficient Scanning Experience

The **scanning interface** provides real-time feedback with clear visual indicators and progress updates. Android 15's enhanced camera capabilities ensure optimal performance across various lighting conditions and device orientations.

### 2.4. Organized Data Management

The **record management interface** presents data in clear, organized formats with powerful sorting and filtering options. The **export wizard** guides users through the process of selecting records, choosing formats, and sharing files with minimal complexity.

---

# Security Framework and Data Protection Strategy

## 1. Enhanced Authentication and Authorization

The application implements a comprehensive security framework leveraging Android 15's advanced security features. **Strong password policies** are enforced during registration, requiring complex passwords that integrate with Android 15's enhanced password validation systems.

**Biometric authentication** utilizes Android 15's improved BiometricPrompt API, providing enhanced security through hardware-backed biometric verification. The system supports multiple biometric modalities with fallback options for devices without specific sensors.

**Secure token management** employs JSON Web Tokens with enhanced encryption, stored in Android 15's improved Keystore system. Token refresh mechanisms ensure continuous security without compromising user experience.

**Role-based access control** architecture supports future scalability, allowing different permission levels for various user roles including administrators, supervisors, and operators.

## 2. Advanced Data Encryption

**Data at rest** protection utilizes Android 15's enhanced encryption capabilities, including improved Jetpack Security libraries with `EncryptedSharedPreferences` and `EncryptedFile` implementations. All sensitive data, including the local database and exported files, receives hardware-backed encryption through Android 15's enhanced Keystore system.

**Data in transit** security implements TLS 1.3 with Android 15's improved network security protocols. Certificate pinning prevents man-in-the-middle attacks, while enhanced HTTPS enforcement ensures all network communications remain secure.

## 3. Comprehensive Security Practices

**Input validation** occurs at multiple levels, preventing common vulnerabilities such as SQL injection and cross-site scripting. Android 15's enhanced validation APIs provide additional protection layers.

**Code obfuscation** utilizes R8 with Android 15 optimizations, making reverse engineering significantly more difficult. **Tamper detection** mechanisms monitor for unauthorized modifications to application code and resources.

**Principle of least privilege** guides all permission requests, with contextual explanations leveraging Android 15's improved permission management system. **Secure logging** practices ensure no sensitive information appears in logs, with production builds completely disabling debug logging.

## 4. Enhanced Dependency Management

**Regular security audits** utilize automated tools including OWASP Dependency-Check and GitHub security alerts. **Automated updates** through Dependabot ensure dependencies remain current with the latest security patches.

**Third-party library vetting** includes comprehensive security assessments, maintenance history reviews, and community reputation analysis before integration.

## 5. Privacy Protection

**Data minimization** principles ensure only essential data collection for application functionality. **Privacy policies** provide clear, comprehensive explanations of data collection, usage, and protection practices, easily accessible within the application.

**User consent** mechanisms obtain explicit permission before collecting personal data or accessing device features, integrating with Android 15's enhanced privacy controls.

---

# Database Schema and Synchronization Architecture

## 1. Local Database Schema (Room with Android 15 Enhancements)

The local database leverages Room persistence library with Android 15's enhanced SQLite performance and security features. The schema efficiently stores SIRIM QR code information with metadata for synchronization and auditing.

### 1.1. Core Data Tables

**SIRIM Records Table** serves as the primary data repository:

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY`, `AUTOINCREMENT` | Unique record identifier with Android 15 optimization |
| `sirim_serial_no` | `TEXT` | `NOT NULL`, `UNIQUE` | SIRIM serial number (max 12 characters) for synchronization |
| `batch_no` | `TEXT` | | Batch number (max 200 characters) |
| `brand_trademark` | `TEXT` | | Brand/trademark (max 1024 characters) |
| `model` | `TEXT` | | Product model (max 1500 characters) |
| `type` | `TEXT` | | Product type (max 1500 characters) |
| `rating` | `TEXT` | | Product rating (max 600 characters) |
| `size` | `TEXT` | | Product size (max 1500 characters) |
| `created_at` | `INTEGER` | `NOT NULL` | Creation timestamp (milliseconds) |
| `updated_at` | `INTEGER` | `NOT NULL` | Last update timestamp (milliseconds) |
| `is_synced` | `INTEGER` | `NOT NULL`, `DEFAULT 0` | Synchronization status flag |
| `device_id` | `TEXT` | | Android 15 device identifier for multi-device sync |

**User Authentication Table**:

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY`, `AUTOINCREMENT` | Unique user identifier |
| `username` | `TEXT` | `NOT NULL`, `UNIQUE` | User's username |
| `password_hash` | `TEXT` | `NOT NULL` | Enhanced hash with Android 15 security |
| `biometric_enabled` | `INTEGER` | `DEFAULT 0` | Biometric authentication status |
| `last_login` | `INTEGER` | | Last login timestamp |

## 2. Optional Remote Database Schema

The remote database mirrors local schema structure for seamless synchronization, enhanced with cloud-specific optimizations and multi-user support.

### 2.1. Cloud Database Tables

**Remote SIRIM Records** with enhanced cloud capabilities:

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `SERIAL` | `PRIMARY KEY` | Unique cloud record identifier |
| `sirim_serial_no` | `VARCHAR(12)` | `NOT NULL`, `UNIQUE` | SIRIM serial number |
| `batch_no` | `VARCHAR(200)` | | Batch number |
| `brand_trademark` | `VARCHAR(1024)` | | Brand/trademark |
| `model` | `VARCHAR(1500)` | | Product model |
| `type` | `VARCHAR(1500)` | | Product type |
| `rating` | `VARCHAR(600)` | | Product rating |
| `size` | `VARCHAR(1500)` | | Product size |
| `created_at` | `TIMESTAMP` | `NOT NULL` | Creation timestamp |
| `updated_at` | `TIMESTAMP` | `NOT NULL` | Last update timestamp |
| `user_id` | `INTEGER` | `FOREIGN KEY` | Record owner |
| `device_id` | `VARCHAR(255)` | | Source device identifier |
| `sync_version` | `INTEGER` | `DEFAULT 1` | Version control for conflict resolution |

## 3. Enhanced Synchronization Architecture

The synchronization system utilizes Android 15's enhanced WorkManager with improved background processing and battery optimization capabilities.

**Synchronization triggers** support both manual user initiation and automatic background processing. Android 15's enhanced WorkManager ensures reliable execution even when the application is not active, with intelligent scheduling based on device conditions and user patterns.

**Data transfer protocols** use Retrofit with Android 15's enhanced network security features, transferring data in optimized JSON formats with compression and encryption. **Batch processing** capabilities handle large datasets efficiently while maintaining responsiveness.

**Conflict resolution** implements sophisticated algorithms using timestamp comparison and version control. The system maintains data integrity through **last-write-wins** strategies enhanced with user notification systems for significant conflicts.

**Synchronization monitoring** provides real-time status updates and error handling, with automatic retry mechanisms and exponential backoff strategies for network failures.

---

# Implementation Roadmap and Development Plan

## 1. Enhanced Development Methodology

The project follows an **Agile Scrum** methodology optimized for Android 15 development, with two-week sprints designed to leverage the platform's latest capabilities. Each sprint includes comprehensive testing with Android 15's enhanced testing frameworks and continuous integration practices.

## 2. Detailed Sprint Breakdown

### Sprint 1: Foundation and Android 15 Integration (Weeks 1-2)

**Primary objectives** include establishing the Android Studio project with Android 15 SDK configuration and implementing core application architecture using MVVM with Android 15 optimizations. The team will set up Room database with enhanced Android 15 features and implement basic authentication using the platform's improved security APIs.

**Key deliverables** include a functional Android 15 application with modern authentication screens, local database implementation with enhanced security, and core architecture foundation ready for feature development.

### Sprint 2: Advanced QR Code Scanning (Weeks 3-4)

**Development focus** centers on integrating Google ML Kit with Android 15 enhancements, implementing real-time camera preview using CameraX with Low Light Boost capabilities, and developing sophisticated data extraction logic for SIRIM QR codes.

**Sprint deliverables** include a high-performance QR code scanner leveraging Android 15's camera improvements, accurate SIRIM code recognition with enhanced OCR capabilities, and user-friendly data verification interfaces.

### Sprint 3: Comprehensive Data Management (Weeks 5-6)

**Implementation priorities** include developing robust data storage using Room with Android 15 optimizations, creating intuitive user interfaces for record management, and implementing advanced search and filtering capabilities using the platform's enhanced text processing features.

**Expected outcomes** include complete record management functionality, powerful search and filtering systems, and detailed record viewing capabilities with Android 15's improved UI components.

### Sprint 4: Multi-Format Export System (Weeks 7-8)

**Development activities** focus on integrating export libraries (Apache POI, iText) with Android 15 compatibility, implementing customizable export functionality, and developing seamless sharing capabilities using the platform's enhanced sharing system.

**Sprint results** include comprehensive export functionality supporting Excel, PDF, and ZIP formats, customizable field selection systems, and integrated sharing capabilities with Android 15's improved security.

### Sprint 5: Cloud Synchronization (Weeks 9-10)

**Technical implementation** includes backend server development with Android 15-compatible APIs, RESTful API creation for data synchronization, and mobile synchronization logic using enhanced WorkManager capabilities.

**Deliverable targets** include fully functional backend infrastructure, reliable data synchronization between mobile and cloud, and comprehensive conflict resolution systems.

### Sprint 6: Testing and Deployment (Weeks 11-12)

**Quality assurance activities** include comprehensive testing using Android 15's enhanced testing frameworks, performance optimization leveraging platform improvements, and Google Play Store preparation with Android 15 compliance.

**Final deliverables** include production-ready application with comprehensive testing coverage, optimized performance for Android 15, and complete deployment package for Google Play Store distribution.

## 3. Enhanced Timeline and Milestones

| Sprint Phase | Duration | Key Achievements | Android 15 Features Utilized |
| :--- | :--- | :--- | :--- |
| **Sprint 1** | 2 Weeks | Foundation, architecture, authentication | Enhanced security APIs, improved Keystore |
| **Sprint 2** | 2 Weeks | QR scanning, camera integration | Low Light Boost, enhanced camera controls |
| **Sprint 3** | 2 Weeks | Data management, UI development | Improved UI components, enhanced text processing |
| **Sprint 4** | 2 Weeks | Export functionality, sharing | Enhanced file handling, improved sharing system |
| **Sprint 5** | 2 Weeks | Cloud synchronization | Enhanced WorkManager, improved network security |
| **Sprint 6** | 2 Weeks | Testing, optimization, deployment | Enhanced testing frameworks, performance optimizations |
| **Total Duration** | **12 Weeks** | **Complete MVP Release** | **Full Android 15 Integration** |

---

## References

[1] [SIRIM QAS International - Implementation of New SIRIM-ST Labels with QR Code](https://www.sirim-qas.com.my/implementation-of-new-sirim-st-labels-with-qr-code/)

[2] [Google ML Kit - Scan barcodes with ML Kit on Android](https://developers.google.com/ml-kit/vision/barcode-scanning/android)

[3] [Android Developers - Features and APIs Overview (Android 15)](https://developer.android.com/about/versions/15/features)

[4] [Android Developers - Save data in a local database using Room](https://developer.android.com/training/data-storage/room)

[5] [Android Developers - Security checklist](https://developer.android.com/privacy-and-security/security-tips)

---

**Document Version:** 2.0  
**Last Updated:** September 29, 2025  
**Next Review:** October 15, 2025
