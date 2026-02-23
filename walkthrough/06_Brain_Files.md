# 06 Brain Files (Implementation Planning)

This document describes the structure and authoring process for Brain files, which serve as the implementation plan for the project.

## 1. Mirroring the Walkthrough

- The primary principle of the Brain stage is that it mirrors the Walkthrough. For each user flow and feature detailed in a walkthrough document, there is a corresponding brain document.
- This 1:1 relationship ensures that the implementation plan is directly tied to the approved specification.
- The file numbering and naming convention from the `/walkthrough` directory is replicated in the `/brain` directory to make this correspondence clear (e.g., `walkthrough/03.01_Core_Flow.md` corresponds to `brain/03.01_Core_Flow.md`).

## 2. Structure and Content

Brain files are structured Markdown documents that contain technical planning details, such as:
- Data structures and models
- API endpoints and contracts
- Business logic and algorithms
- UI component states and interactions
- Error handling and constraints

Unlike the walkthrough, the `/brain` directory does not typically have a root `00_INDEX.md` file, but sub-folders can have index files if needed.

## 3. Global vs. Flow-Specific Brains

To avoid repetition, Brain files are organized into two categories:
- **Global/Shared:** These documents define project-wide concerns like shared data models, authentication logic, or global API conventions.
- **Per-Flow:** These documents contain the implementation details specific to a single user journey, referencing the global brain files where applicable.

## 4. Authoring Process

The authoring process for brain files is identical to the one used for walkthrough files:
- It is a question-driven workflow guided by the AI.
- Drafts are created with Q/A notes for traceability.
- The user must explicitly "Approve" each file to finalize it, at which point the Q/A notes are removed.

## 5. Brain Completion

- The Brain stage is considered complete only when all brain files have been moved from a "Draft" to an "Approved" status.
- To finalize the stage, the user must issue the "brain complete" voice command.
- GhostBuilder will then update the `project_state.json` file and prepare for the final stage: Code Generation.