# 18 Commands and Prompt Templates (Editable System)

This document describes GhostBuilder's system for managing voice commands and the underlying AI prompt templates, which are designed to be editable by the user.

## 1. Global and Editable Commands

-   **Global Scope:** Voice commands and their corresponding phrases are global, meaning they apply across all projects within the application.
-   **Defaults Provided:** GhostBuilder comes with a set of built-in default commands that cover all core functionality.
-   **User Editable:** Users have the ability to edit these commands and their associated prompt templates to better suit their workflow or preferences.

## 2. Editable Prompt Templates

-   Each command that interacts with the AI is backed by a prompt template.
-   These templates are editable through the app's UI and use a simple placeholder system (e.g., `{{file_content}}`) to inject context. The template system does not support complex logic like conditionals or loops.
-   The app layers these editable templates on top of a non-editable base set of instructions that enforce the core rules of interaction, such as the strict JSON contract and safety protocols.

## 3. Management and Safety Features

To ensure the system remains stable even with user modifications, GhostBuilder includes several management and safety features:

-   **Reset to Defaults:** A user can revert all commands and templates back to their original, factory-default state at any time.
-   **Test Command:** A "dry-run" feature allows users to test a modified command. This test validates that the AI's response, based on the new prompt, still conforms to the required JSON schema without actually executing any actions.
-   **Automatic Reversion:** If a user's edits to a command prompt lead to three consecutive invalid JSON responses from the AI, GhostBuilder will automatically revert that specific command's prompt back to its last working version (or the default) to prevent the command from becoming permanently broken.
