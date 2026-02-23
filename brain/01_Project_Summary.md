# Brain: 01 Project Summary

This document outlines the high-level technical architecture and foundational components for the GhostBuilder application, based on the principles described in `walkthrough/01_Project_Summary.md`.

## 1. High-Level Architecture

The application will follow a layered architecture to separate concerns and ensure maintainability.

-   **UI Layer:** Responsible for all user interactions, including the main screen, project list, file tree, editor, and session screen. It will be built using modern Android UI components (Jetpack Compose).
-   **ViewModel Layer:** Acts as a bridge between the UI and the business logic. It will hold and manage UI-related data and expose it to the UI, while delegating business logic to the domain layer.
-   **Domain Layer (Business Logic):** Contains the core application logic, including the pipeline manager, state machine, authoring workflows, and consistency checkers. This layer is independent of any specific UI or data storage implementation.
-   **Data Layer:** Manages all data persistence and retrieval. This includes the local file system for project files and secure storage for credentials. It also includes repositories for interacting with external APIs like GitHub.
-   **Service Layer:** Handles background operations, primarily the hands-free voice listening service which runs independently of the UI.

## 2. Core Data Models

These are the primary data entities that will be used throughout the application.

### Project
-   `id`: UUID
-   `name`: String (e.g., "My Awesome Project")
-   `local_path`: String (Path to the project directory on the device)
-   `github_repo_url`: String (URL of the associated GitHub repository)
-   `project_state`: ProjectState (A complex object representing the `project_state.json` file)

### ProjectState
-   `state_version`: String
-   `pipeline_stage`: Enum (e.g., SEEDS, WALKTHROUGH, BRAIN, CODE)
-   `file_status`: Map<String, FileStatus> (e.g., `{"path/to/file.md": "Approved"}`)
-   `timeline`: List<TimelineEvent>

### FileStatus
-   Enum: `DRAFT`, `APPROVED`

### TimelineEvent
-   `timestamp`: Timestamp
-   `event_description`: String (e.g., "Seeds verified: PASS")

## 3. Key Services and Components

### VoiceInputService
-   **Responsibility:** Runs as a foreground service to listen for the wake word ("Ghost") even when the screen is off.
-   **Technology:** Will use Android's `SpeechRecognizer` or a third-party library for more robust wake-word detection.
-   **Features:** Manages "Factory Mode" vs. "Quiet Mode" logic. Deletes audio immediately after transcription.

### GitHubService
-   **Responsibility:** Handles all interactions with the GitHub API.
-   **Operations:** Create repository, commit and push files, check for repository existence.
-   **Authentication:** Uses OAuth2. The token will be stored securely using `EncryptedSharedPreferences`.
-   **Library:** OkHttp/Retrofit for API calls.

### PipelineManager
-   **Responsibility:** Enforces the deterministic `Seeds → Walkthrough → Brain → Code` pipeline.
-   **Logic:** Checks project state to determine the next valid action. Blocks progression if stage requirements are not met.

### FileSystemManager
-   **Responsibility:** Manages all file and directory operations within a project's workspace.
-   **Operations:** Create, read, update, delete files and folders. Handles draft files (`.draft.md`).

## 4. Android Permissions

The `AndroidManifest.xml` will need to declare the following permissions:

-   `android.permission.INTERNET`: For GitHub API access and AI backend communication.
-   `android.permission.RECORD_AUDIO`: For capturing voice commands.
-   `android.permission.FOREGROUND_SERVICE`: To run the `VoiceInputService` persistently.

## 5. Key Dependencies

-   **Jetpack Compose:** For building the user interface.
-   **Kotlin Coroutines & Flow:** For managing asynchronous operations.
-   **Retrofit/OkHttp:** For networking with GitHub and the AI backend.
-   **Gson/Moshi:** For JSON serialization/deserialization (`project_state.json`, API responses).
-   **AndroidX Security (EncryptedSharedPreferences):** For secure storage of the GitHub OAuth token.
-   **Porcupine/Picovoice (or similar):** Potential candidate for on-device wake word detection to enhance privacy and reliability. (Decision to be finalized).
