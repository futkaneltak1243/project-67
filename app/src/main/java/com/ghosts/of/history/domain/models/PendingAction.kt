package com.ghosts.of.history.domain.models

data class PendingAction(
    val command: VoiceCommand,
    val confirmations: Int = 0
) {
    companion object {
        const val REQUIRED_CONFIRMATIONS_FOR_DESTRUCTIVE_ACTION = 2
    }
}
