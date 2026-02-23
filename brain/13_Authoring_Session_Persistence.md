# 13. Authoring Session Persistence

This document outlines the architecture for persisting the authoring session state within the application.

## 1. Data Structure

The authoring session state will be represented by the following Kotlin data class. It is designed to be serializable for easy storage and retrieval.

```kotlin
import kotlinx.serialization.Serializable

@Serializable
data class AuthoringSessionState(
    val projectId: String,
    val filePath: String,
    val lastQuestion: String? = null,
    val draftContent: String? = null
)
```

## 2. Repository Interface

The `AuthoringSessionRepository` interface defines the contract for the persistence layer, abstracting the underlying storage mechanism.

```kotlin
interface AuthoringSessionRepository {
    /**
     * Saves the current authoring session state.
     * @param state The AuthoringSessionState to save.
     */
    suspend fun saveSession(state: AuthoringSessionState)

    /**
     * Loads the last saved authoring session state.
     * @return The loaded AuthoringSessionState, or null if no session is found.
     */
    suspend fun loadSession(): AuthoringSessionState?

    /**
     * Clears any stored authoring session state.
     */
    suspend fun clearSession()
}
```

## 3. Implementation Details

The `AuthoringSessionState` will be stored as a JSON file named `authoring_session.json`.
This file will reside in the application's internal cache directory to ensure it's temporary and can be cleared by the system or user.

It is crucial that `authoring_session.json` is excluded from version control (e.g., via `.gitignore`) as it contains user-specific session data and should not be committed to the repository.