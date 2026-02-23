package com.ghostbuilder.domain.models

/**
 * Represents different voice modes for the application.
 */
enum class VoiceMode {
    FACTORY,
    QUIET
}

/**
 * Represents an action that is pending and requires user confirmations.
 *
 * @property actionDetails A string describing the action.
 * @property requiredConfirmations The number of confirmations needed for the action to proceed.
 * @property confirmationsReceived The number of confirmations received so far, defaults to 0.
 * @property isDestructive A boolean flag indicating if the action is destructive.
 */
data class PendingAction(
    val actionDetails: String,
    val requiredConfirmations: Int,
    val confirmationsReceived: Int = 0,
    val isDestructive: Boolean
)
