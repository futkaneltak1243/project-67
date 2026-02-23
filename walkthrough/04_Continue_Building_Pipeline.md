# 04 The "Continue Building" Pipeline

This document describes the core deterministic pipeline that a user follows after the initial project setup and seed verification.

## 1. Initiating a Session

- The user starts a building session by tapping the "Continue building" button from the project's main screen.
- This action launches a focused session screen and activates the hands-free listening service.
- GhostBuilder automatically determines the next logical step based on the project's current state, ensuring the user always resumes from where they left off.

## 2. The Deterministic Pipeline Stages

GhostBuilder enforces a strict, sequential pipeline to ensure a predictable and consistent workflow. A user cannot skip stages.

The primary stages are:
1.  **Seeds Added & Verified:** The foundational step where the project's rules are established.
2.  **Walkthrough Drafted:** The user, guided by the AI, creates a detailed, multi-file specification of the target application.
3.  **Brain Files Generated:** The system generates structured implementation plans that mirror the walkthrough.
4.  **Code Generated:** The final application codebase is generated based on the approved brain files.

## 3. Stage Completion

- A stage is considered complete only when all files belonging to that stage have the status "Approved."
- To formally complete a stage, the user must speak a specific voice command:
    - "walkthrough complete"
    - "brain complete"
    - "code complete"
- If the user attempts to issue a completion command before all files in that stage are approved, GhostBuilder will refuse and inform the user which files are still in a draft state.
- Upon successful completion of a stage, the `project_state.json` file is updated to reflect the new project status.