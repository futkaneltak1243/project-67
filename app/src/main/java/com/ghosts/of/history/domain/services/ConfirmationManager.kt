package com.ghosts.of.history.domain.services

import com.ghosts.of.history.domain.models.PendingAction
import com.ghosts.of.history.domain.models.VoiceCommand
import kotlinx.coroutines.flow.StateFlow

interface ConfirmationManager {
    val pendingAction: StateFlow<PendingAction?>

    suspend fun setPendingAction(command: VoiceCommand)
    suspend fun confirm(): VoiceCommand?
    suspend fun clear()
}
