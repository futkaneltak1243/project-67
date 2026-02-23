# Brain: 11 Optional Auto-Decision Rule

This document provides the technical implementation plan for the optional "Auto-Decision" feature, as described in `walkthrough/11_Optional_Auto_Decision_Rule.md`.

## 1. Data Models

### AutoDecisionRule (User-editable configuration)
- `rule_id`: String (A unique identifier, e.g., "USE_UUID_FOR_PRIMARY_KEY")
- `description`: String (Human-readable explanation)
- `conditions`: List<Condition> (A set of criteria that the project context must meet for the rule to apply)
- `decision_to_make`: String (The specific decision to be made, e.g., "Set data type to UUID")

### AutoDecisionLog (Stored within a `DraftSection`)
- `log_id`: Int (A sequential number, e.g., 1, 2, 3, for user reference)
- `rule_id_triggered`: String
- `decision_text`: String (The human-readable decision that was made, e.g., "Selected primary key as 'id'.")
- `is_rejected`: Boolean (Defaults to `false`)

## 2. Core Logic and Algorithms

### 2.1. Rule Engine
- **Trigger:** Before the AI is prompted to ask a new question during an authoring session.
- **Process:**
    1.  Load the user-editable checklist of `AutoDecisionRule`s from a configuration file (e.g., `/seed/auto_decision_rules.json`).
    2.  The system evaluates the current context (e.g., the file being written, the last question) against the `conditions` of each rule.
    3.  A list of applicable rules is passed to the AI as part of the context pack.

### 2.2. AI Prompting and Response
- **System Prompt Instructions:** The AI's system prompt will be modified. If the `auto_decision_rules_enabled` flag is true, it will be instructed that it can autonomously make decisions *only if* they are covered by the provided list of applicable rules. It must announce these decisions clearly.
- **Response Parsing:** The application will look for a specific key in the AI's JSON response, `auto_decisions`, which will contain a list of `AutoDecisionLog` objects.

### 2.3. Recording in Draft Files
- When the app receives a response containing `auto_decisions`, it will format them and prepend them to the Q/A notes for the current `DraftSection`.
- **Formatting:**
    ```markdown
    > **Auto-decisions:**
    > 1. [Decision text for log_id 1]
    > 2. [Decision text for log_id 2]
    >
    > **Q:** [Question Text]
    > **A:** [Answer Text]

    [Generated Content]
    ```
- These `AutoDecisionLog` objects are stored within the `DraftSection` model in the `.draft` file.

### 2.4. Selective Rejection Logic
- **Trigger:** User issues a command like "reject decisions 2 3".
- **Process:**
    1.  The `VoiceCommandProcessor` parses the numbers from the command string.
    2.  It loads the current `DraftSection` from the `.draft` file.
    3.  It finds the `AutoDecisionLog` objects with `log_id` matching the rejected numbers and sets their `is_rejected` flag to `true`.
    4.  The system then re-prompts the AI. The context pack will include the current `DraftSection` with the rejected flags. The system prompt will instruct the AI to re-evaluate and provide new proposals *only for the rejected decisions*.

## 3. AI Interaction Contract

The AI response JSON will be extended to include an optional field for auto-decisions.

```json
{
  "schema_version": "1.0",
  "response_id": "...",
  "speech": "...",
  "auto_decisions": [
    {
      "log_id": 1,
      "rule_id_triggered": "USE_UUID_FOR_PRIMARY_KEY",
      "decision_text": "Selected primary key as 'id' with type UUID."
    }
  ],
  "question": { ... },
  "patch": [ ... ]
}
```

## 4. User Configuration

- The auto-decision feature will be controlled by a global setting in the app.
- The rules themselves will be stored in a JSON file within the project's `/seed` directory, making them version-controlled and editable by the user on a per-project basis.
