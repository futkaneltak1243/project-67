package com.ghosts.of.history.domain.models

sealed interface VoiceState {
    object Idle : VoiceState
    object Listening : VoiceState
    object Processing : VoiceState
    data class Confirming<T>(val pendingAction: T) : VoiceState
    object Speaking : VoiceState
}
