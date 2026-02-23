# 03 Import and Verify Seeds Flow

This document describes the user journey for importing and verifying seed files for a new project.

## 1. Default Seeds

- Upon project creation, GhostBuilder automatically includes a default set of seed files in the `/seed` directory.
- These files provide the foundational protocols and definitions for the AI's behavior and are editable by the user.

## 2. Manual Seed Management (Optional)

- The user can manually add, remove, or edit the seed files using the in-app editor.
- This allows for customization of the project's foundational rules to fit specific needs.
- This is a UI-driven process for pasting content into new or existing files.

## 3. Verification Gate

- Before the user can begin the main authoring process ("Continue building"), they must verify the seeds.
- This is initiated by tapping a "Verify" button in the UI.
- Verification is a fully automatic process where GhostBuilder checks the seed files against a built-in (but editable) checklist to ensure they are complete and correctly formatted.

## 4. Verification Outcome

- **Pass:** If verification is successful, the `project_state.json` file is updated to mark the seeds as verified, along with a timestamp. The user can now proceed to the next stage.
- **Fail:** If verification fails, the app will report the specific errors. The user is blocked from proceeding. They must correct the issues in the seed files and run the verification process again. The `project_state.json` file records the failure event with short error codes.

## 5. Re-verification

- If a user modifies any file in the `/seed` directory after a successful verification, the seeds will be marked as unverified.
- The "Continue building" functionality will be blocked until the user successfully runs the verification process again. This ensures that the project's foundation remains consistent.