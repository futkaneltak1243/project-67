# 20 Example Session

This document provides a concrete, step-by-step example of a typical authoring session in GhostBuilder, using the default "Factory Mode."

The goal of this session is to create a small section within a walkthrough file in a controlled, voice-driven manner.

## The Scenario

The user has started a "Continue building" session. GhostBuilder has determined that the next logical step is to create a new file, `/walkthrough/01.01_Overview.md`, and begin drafting its content.

## The Interaction Flow

1.  **GhostBuilder Proposes the Action:**
    -   **Speech:** "I will create `/walkthrough/01.01_Overview.md` and draft the next section. Say ‘Proceed’ to continue."
    -   *This follows the rule that any file-changing action requires confirmation.*

2.  **User Confirms:**
    -   **Voice Command:** "Ghost proceed done"
    -   *The user gives the go-ahead. The `done` keyword is used because this is Factory Mode.*

3.  **GhostBuilder Asks a Guiding Question:**
    -   **Speech:** "Question: Should this overview describe the core goal of the app in one paragraph? Answer A) Yes B) No."
    -   *The authoring process begins with a simple, focused question to guide content generation.*

4.  **User Answers:**
    -   **Voice Command:** "Ghost A done"
    -   *The user selects option A.*

5.  **GhostBuilder Generates and Reads the Draft:**
    -   **Speech:** "Drafted section. I will read it now… [Reads the generated paragraph]. If you want to keep it, say ‘Approve’."
    -   *The AI generates the content based on the user's answer and reads it aloud for review. The draft file now contains the generated text along with the Q/A notes for traceability.*

6.  **User Approves the File:**
    -   **Voice Command:** "Ghost approve done"
    -   *The user is satisfied with the file's content and approves it.*

7.  **GhostBuilder Finalizes the File:**
    -   **Speech:** "Approved. Cleaning draft Q/A notes and saving the final file."
    -   *GhostBuilder removes the temporary Q/A notes, saves the clean content to `/walkthrough/01.01_Overview.md`, and updates its status to "Approved" in `project_state.json`.*
