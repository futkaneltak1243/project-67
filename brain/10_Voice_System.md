# Brain: 10 Voice System (Hands-Free, Screen-Off)

This document provides the technical implementation plan for the voice interaction system, as described in `walkthrough/10_Voice_System.md`.

## 1. Data Models

### VoiceMode (Enum, stored in global app settings)
- `FACTORY`
- `QUIET`

### PendingAction
- `action_type`: String (e.g., "PUSH_TO_GITHUB", "CREATE_FILE")
- `payload`: Map<String, Any> (Details of the action, e.g., `{"file_path": "/path/to/file.md"}`)
- `confirmation_prompt`: String (The text to be read aloud to the user)
- `required_confirmations`: Int (Usually 1, but 2 for destructive actions)
- `confirmations_received`: Int

## 2. Core Logic and Services

### 2.1. VoiceInputService
- **Type:** Android Foreground Service.
- **Responsibility:** Manages the lifecycle of the speech recognizer, handles the wake word, and processes incoming voice commands. This service is essential for screen-off operation.
- **Wake Word Detection:**
    -   An on-device wake word engine (e.g., Porcupine) will be used to listen for "Ghost". This is more efficient and private than streaming all audio to the cloud.
    -   Upon detecting the wake word, the service will activate Android's `SpeechRecognizer` to capture the actual command.
- **Mode Handling:**
    -   **Factory Mode:** The service will instruct the `SpeechRecognizer` to listen until it hears the "done" suffix.
    -   **Quiet Mode:** The service will use the `SpeechRecognizer`'s end-of-speech detection to stop listening after a pause.

### 2.2. VoiceCommandProcessor
- **Responsibility:** Takes the transcribed text from the `VoiceInputService` and maps it to an application command.
- **Process:**
    1.  Normalize the input text (lowercase, trim whitespace).
    2.  Use a fuzzy string matching algorithm or a simple keyword-based router to identify the intended command (e.g., "approve", "reject", "push").
    3.  If a command requires confirmation (e.g., it's not read-only), it will create a `PendingAction` object and store it in a state manager.
    4.  It will then trigger the Text-to-Speech (TTS) engine to read the `confirmation_prompt`.
    5.  If the next command is "proceed", it will execute the pending action.

### 2.3. Confirmation Flow Logic
- A `ConfirmationManager` will manage the state of `PendingAction`.
- **Normal Actions:**
    1.  `VoiceCommandProcessor` creates a `PendingAction` with `required_confirmations: 1`.
    2.  If the next command is "proceed", `ConfirmationManager` increments `confirmations_received` to 1.
    3.  Since `1 == 1`, the action is executed and the `PendingAction` is cleared.
- **Destructive Actions (e.g., Delete File):**
    1.  `VoiceCommandProcessor` creates a `PendingAction` with `required_confirmations: 2`.
    2.  On the first "proceed", `confirmations_received` becomes 1. The manager re-issues the confirmation prompt.
    3.  On the second "proceed", `confirmations_received` becomes 2. The action is executed and the `PendingAction` is cleared.
    4.  If any other command is received, the `PendingAction` is cleared, cancelling the flow.

## 3. Android Platform Integration

### 3.1. Text-to-Speech (TTS)
-   The application will use Android's built-in `TextToSpeech` engine for all readback.
-   A `TTSManager` class will be created to manage the TTS queue, handle initialization, and provide simple methods like `speak(text)`.

### 3.2. Speech Recognition
-   Android's `SpeechRecognizer` class will be used for transcribing commands after the wake word is detected.
-   The `VoiceInputService` will implement the `RecognitionListener` interface to receive transcription results.

## 4. File Selection Logic

- **Trigger:** A command that requires a file path as an argument (e.g., "open file ...").
- **Process:**
    1.  Extract the potential filename from the transcribed text.
    2.  Perform a search over the project's file tree.
    3.  Use a string similarity algorithm (e.g., Levenshtein distance) to find the best matches.
    4.  **If one clear match is found (score > 0.9):** Select it.
    5.  **If multiple good matches are found (score > 0.7):** Create a temporary `PendingAction` where the user must choose. The `confirmation_prompt` will be "Which file do you mean? A) ..., B) ..., C) ...". The user's response ("A", "B", etc.) will resolve the selection.
