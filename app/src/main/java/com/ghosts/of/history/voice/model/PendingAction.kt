package com.ghosts.of.history.voice.model

data class PendingAction(
    val actionType: String,
    val payload: Map<String, Any>,
    val confirmationPrompt: String,
    val requiredConfirmations: Int = 1,
    var receivedConfirmations: Int = 0
) {
    fun isConfirmed(): Boolean {
        return receivedConfirmations >= requiredConfirmations
    }
}
