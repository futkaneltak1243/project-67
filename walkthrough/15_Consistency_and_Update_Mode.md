# 15 Consistency and Update Mode

This document explains how GhostBuilder maintains project consistency through automated checks and managed workflows for fixing issues or updating existing decisions.

## 1. Consistency Checks

To prevent contradictions and ensure the project remains in a valid state, GhostBuilder performs automated consistency checks at key moments.

-   **When Checks Run:**
    -   After applying a fix or update plan.
    -   After a new file is approved.
    -   When the user initiates a "Continue building" session, especially if manual file changes might have occurred.
-   **Blocking Mechanism:** If a consistency check fails, all forward progress is blocked. The user cannot continue building or push changes to GitHub until the issues are resolved.
-   **Reporting:** When a check fails, GhostBuilder reads the errors aloud to inform the user of the specific problems that need to be addressed.

## 2. The "Fix" Workflow

When a consistency check fails, the user must explicitly start the fix workflow.

1.  **Initiation:** The user says the command "fix" to begin the process.
2.  **Plan Proposal:** The AI analyzes the consistency errors and proposes a plan to resolve them. This plan is presented to the user for review.
3.  **Approval:** The user must approve the proposed fix plan. Like other significant actions, this requires a readback and a "Proceed" confirmation.
4.  **Execution and Re-check:** Once approved, GhostBuilder executes the fix plan and immediately runs another consistency check to verify that the issues have been resolved.

## 3. Update Mode

Update Mode is a specific workflow triggered when a user changes a previously approved decision. This could happen by editing a file, approving a patch that alters existing logic, or explicitly stating a decision change.

-   **Trigger:** Any change to an approved part of the project that has downstream implications.
-   **Update Plan:** GhostBuilder automatically enters Update Mode and proposes an "update plan." This plan identifies all the files across the walkthrough, brain, and code that need to be modified to remain consistent with the new decision.
-   **Approval:** The user must review and "Approve" the update plan before any changes are made.
-   **Execution:** After approval, the plan is executed, and consistency checks are run to ensure the project is coherent again.
-   **Auditing:** A summary of the update plan and its results is recorded in the local-only `logs/audit.md` file.
