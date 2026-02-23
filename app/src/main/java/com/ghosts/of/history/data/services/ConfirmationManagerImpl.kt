package com.ghosts.of.history.data.services

import com.ghosts.of.history.domain.models.PendingAction
import com.ghosts.of.history.domain.models.VoiceCommand
import com.ghosts.of.history.domain.services.ConfirmationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfirmationManagerImpl @Inject constructor() : ConfirmationManager {

    private val _pendingAction = MutableStateFlow<PendingAction?>(null)
    override val pendingAction: StateFlow<PendingAction?> = _pendingAction.asStateFlow()

    override suspend fun setPendingAction(command: VoiceCommand) {
        _pendingAction.value = PendingAction(command = command, confirmations = 0)
    }

    override suspend fun confirm(): VoiceCommand? {
        val currentAction = _pendingAction.value
        if (currentAction == null) {
            return null
        }

        val newConfirmations = currentAction.confirmations + 1
        val isDestructive = when (currentAction.command) {
            is VoiceCommand.OpenFile,
            is VoiceCommand.CreateFile,
            is VoiceCommand.EditFile -> true
            else -> false
        }

        val requiredConfirmations = if (isDestructive) {
            PendingAction.REQUIRED_CONFIRMATIONS_FOR_DESTRUCTIVE_ACTION
        } else {
            1 // Non-destructive actions require only one confirmation
        }

        if (newConfirmations >= requiredConfirmations) {
            val confirmedCommand = currentAction.command
            _pendingAction.value = null // Action is fully confirmed, clear it
            return confirmedCommand
        } else {
            _pendingAction.value = currentAction.copy(confirmations = newConfirmations)
            return null // Not yet fully confirmed
        }
    }

    override suspend fun clear() {
        _pendingAction.value = null
    }
}