# Brain: 09 Data Storage

This document provides the technical implementation plan for the data storage strategy, as described in `walkthrough/09_Data_Storage.md`.

## 1. File System Structure and Location

-   **Root Directory:** All GhostBuilder projects will be stored within the application's private internal storage directory, accessible via `Context.getFilesDir()` in Android. This ensures that other apps cannot access the project data.
-   **Project Structure:** Each project will have its own subdirectory named after a unique project ID (UUID) to prevent naming conflicts.
    ```
    /data/data/com.ghostbuilder.app/files/
    └── projects/
        └── {project_uuid_1}/
            ├── .gitignore
            ├── project_state.json
            ├── README.md
            ├── app/
            ├── brain/
            ├── seed/
            ├── walkthrough/
            ├── logs/ (local-only)
            │   ├── audit.md
            │   └── errors.md
            └── transcripts/ (local-only)
                └── ...
        └── {project_uuid_2}/
            └── ...
    ```

## 2. Data Models

### 2.1. `project_state.json`
-   **Format:** JSON.
-   **Library:** Gson or Moshi for serialization and deserialization.
-   **Content:** As defined in previous brain files, this file is the single source of truth for the project's synced state, including pipeline stage and file statuses.

### 2.2. Local Session State
-   **Purpose:** To store temporary, non-synced state required for application operation, such as the state of an in-progress authoring session or the last known GitHub commit SHA.
-   **File Naming:** `{project_uuid}.session.json` (or similar), stored in a shared location outside any individual project's synced directory.
-   **Format:** JSON.
-   **Example Content:**
    ```json
    {
      "session_version": "1.0",
      "last_known_commit_sha": "a1b2c3d4...",
      "active_authoring_file": "/walkthrough/03.01.md.draft"
    }
    ```

## 3. Core Logic and Implementation

### 3.1. FileSystemManager
-   A dedicated `FileSystemManager` class will abstract all file I/O operations.
-   It will have methods like `getProjectRoot(projectId)`, `readFile(projectId, path)`, `writeFile(projectId, path, content)`.
-   This class will be responsible for constructing the correct absolute file paths based on the project ID and the relative file path provided.

### 3.2. Privacy: Audio Handling
-   **Mechanism:** The `VoiceInputService` will use Android's `SpeechRecognizer` which provides transcribed text via callbacks.
-   The audio data stream from the microphone will be processed in memory by the recognizer.
-   The application code will **never** write the raw audio byte stream to a file on disk.
-   Once the `SpeechRecognizer` returns a result (or an error), the in-memory audio buffer is automatically discarded by the Android system. This enforces the "no audio storage" rule.

### 3.3. Data Synchronization
-   The `GitHubService` will be responsible for reading files from the project workspace for pushing.
-   It will explicitly **exclude** the `/logs` and `/transcripts` directories from the list of files to be included in the Git tree, as enforced by the `.gitignore` file and the service's own logic.
