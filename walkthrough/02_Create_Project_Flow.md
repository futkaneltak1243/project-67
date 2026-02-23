# 02 Create Project Flow

This document outlines the user journey for creating a new project within GhostBuilder.

## 1. Initiation

The user initiates the project creation process from the main screen of the application. This is a UI-driven action, not a voice command.

## 2. Project Naming

- The user provides a name for the project in a text input field.
- GhostBuilder uses this name to generate a sanitized, URL-friendly slug for the GitHub repository (e.g., "My Awesome Project" becomes "my-awesome-project").

## 3. GitHub Repository Creation

- GhostBuilder creates a new private GitHub repository under the user's linked GitHub account.
- If a repository with the sanitized name already exists, the app will suggest an alternative (e.g., "my-awesome-project-2") and ask for confirmation via a UI dialog before proceeding.
- The creation of the GitHub repository is a mandatory step. If it fails, the project creation process is halted, and the user is notified of the error.

## 4. Initial Project Structure

Once the repository is created, GhostBuilder performs an initial commit that includes:
- The required empty folder structure: `/seed`, `/walkthrough`, `/brain`, `/app`.
- A placeholder `README.md` file.
- The initial `project_state.json` file.

## 5. First-Time Setup

If this is the user's first time creating a project, they will be prompted to sign in with their GitHub account and grant the necessary permissions for GhostBuilder to create private repositories and push commits on their behalf. This is a one-time setup process.