# 16 Roles and Permissions

This document outlines the roles and permissions model for GhostBuilder, which is designed for simplicity and security in its first version.

## 1. Single-Owner Model

-   **Single User:** GhostBuilder operates on a single-owner model. The application is designed for an individual user, and there is no functionality for multiple accounts, team collaboration, or shared projects in version 1.
-   **GitHub-Based Identity:** The user's identity is tied directly to their GitHub account, which is used for signing in, creating repositories, and restoring projects. There is no separate GhostBuilder user account system.

## 2. Application Security

-   **Device-Level Security:** The application does not implement its own separate lock screen or passcode. It relies on the standard security measures of the user's Android device (e.g., PIN, password, fingerprint, facial recognition).
-   **Permissions:** The primary permission required by the application is the ability to interact with the user's GitHub account to create and manage private repositories. This permission is granted by the user via an OAuth flow during the initial setup.
