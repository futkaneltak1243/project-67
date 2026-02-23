# 05 Walkthrough Authoring

This document details the process of authoring walkthrough files, which also applies to brain and code files.

## 1. Proposing the Next Step

When a user starts a "Continue building" session, GhostBuilder proposes the next action. This could be creating a new file or continuing to work on an existing draft file. The proposal includes the suggested file path and name.

## 2. Voice Confirmation

To begin the proposed action, the user must give a voice confirmation, typically by saying "Proceed." This follows the global safety rule that any action changing project files requires explicit user consent.

## 3. Question-Driven Authoring

The authoring process is interactive and driven by questions from the AI.
- The AI asks one simple question at a time, often with multiple-choice answers (A, B, C).
- The user provides an answer via voice command.
- Based on the answer, the AI generates a small section of content for the file.

## 4. Draft Content and Q/A Notes

- The generated content is added to a draft file.
- Crucially, the AI includes Q/A (Question/Answer) notes directly above the generated section. These notes provide context and traceability, showing exactly why a piece of content was written.
- GhostBuilder reads the generated content aloud for the user to review.

## 5. Iteration and Rejection

- If the user is not satisfied with the last generated section, they can say "Reject."
- This command discards the most recent addition, and the AI will re-ask the question or propose an alternative, allowing for iterative refinement.

## 6. File Approval

- Once the user is satisfied with the entire content of the file, they give the "Approve" voice command.
- Upon approval, GhostBuilder performs two actions:
    1. It removes all the temporary Q/A notes from the file, leaving only the clean, final content.
    2. It saves the file and updates its status from "Draft" to "Approved" in `project_state.json`.