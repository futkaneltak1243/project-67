# Brain: 05 Walkthrough Authoring

This document provides the technical implementation plan for the file authoring workflow, as described in `walkthrough/05_Walkthrough_Authoring.md`.

## 1. Data Models

### DraftSection
- `question_id`: UUID (A unique identifier for the question that generated this section)
- `question_text`: String
- `answer_text`: String
- `generated_content`: String (The actual content block for the file)

### DraftFile
- `file_path`: String (Path to the target file, e.g., `/walkthrough/01.md`)
- `draft_path`: String (Path to the temporary draft file, e.g., `/walkthrough/01.md.draft`)
- `sections`: List<DraftSection> (An ordered list of all sections currently in the draft)

## 2. Core Logic and Algorithms

### 2.1. Draft File Management

- **Creation:** When authoring a new file or editing an existing one, the system will work with a temporary draft file. For `file.md`, the draft will be `file.md.draft`.
- **Serialization:** The `DraftFile` object, including its list of `DraftSection` objects, will be serialized (e.g., to JSON or a custom format) and saved to the `.draft` file. This preserves the Q/A context between sessions.
- **Rendering:** The `.draft` file is not the file shown to the user in the editor. The editor will display a rendered version that combines the `generated_content` from all sections, interspersed with formatted Q/A notes.

### 2.2. Q/A Note Formatting

- When rendering the draft content for display or readback, the Q/A notes will be formatted to be easily distinguishable.
- For Markdown files, a blockquote format will be used:
    ```markdown
    > **Q:** [Question Text]
    > **A:** [Answer Text]

    [Generated Content]
    ```
- For code files, the notes will be placed inside comment blocks:
    ```javascript
    /*
     * Q: [Question Text]
     * A: [Answer Text]
     */
    [Generated Code]
    ```

### 2.3. "Reject" Command Logic

- **Trigger:** User issues the "Reject" voice command.
- **Process:**
    1.  Load the `DraftFile` object from the `.draft` file.
    2.  Remove the last `DraftSection` object from the `sections` list.
    3.  Re-serialize and save the updated `DraftFile` object back to the `.draft` file.
    4.  The system then re-initiates the AI prompt that led to the rejected section.

### 2.4. "Approve" Command Logic

- **Trigger:** User issues the "Approve" voice command for the current file.
- **Process:**
    1.  Load the `DraftFile` object from the `.draft` file.
    2.  Concatenate the `generated_content` from every `DraftSection` in order.
    3.  Write the resulting clean content to the main file path (e.g., `file.md`).
    4.  Delete the temporary `.draft` file.
    5.  Update the `file_status` map in `project_state.json`, setting the status of `file.md` to `APPROVED`.
    6.  Add a `TimelineEvent` to `project_state.json` to record the approval.

## 3. AI Interaction Contract

The AI response for a question-driven authoring step must conform to a strict JSON structure.

- **AI Request from App:** The app sends the current project context, including the last `DraftSection` if available.
- **AI Response to App:**
    ```json
    {
      "schema_version": "1.0",
      "response_id": "...",
      "speech": "...",
      "question": {
        "question_id": "...",
        "question_text": "Should this section do X?",
        "choices": ["A) Yes", "B) No"]
      },
      "patch": [
        {
          "type": "update_file",
          "path": "/path/to/file.md.draft",
          "edits": [...] // The new DraftSection is appended here
        }
      ]
    }
    ```
- The app is responsible for parsing this response, updating the `DraftFile` model, and saving the `.draft` file.

## 4. Session Persistence

- The state of the current authoring session (which file is being worked on, its draft content) is persisted in the `.draft` file itself.
- If the app restarts, it can check for the presence of `.draft` files to determine if there's an unfinished authoring session and resume it automatically.