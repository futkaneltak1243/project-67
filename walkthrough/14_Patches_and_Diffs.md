# 14 Patches and Diffs

This document describes how GhostBuilder handles and communicates file changes using a structured patch and diff system, which is essential for the voice-first approval workflow.

## 1. Structured Patch Operations

Instead of proposing raw text changes, the AI communicates all file modifications as a set of structured patch operations. This allows the application to understand the intent behind each change. The supported operations are:

-   `create`: Create a new file.
-   `modify`: Change the content of an existing file.
-   `delete`: Remove a file.
-   `rename` / `move`: Rename a file or move it to a new directory.

## 2. Voice Readback of Changes

Before any changes are applied, GhostBuilder reads them aloud to the user for approval. The application renders the structured patch operations into a human-friendly summary and a unified diff format.

### Readback Order

To ensure a logical and predictable review process, changes are always read back in a specific order:

1.  **Creations:** Announce any new files being created.
2.  **Modifications:** Detail changes to existing files.
3.  **Renames/Moves:** Announce any files or folders that are being renamed or moved.
4.  **Deletions:** Announce any files being deleted.

### Diff Readback

For each file being modified, GhostBuilder first provides a concise summary (e.g., "File `main.js` changed (5 lines)"), where the line count refers only to the lines being added or removed. It then reads the full, line-by-line diff for that file.

## 3. Managing Large Changes

To keep the voice approval process manageable, very large sets of changes are automatically split into smaller, more focused patches. This prevents overwhelming the user with a single, lengthy readback and allows for more granular review and approval.
