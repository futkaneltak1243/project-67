# 01 Project Summary

This document provides a high-level summary of the GhostBuilder project.

GhostBuilder is a voice-first Android application designed for software developers and builders who want to work on projects hands-free. The primary use case is for individuals in environments where they cannot interact with a screen, such as during long factory shifts.

The core principles of GhostBuilder are:
- **Voice-First, Hands-Free:** The application is designed to be operated primarily through voice commands, allowing for use when the screen is off and the phone is locked (after a session has been initiated).
- **Predictable & Auditable Output:** The development process is structured and stage-based (Seeds → Walkthrough → Brain → Code). All changes to the project are made through small, controlled approvals, ensuring the output is consistent and trustworthy.
- **GitHub Integration:** Each project is synced to its own private GitHub repository. This serves as a backup, an audit trail, and allows for restoring projects on different devices.
- **Privacy-Focused:** Audio recordings are not stored. Transcripts and logs are kept on-device and are not uploaded.

The application guides the user through a deterministic pipeline to create a complete software project, starting from foundational documents (Seeds), moving to a detailed specification (Walkthrough), implementation planning (Brain), and finally generating the application codebase (`/app`).
