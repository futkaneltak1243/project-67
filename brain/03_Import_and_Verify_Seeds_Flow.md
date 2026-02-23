# Brain: 03 Import and Verify Seeds Flow

This document provides the technical implementation plan for the "Import and Verify Seeds" user flow, based on `walkthrough/03_Import_and_Verify_Seeds_Flow.md`.

## 1. Data Models

### SeedVerificationRule
- `id`: String (e.g., "PRESENCE_OF_PROJECT_DEFINITION")
- `description`: String (Human-readable explanation of the rule)
- `is_editable`: Boolean
- `parameters`: Map<String, Any> (e.g., `{"file_path": "seed/PROJECT_DEFINITION.md"}`)

### SeedVerificationResult
- `rule_id`: String
- `status`: Enum (PASS, FAIL)
- `error_code`: String (e.g., "FILE_NOT_FOUND", nullable)
- `message`: String (Details on failure, nullable)

## 2. Core Logic and Algorithms

### 2.1. Default Seed Provisioning

- **Trigger:** `ProjectRepository.createProject()` success.
- **Process:**
    1.  On new project creation, the application will copy a set of default seed files from its internal `assets` directory into the new project's `/seed` directory on the local file system.
    2.  This is a one-time file copy operation.

### 2.2. Seed Verification Engine

- **Trigger:** User taps the "Verify" button.
- **Process:**
    1.  Load the list of `SeedVerificationRule` objects. These will be stored in a configurable file within the app's assets, but can be overridden by a user-edited file in the project directory.
    2.  Iterate through each rule and execute its corresponding check against the project's `/seed` directory.
    3.  Collect a `SeedVerificationResult` for each rule.
    4.  Aggregate the results. If any result has a status of `FAIL`, the entire verification process fails.

- **Example Built-in Rules:**
    -   **File Presence:** Checks if a specific file exists (e.g., `seed/PROJECT_DEFINITION.md`).
    -   **File Not Empty:** Checks if a file has content.
    -   **Markdown Formatting:** A basic linter to check for valid Markdown structure (e.g., balanced headers).

### 2.3. Re-verification Logic

- **Mechanism:** The application will use file system observers or calculate checksums/hashes of the files in the `/seed` directory to detect changes.
- **State Tracking:**
    1.  After a successful verification, the app will store the hashes of all files in `/seed` within the local-only session state (not in `project_state.json`).
    2.  Before allowing the "Continue building" action, the app will re-calculate the hashes of the current seed files.
    3.  If any hash does not match the stored hash, the project's state is internally flagged as requiring re-verification.
- **Blocking:** The `PipelineManager` will check this internal flag. If re-verification is required, it will block the "Continue building" action and the UI will update to reflect the unverified state.

## 3. State Management (`project_state.json`)

- **On Verification Pass:**
    -   A new `TimelineEvent` is appended to the `timeline` array:
        ```json
        {
          "timestamp": "2023-10-27T10:00:00Z",
          "event_description": "Seeds verified: PASS"
        }
        ```
    -   The `pipeline_stage` is advanced if it was at the initial stage.

- **On Verification Fail:**
    -   A new `TimelineEvent` is appended:
        ```json
        {
          "timestamp": "2023-10-27T10:01:00Z",
          "event_description": "Seeds verified: FAIL",
          "details": {
            "error_codes": ["FILE_NOT_FOUND:seed/PROJECT_DEFINITION.md"]
          }
        }
        ```

## 4. UI Component States and Interactions

-   **Seed Status Indicator:**
    -   Displays the current state: "Unverified", "Verified", "Changes detected, re-verification needed".
-   **"Verify" Button:**
    -   **Enabled:** When the project is in a state that allows for seed verification.
    -   **Disabled:** During the verification process.
    -   **Loading State:** Shows a progress indicator while the verification engine is running.
-   **Verification Results Display:**
    -   On failure, the UI will display a list of the failed checks and their corresponding error messages to guide the user in fixing them.
