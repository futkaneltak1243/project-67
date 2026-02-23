# Brain: 08 GitHub Sync

This document provides the technical implementation plan for the GitHub synchronization features, as described in `walkthrough/08_GitHub_Sync.md`.

## 1. Data Models

### LocalSessionState (Not synced to GitHub)
- `project_id`: UUID
- `last_known_commit_sha`: String (The SHA of the head of the `main` branch from the last successful push)

## 2. Core Logic and Algorithms

### 2.1. Change Detection

- **Trigger:** User issues the "push" voice command.
- **Process:**
    1.  Fetch the latest commit SHA from the remote `main` branch using the GitHub API (`GET /repos/{owner}/{repo}/branches/main`).
    2.  Compare this remote SHA with the `last_known_commit_sha` stored in the local session state.
    3.  For a more detailed summary, the system will need to compare the local file system state with the Git tree of the `last_known_commit_sha`.
    4.  **Simplified v1 Approach:** Instead of a full diff, the system can list local files that have been modified since the timestamp of the last push. It will scan the project directory and generate a list of added, modified, and deleted files.
    5.  This list of changes is used to generate the summary for the user (e.g., "3 files added, 2 modified").

### 2.2. AI-Powered Commit Message Generation

- **Context Pack:**
    -   A summary of changes (list of added/modified/deleted file paths).
    -   The content of `project_state.json`'s `timeline` to see recent events.
- **System Prompt Instructions:** The AI will be instructed to generate a concise, conventional commit message based on the provided summary and timeline events.

### 2.3. Push Execution Flow

This process mirrors the initial commit logic but uses a parent commit.

1.  **Get Parent Commit:** Fetch the current SHA of the `main` branch's head. This will be the parent of our new commit.
2.  **Create Blobs:** For each new or modified file, make a `POST /repos/{owner}/{repo}/git/blobs` call with the file content to get a blob SHA.
3.  **Create Tree:** Construct a tree object that represents the desired state of the repository. This includes all existing files (with their existing SHAs) and the new/modified files (with their new blob SHAs). Files to be deleted are omitted from the new tree. Make a `POST /repos/{owner}/{repo}/git/trees` call with this tree definition.
4.  **Create Commit:** Make a `POST /repos/{owner}/{repo}/git/commits` call, providing the new tree SHA, the parent commit SHA, and the user-approved commit message.
5.  **Update Reference:** Make a `PATCH /repos/{owner}/{repo}/git/refs/heads/main` call to point the `main` branch to the new commit SHA.
6.  **On Success:** Update the `last_known_commit_sha` in the local session state with the new commit's SHA.

### 2.4. Restore from GitHub Logic

- **Trigger:** User selects "Restore from GitHub" in the UI.
- **Process:**
    1.  **List Repositories:** Call `GET /user/repos` to fetch a list of the user's repositories.
    2.  **Filter:** Filter the list to find repositories that contain a `project_state.json` file, indicating they are GhostBuilder projects.
    3.  **User Selection:** Display the filtered list to the user.
    4.  **Download:** Upon selection, use the `GET /repos/{owner}/{repo}/zipball/{ref}` API endpoint to download a zip archive of the repository's `main` branch.
    5.  **Extraction:** Unzip the archive into the application's local project directory.
    6.  The project is now restored. Local-only files (`/logs`, `/transcripts`) will be created fresh.

## 3. `.gitignore` Content

The `.gitignore` file created at the start of the project will contain:

```
# GhostBuilder local-only files
/logs/
/transcripts/

# Draft files (optional, but good practice)
*.draft

# Local session state
*.session.json
```

## 4. UI Interactions

-   **Push Confirmation Dialog:**
    -   Displays the summary of changes.
    -   Displays the AI-suggested commit message in an editable text field.
    -   Provides "Approve Message" and "Proceed with Push" buttons, which are enabled/disabled sequentially to enforce the flow.
-   **Restore Project List:**
    -   A simple list view showing the names of the user's GhostBuilder repositories available for restoration.
