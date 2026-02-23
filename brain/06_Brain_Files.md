# Brain: 06 Brain Files (Implementation Planning)

This document provides the technical implementation plan for how the GhostBuilder system will manage the creation and structure of Brain files, as described in `walkthrough/06_Brain_Files.md`.

## 1. Core Logic and Algorithms

### 1.1. Walkthrough-to-Brain File Mirroring

- **Trigger:** When the `PipelineManager` determines the next step is to create a new brain file.
- **Process:**
    1.  The system will scan the `/walkthrough` and `/brain` directories.
    2.  It will build two lists of file paths (excluding `00_INDEX.md` files).
    3.  It will identify walkthrough files that do not have a corresponding brain file by comparing the lists after replacing the root directory name (e.g., `walkthrough/03.01.md` vs. `brain/03.01.md`).
    4.  The AI will be prompted with the list of missing brain files to propose the next one to be created, ensuring the 1:1 mapping is maintained.

### 1.2. AI Prompting Strategy for Brain Content

- To generate a brain file, the AI's context pack will be specifically curated.
- **Context Pack:**
    -   The corresponding `walkthrough` file will be included in its entirety.
    -   Any existing "global" or "shared" brain files (e.g., `brain/01_Project_Summary.md` containing core data models) will also be included.
    -   The `project_state.json` file.
- **System Prompt Instructions:** The AI's system prompt for this stage will instruct it to transform the narrative user journey from the walkthrough into a technical plan, focusing on elements like data models, API contracts, algorithms, and UI states. It will be instructed to reference global decisions where possible to avoid repetition.

### 1.3. Global vs. Flow-Specific File Identification

- The system will not use a hardcoded list to distinguish between global and flow-specific files.
- Instead, the distinction will be managed by the AI during the authoring proposal.
- The AI will be instructed to first propose foundational, project-wide brain files (like a summary, shared data models, etc.) before moving on to the flow-specific files that mirror the rest of the walkthrough. This is guided by the content of the walkthrough files themselves.

## 2. Authoring Process Integration

- The authoring process for brain files will reuse the exact same technical implementation as the walkthrough authoring, detailed in `brain/05_Walkthrough_Authoring.md`.
- This includes:
    -   The use of `.draft` files.
    -   The `DraftFile` and `DraftSection` data models for session persistence.
    -   The Q/A note generation in blockquotes.
    -   The "Reject" and "Approve" command logic.
- No new components are required to support brain file authoring.

## 3. Brain Completion Logic

- The logic for the "brain complete" command will be handled by the `PipelineManager.verifyStageCompletion('BRAIN')` method.
- **Process:**
    1.  The method will perform the mirroring check described in section 1.1 to determine the complete list of required brain files based on the existing walkthrough files.
    2.  It will then iterate through this required list and check the status of each file in `project_state.json`'s `file_status` map.
    3.  If all required brain files exist and have a status of `APPROVED`, the stage is marked as complete.
    4.  If any are missing or are in `DRAFT` state, the command is rejected, and the user is informed of the list of incomplete files.
