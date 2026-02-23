# 12. AI Request Design

This document outlines the architecture for handling AI requests and responses within the system, covering data structures, state management, and error handling.

## 1. Data Structures

The following Kotlin data classes define the communication contract with the AI, designed for serialization using `kotlinx.serialization`.

```kotlin
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * Represents the top-level JSON object received from the AI.
 * This sealed class allows for future expansion with different types of AI responses.
 */
@Serializable
sealed class AiResponse {
    abstract val schema_version: String
    abstract val response_id: String
    abstract val val speech: String
    abstract val question: Question?
    abstract val commands: List<AiCommand>?

    /**
     * The standard implementation of an AiResponse, containing all expected properties.
     */
    @Serializable
    @SerialName("AiResponse") // This name will be used as the class discriminator value if one is configured
    data class StandardAiResponse(
        override val schema_version: String,
        override val response_id: String,
        override val speech: String,
        override val question: Question? = null,
        override val commands: List<AiCommand>? = null
    ) : AiResponse()
}

/**
 * Represents a question posed by the AI, requiring user input.
 */
@Serializable
data class Question(
    val question_id: String,
    val prompt: String,
    val options: List<String>
)

/**
 * A sealed interface representing various commands the AI can issue to the system.
 */
@Serializable
sealed interface AiCommand {
    /**
     * Command to request specific files from the user or system.
     */
    @Serializable
    @SerialName("RequestFiles")
    data class RequestFiles(val reason: String) : AiCommand

    /**
     * Command to create a new file with specified content.
     */
    @Serializable
    @SerialName("WriteFile")
    data class WriteFile(val file_name: String, val content: String) : AiCommand

    /**
     * Command to request an edit to an existing file, described by a prompt.
     */
    @Serializable
    @SerialName("EditFile")
    data class EditFile(val file_name: String, val description: String) : AiCommand

    /**
     * Command to report progress or a summary of an ongoing task.
     */
    @Serializable
    @SerialName("ReportProgress")
    data class ReportProgress(val summary: String) : AiCommand
}
```

## 2. State Management

The `AiRequestState` sealed class defines the lifecycle of an AI request, enabling clear state transitions and UI updates.

```kotlin
/**
 * Represents the various states an AI request can be in.
 */
sealed class AiRequestState {
    /**
     * Initial state, no active request.
     */
    object Idle : AiRequestState()

    /**
     * A request has been initiated and is currently in progress.
     */
    object Requesting : AiRequestState()

    /**
     * The request completed successfully, and a valid AI response was received and parsed.
     */
    data class Success(val response: AiResponse) : AiRequestState()

    /**
     * The request failed due to a network error, parsing error, or other exception.
     */
    data class Error(val throwable: Throwable) : AiRequestState()
}
```

**Flow Description:**
The system begins in the `Idle` state. When a new AI request is initiated (e.g., by user input or an internal trigger), the state transitions to `Requesting`. Upon receiving a successful response from the AI and successfully parsing it into an `AiResponse` object, the state moves to `Success`, carrying the parsed response. If any error occurs during the request (e.g., network failure, invalid response format, or other exceptions), the state transitions to `Error`, encapsulating the `Throwable` that caused the failure.

## 3. Error Handling and Retry Logic

To enhance robustness, the system incorporates a basic retry mechanism for AI requests.

**Retry Mechanism:**
Upon transitioning to the `Error` state, the system will automatically attempt to retry the failed request **once**. This single retry helps mitigate transient network issues or temporary AI service unavailability. If the request fails a second time (i.e., the retry attempt also results in an `Error` state), the system will then remain in the `Error` state. At this point, it will await explicit user intervention or a manual trigger to initiate a new request, preventing infinite retry loops and allowing for human inspection of persistent issues.
