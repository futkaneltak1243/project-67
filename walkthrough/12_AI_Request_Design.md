# 12 AI Request Design (Predictable and Parsable)

This document describes the communication protocol between the GhostBuilder application and the AI backend, ensuring predictable and machine-parsable interactions.

## 1. Strict JSON Contract

All communication from the AI to the GhostBuilder app is encapsulated in a single, strictly-defined JSON object. The AI never returns free-form text; all responses must adhere to the specified schema. This ensures that the application can reliably parse and act on the AI's output.

### Core Response Envelope

Every AI response includes:
-   `schema_version`: The version of the communication protocol.
-   `response_id`: A unique identifier for the response.
-   `speech`: The exact text the application should read aloud to the user.
-   Optional fields for `question`, `commands`, `patch`, and `requires` to guide the workflow.

## 2. Stateless Backend with Focused Context

The AI backend operates on a stateless, per-request basis. It does not retain a long-term memory of the conversation history. Instead, the GhostBuilder application sends a focused "context pack" with each request.

This context pack typically includes:
-   The current `project_state.json`.
-   Any specific files relevant to the current task.

If the AI requires more information to proceed, it can use a specific JSON command (`REQUEST_FILES`) to ask the application for additional files. This approach minimizes the amount of data sent with each request and ensures that decisions are based on the approved, canonical state of the project files rather than a transient chat history.

## 3. Handling Invalid Responses

If the AI returns a response that is malformed, cannot be parsed, or does not conform to the JSON schema, GhostBuilder has a built-in recovery mechanism:

1.  It reads a short, user-friendly error message aloud.
2.  It automatically retries the request one time.
3.  If the second attempt also fails, the process is halted, and the application waits for further user instruction, preventing a loop of failures.
