# 07 Code Generation

This document outlines the final stage of the GhostBuilder pipeline: generating the application codebase under the `/app` directory.

## 1. Foundation

- Code generation begins only after the "brain complete" command has been successfully issued.
- The project's technology stack and architecture (e.g., Next.js, Laravel) are determined during the Walkthrough stage and finalized in the Brain stage. This decision guides the entire code generation process.

## 2. Authoring Process for Code

The process for generating code files is consistent with the authoring workflow used for walkthrough and brain files, with one key difference:

- **File-by-File Generation:** The AI proposes the creation of one code file at a time, including its path and filename.
- **Question-Driven:** The content of the file is built up section by section based on a series of questions and the user's answers.
- **Drafts with Commented Notes:** During the drafting process, the contextual Q/A notes are placed inside comment blocks appropriate for the programming language (e.g., `// Q: ...`, `/* A: ... */`). This ensures that the draft code can still be syntactically valid or runnable.
- **Approval:** The user must say "Approve" to finalize each code file. Upon approval, the Q/A comments are removed, and the file's status is updated to "Approved" in `project_state.json`.

## 3. Project Structure

The generated code resides in the `/app` directory and is organized to resemble a standard project for the chosen technology stack. This includes typical subdirectories like `src`, `components`, `server`, `client`, etc., as appropriate.

## 4. Root README File

As the codebase takes shape, the placeholder `README.md` file at the root of the project repository is updated. This file will contain the final installation and execution instructions for the application being built in the `/app` directory.

## 5. Code Completion

- The code generation stage is considered complete when all necessary application files have been generated and approved.
- The user finalizes this stage by issuing the "code complete" voice command.
- At this point, the project is considered fully built, and the `/app` directory should contain a runnable application.