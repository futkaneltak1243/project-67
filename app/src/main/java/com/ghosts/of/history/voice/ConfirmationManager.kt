package com.ghosts.of.history.voice

import com.ghosts.of.history.voice.model.PendingAction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfirmationManager @Inject constructor() {

    private var currentAction: PendingAction? = null

    val pendingAction: PendingAction?
        get() = currentAction

    fun setPendingAction(action: PendingAction) {
        currentAction = action
    }

    fun hasPendingAction(): Boolean {
        return currentAction != null
    }

    fun confirmAction(): PendingAction? {
        currentAction?.let { action ->
            action.received_confirmations++
            return action
        }
        return null
    }

    fun cancelAction() {
        currentAction = null
    }
}