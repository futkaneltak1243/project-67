# 08 GitHub Sync

This document describes how project changes are synced to the project's private GitHub repository.

## 1. Manual, Batched Pushes

- Unlike the initial commit during project creation, subsequent synchronization with GitHub is a manual process initiated by the user.
- All changes made since the last push are batched into a single commit.

## 2. The "Push" Voice Command

- The user starts the process with the "push" voice command.
- This action, like others that modify the project's state or repository, is protected by the global safety rule requiring readback and confirmation.

## 3. Push Confirmation Flow

1.  **Summary of Changes:** GhostBuilder first provides a brief summary of the changes to be pushed (e.g., "3 files added, 2 modified").
2.  **Commit Message Proposal:** The AI suggests a commit message based on the changes.
3.  **Message Approval/Editing:** The user can either approve the suggested message directly or edit it using a command like "Set message..."
4.  **Final Confirmation:** After the commit message is finalized, the user must give a final "Proceed" command to execute the push to GitHub.

## 4. What is Pushed

The following project directories and files are pushed to the GitHub repository:
- `/seed`
- `/walkthrough`
- `/brain`
- `/app`
- `project_state.json`

Local-only, sensitive, or transient data like transcripts and logs are explicitly excluded via a `.gitignore` file.

## 5. Draft Files

- Draft files (e.g., `File.draft.md`) are only pushed to the repository if the user initiates a push while they exist.
- Once a file is approved, the corresponding `.draft` file is automatically deleted, and the clean, approved version is committed in the next push.

## 6. Restore from GitHub

- GhostBuilder provides a mechanism to restore a project from its GitHub repository.
- This allows a user to recover their work on a new device by signing into the same GitHub account.
- The restore process clones the repository, bringing back the entire project workspace. Local-only files like transcripts are not restored.