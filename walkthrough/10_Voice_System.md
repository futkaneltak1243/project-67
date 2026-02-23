# 10 Voice System (Hands-Free, Screen-Off)

This document describes the voice-first interaction model of GhostBuilder, which is designed for hands-free operation.

## 1. Voice Modes

GhostBuilder offers two distinct voice interaction modes to accommodate different environments.

### a. Factory Mode (Default)

-   **Activation:** Always listening for the wake word "Ghost" while a building session is active.
-   **Command Structure:** Every command must be framed by the wake word and the word "done" (e.g., "Ghost approve done").
-   **Use Case:** Designed for noisy environments where clear, deliberate command boundaries are necessary to prevent misinterpretation.

### b. Quiet Mode

-   **Activation:** Still requires the "Ghost" wake word.
-   **Command Structure:** The "done" suffix is not required. The system detects the end of a command after a brief period of silence.
-   **Toggling:** Users can switch between modes using voice commands ("quiet on" or "quiet off").

## 2. Readback and Confirmations

To ensure safety and prevent unintended actions, GhostBuilder follows a strict confirmation protocol for any voice action that modifies files, project state, or interacts with GitHub.

1.  **Readback:** GhostBuilder first reads aloud a summary of the action it is about to perform.
2.  **User Confirmation:** The user must respond with "Proceed" for the action to be executed.

-   **"Proceed" vs. "Approve":**
    -   **Proceed:** Confirms the execution of an action (e.g., saving a file, pushing to GitHub).
    -   **Approve:** Finalizes the content of a draft file, marking it as complete.

Read-only actions, such as reading a file or checking status, do not require this "Proceed" confirmation and can be executed immediately.

## 3. Destructive Actions

Actions that are destructive, such as deleting a file, require a two-step voice confirmation. The user must say "Proceed" twice to confirm the action, providing an extra layer of safety.

## 4. File Reading Controls

When reading a file aloud, the user has several voice commands to control the playback:
-   By default, the app reads the top 40 lines.
-   `next`: Reads the next block of lines.
-   `previous`: Reads the previous block of lines.
-   `stop reading`: Immediately halts the readback.

## 5. File Selection by Voice

Users can open or select files using their voice.
-   If the user says a filename that has a unique match, the file is selected.
-   If there are multiple likely matches, GhostBuilder will list the top options (e.g., A, B, C), and the user can select one by saying its corresponding letter (e.g., "Ghost A done").