# Brain: 04 The "Continue Building" Pipeline

This document provides the technical implementation plan for the deterministic project pipeline, as described in `walkthrough/04_Continue_Building_Pipeline.md`.

## 1. Data Models (`project_state.json`)

The core of the pipeline is managed through the `project_state.json` file.

### PipelineStage (Enum)
- `SEEDS_UNVERIFIED`
- `SEEDS_VERIFIED`
- `WALKTHROUGH_IN_PROGRESS`
- `WALKTHROUGH_COMPLETE`
- `BRAIN_IN_PROGRESS`
- `BRAIN_COMPLETE`
- `CODE_IN_PROGRESS`
- `CODE_COMPLETE`

### ProjectState
- `pipeline_stage`: PipelineStage (The current stage of the project)
- `file_status`: Map<String, FileStatus> (Maps file path to its status: `DRAFT` or `APPROVED`)

## 2. Core Logic: The PipelineManager

A central `PipelineManager` class will be responsible for managing the project's state and progression through the pipeline.

### 2.1. State Machine

The `PipelineManager` will implement a state machine with the following transitions:

- `SEEDS_UNVERIFIED` -> `SEEDS_VERIFIED`: Triggered by successful seed verification.
- `SEEDS_VERIFIED` -> `WALKTHROUGH_IN_PROGRESS`: Triggered automatically when the user starts building after seeds are verified.
- `WALKTHROUGH_IN_PROGRESS` -> `WALKTHROUGH_COMPLETE`: Triggered by the "walkthrough complete" voice command, only if all `/walkthrough/**/*.md` files are `APPROVED`.
- `WALKTHROUGH_COMPLETE` -> `BRAIN_IN_PROGRESS`: Triggered automatically when the user starts building after the walkthrough is complete.
- `BRAIN_IN_PROGRESS` -> `BRAIN_COMPLETE`: Triggered by the "brain complete" voice command, only if all `/brain/**/*.md` files are `APPROVED`.
- `BRAIN_COMPLETE` -> `CODE_IN_PROGRESS`: Triggered automatically when the user starts building after the brain is complete.
- `CODE_IN_PROGRESS` -> `CODE_COMPLETE`: Triggered by the "code complete" voice command, only if all `/app/**/*.*` files are `APPROVED`.

### 2.2. Determining the Next Logical Step

When the user taps "Continue building", the `PipelineManager.getNextStep()` method will be called.

- **Logic:**
    1. Read the current `pipeline_stage` from `project_state.json`.
    2. Scan the corresponding directory (`/walkthrough`, `/brain`, `/app`) for any files with `DRAFT` status in `file_status`.
    3. **If a draft file exists:** The next step is to continue working on that file.
    4. **If no draft files exist:** The next step is to propose the creation of a new file for the current stage. The AI will be invoked to suggest the next file based on the project context.
    5. **If the stage is complete:** The `PipelineManager` will automatically transition to the `_IN_PROGRESS` state of the next stage.

## 3. Stage Completion Logic

### 3.1. Voice Command Handling

- The `VoiceCommandProcessor` will recognize the stage completion phrases ("walkthrough complete", etc.).
- It will delegate the completion check to the `PipelineManager`.

### 3.2. File Status Verification

- **`PipelineManager.verifyStageCompletion(stage)`:**
    1.  Identify the target directory based on the stage (e.g., `WALKTHROUGH` -> `/walkthrough`).
    2.  List all files within that directory recursively.
    3.  For each file, look up its status in `project_state.json`'s `file_status` map.
    4.  If any file is missing from the map or has a status of `DRAFT`, the verification fails.
    5.  **On Failure:** The function returns a list of unapproved files, which is then communicated to the user.
    6.  **On Success:** The function updates the `pipeline_stage` in `project_state.json` to the corresponding `_COMPLETE` state and adds a `TimelineEvent`.

## 4. UI Interactions

-   **"Continue building" Button:**
    -   **Action:** Triggers the `PipelineManager.getNextStep()` logic.
    -   **State:** The button is disabled if a blocking condition exists (e.g., seeds need re-verification).
-   **Session Screen:**
    -   The UI will be driven by the action determined by `PipelineManager`. It will either open an existing draft file in the editor or present the AI's proposal for a new file.
-   **Status Page:**
    -   This page will display the current `pipeline_stage` and a summary of file statuses (e.g., "Walkthrough: 5/8 files approved").
