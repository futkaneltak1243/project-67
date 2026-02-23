# Brain: 07 Code Generation

This document provides the technical implementation plan for the code generation stage, as described in `walkthrough/07_Code_Generation.md`.

## 1. Core Logic and Algorithms

### 1.1. Technology Stack and Architecture

- The chosen technology stack and architecture (e.g., Next.js, Android/Kotlin, etc.) are read from `project_state.json` where they were stored upon approval during the walkthrough/brain stage.
- This decision is a critical input for the AI's system prompt during code generation.

### 1.2. AI Prompting Strategy for Code Generation

- **Trigger:** When the `PipelineManager` determines the next step is to create a new code file.
- **Context Pack:**
    -   The most relevant `brain` file(s) that describe the component or logic to be coded.
    -   Any global `brain` files (e.g., `01_Project_Summary.md` which might contain shared data models).
    -   The file tree of the `/app` directory to provide context of the existing code structure.
    -   Optionally, the content of a few key existing code files if they are directly related (e.g., a model class that a new service will use).
- **System Prompt Instructions:** The AI's system prompt will instruct it to generate a single code file based on the provided brain specifications. It will be told to adhere to the project's chosen stack and architecture, follow best practices for that stack, and ensure the code is clean, commented where necessary, and fits into the existing `/app` structure.

### 1.3. Draft Q/A Notes in Code Comments

- The system will maintain a mapping of file extensions to comment syntax.
- **Mapping Example:**
    -   `.js`, `.ts`, `.java`, `.kt`, `.swift`: `/* Q: ... \n A: ... */`
    -   `.py`, `.rb`, `.sh`: `# Q: ...\n# A: ...`
    -   `.html`, `.xml`: `<!-- Q: ... \n A: ... -->`
- The `DraftFile` rendering logic (from `brain/05_Walkthrough_Authoring.md`) will use this map to correctly format the Q/A notes when creating the display version of a draft code file.

### 1.4. Root `README.md` Update Logic

- The update of the root `README.md` will be treated as a standard file authoring task within the Code Generation stage.
- The AI will be prompted to generate the content for the README, including installation and usage instructions, based on the complete set of brain files and the generated application code.

## 2. Authoring Process Integration

- The code generation process will reuse the exact same technical implementation as the walkthrough and brain authoring, detailed in `brain/05_Walkthrough_Authoring.md`.
- This includes:
    -   The use of `.draft` files (e.g., `MyComponent.js.draft`).
    -   The `DraftFile` and `DraftSection` data models.
    -   The "Reject" and "Approve" command logic.
- The only difference is the application of language-specific comment syntax for the Q/A notes.

## 3. Code Completion Logic

- **Trigger:** User issues the "code complete" voice command.
- **Verification (`PipelineManager.verifyStageCompletion('CODE')`):**
    1.  Unlike previous stages, there is no direct file-to-file mapping to verify against.
    2.  The primary check is to ensure that all files within the `/app` directory (and the root `README.md`) have a status of `APPROVED` in `project_state.json`'s `file_status` map.
    3.  The system does not programmatically know if the code is "complete" in a functional sense. The user's command is the final authority.
    4.  If any files are found in a `DRAFT` state, the command is rejected, and the user is informed of the list of unapproved files.
    5.  On success, the `pipeline_stage` is updated to `CODE_COMPLETE`.
