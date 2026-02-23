# 09 Data Storage

This document outlines what data GhostBuilder stores for each project and where it is stored.

## 1. Project Workspace (Synced to GitHub)

The core project files are stored in a dedicated project folder and are synchronized with the project's private GitHub repository. This includes:

-   `/seed`: Foundational documents and protocols for the AI.
-   `/walkthrough`: The detailed, multi-file specification for the target application.
-   `/brain`: The structured implementation plans that mirror the walkthrough.
-   `/app`: The generated source code for the target application.
-   `/project_state.json`: A machine-readable file that tracks the project's overall state, including pipeline progress, file statuses (Draft vs. Approved), and a timeline of high-level events.

## 2. Local-Only Data (Not Synced)

To protect user privacy and avoid cluttering the project repository, certain data is stored exclusively on the user's device and is never pushed to GitHub. This is managed via a `.gitignore` file.

-   `/transcripts`: Text transcripts of voice commands for the project.
-   `/logs`:
    -   `audit.md`: Summaries of actions taken within the project.
    -   `errors.md`: A log of errors encountered.
-   **Audio Recordings:** Raw audio is processed in memory for transcription and is deleted immediately. No audio files are ever saved to disk.
-   **Session State:** A temporary file to manage the state of an in-progress authoring session, ensuring work can be resumed after an app restart.
