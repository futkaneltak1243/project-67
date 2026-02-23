# 17 User Interface (v1 Minimum)

This document outlines the minimal user interface (UI) components required for GhostBuilder. While the primary interaction model is voice-first for hands-free operation, a functional UI is necessary for setup, review, and manual management when the user can look at the screen.

## 1. Projects List

-   The main entry point of the application is a simple list displaying all the user's projects.
-   From this screen, the user can create a new project or select an existing one to open.

## 2. Project View

Once a project is opened, the user is presented with the main project view, which includes several key components:

-   **File Tree:** A navigable tree structure showing all the folders and files in the project workspace (`/seed`, `/walkthrough`, `/brain`, `/app`).
-   **Text Editor:** A built-in editor for viewing and manually editing files. It should support syntax highlighting for common file types like Markdown, JSON, and various programming languages.
-   **Manual File Operations:** The UI provides standard file management capabilities, including creating, renaming, and deleting files and folders. UI-based deletions require a single on-screen confirmation dialog.
-   **"Continue Building" Button:** The primary call to action that starts or resumes a hands-free, voice-driven building session.
-   **Status Page:** A dedicated view that displays the current status of the project, including the current pipeline stage and file statuses. This information is also accessible via the "read status" voice command.

## 3. Role of the UI

The on-screen tools are intended for tasks that are more efficiently handled visually, such as initial project setup, reviewing the complete structure of the project, or making precise manual edits to a file. The core, iterative authoring workflow is designed to be handled by the voice system, especially during shifts when the user's screen is off.

## 4. Exclusions for v1

-   **No Advanced Diff Viewer:** The initial version will not include a sophisticated visual diff viewer. File changes proposed by the AI are reviewed via voice readback of the diff.
