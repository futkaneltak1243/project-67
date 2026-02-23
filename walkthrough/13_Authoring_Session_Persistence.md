# 13 Authoring Session Persistence

This document describes how GhostBuilder ensures that a user's authoring progress is not lost, even if the application restarts unexpectedly.

## 1. The Problem

Authoring a file, whether it's a walkthrough, brain, or code file, is an interactive, multi-step process. A session can be interrupted for various reasons, such as the Android operating system closing the app to reclaim memory, the device rebooting, or the app crashing.

## 2. The Solution: Session State File

To solve this, GhostBuilder maintains a local-only session state file for the active project. This file is not part of the project's core data and is never pushed to GitHub.

### Key Characteristics:

-   **Local-Only:** The session state is stored on the device and is included in the project's `.gitignore` file to prevent it from being committed to the repository.
-   **Resilience:** It saves the complete context of the current authoring task, including the file being edited, the last question asked by the AI, and any content that was in the process of being drafted.

## 3. Resuming a Session

When the user taps "Continue building" to start a new session, GhostBuilder first checks for the existence of a session state file.

-   **If a session file exists:** The application seamlessly restores the authoring process to its exact previous state. It will re-present the last question or draft, allowing the user to pick up precisely where they left off without losing any work.
-   **If no session file exists:** The application proceeds with the next logical step in the project pipeline, such as proposing a new file to create.

This persistence mechanism is crucial for the hands-free, long-session nature of GhostBuilder, providing a reliable and frustration-free user experience.
