# 11 Optional Auto-Decision Rule

This document explains the "Auto-Decision" feature, an optional mode designed to accelerate the building process while maintaining user control and auditability.

## 1. Enabling the Feature

The Auto-Decision mode is an optional setting that the user can enable. When active, it permits the AI to make certain decisions autonomously during the authoring process.

## 2. Controlled Autonomy

The AI's ability to make decisions is not unrestricted. It can only make choices that align with a pre-defined, user-editable checklist. This ensures that the auto-decisions stay within the boundaries set by the user.

## 3. Transparent Process

Auto-decisions are never made silently. When the AI makes an autonomous decision, it follows a strict protocol:

1.  **Announce Decisions:** The AI explicitly states the decisions it has made as a numbered list in its voice response (e.g., "Auto-decisions: 1. Selected primary key as 'id'. 2. Chose data type 'string' for username.").
2.  **Record in Draft:** These same numbered decisions are written into the Q/A notes of the draft file, providing a permanent record of what was decided and why.
3.  **Ask Next Question:** After announcing the auto-decisions, the AI proceeds to ask the next logical question to continue the authoring flow.

## 4. Implicit Approval

When the user answers the subsequent question and later approves the draft file, that approval action also implicitly confirms the auto-decisions that were part of that drafting step. They are part of the approved record.

## 5. Selective Rejection

The user retains full control and can override any auto-decision. If the user disagrees with one or more of the AI's choices, they can issue a specific voice command to reject them by number.

-   **Example Command:** "reject decisions 2 3"
-   **Outcome:** The AI is required to reconsider and re-propose only the rejected decisions (in this case, #2 and #3), leaving the others intact.
