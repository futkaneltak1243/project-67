# 19 Non-Functional Requirements

This document details the key non-functional requirements (NFRs) for GhostBuilder, covering privacy, security, reliability, and performance.

## 1. Privacy

-   **No Audio Storage:** Raw audio captured for voice commands is transcribed in memory and deleted immediately. No audio recordings are ever saved to the device.
-   **Local-Only Transcripts and Logs:** All text transcripts of voice commands and application logs (`audit.md`, `errors.md`) are stored exclusively on the user's device. They are explicitly excluded from GitHub synchronization via the `.gitignore` file.
-   **Stateless Backend:** The backend service that processes AI requests does not store any project content, prompts, or AI responses. It only retains minimal operational logs (e.g., error codes) for debugging.

## 2. Security

-   **Backend API Key Management:** The API key for the AI service (e.g., OpenAI) is stored securely on the backend. It is never exposed to or stored on the Android client application.
-   **GitHub-Based Authentication:** The user's identity is managed through their GitHub account. Access to the backend is restricted to the signed-in owner of the project's repository.

## 3. Reliability

-   **Foreground Service for Voice:** The voice listening service runs as a persistent foreground service on Android. This is accompanied by a persistent notification, which reduces the likelihood of the operating system terminating the service while a session is active, enabling screen-off operation.
-   **Strict Command Phrasing:** The use of a wake word ("Ghost") and, in Factory Mode, a concluding word ("done") minimizes the risk of accidental command triggers.
-   **Consistency Gates:** The application enforces consistency checks that block further progress or GitHub pushes if contradictions are found, preventing the project from entering a broken state.
-   **Session Persistence:** In-progress authoring sessions are saved locally, ensuring that work is not lost if the app is closed or restarts unexpectedly.

## 4. Performance and Cost

-   **Focused Context:** To optimize performance and reduce AI service costs, the application sends a minimal, focused context pack with each request rather than the entire conversation history.
-   **Budget Tracking:** The application includes a soft budget tracking feature. It provides a warning to the user once per day if they exceed a pre-defined budget, which is based on a calendar month and resets on the first of the month.
