package com.ghostbuilder.domain.managers

import com.ghostbuilder.domain.models.PendingAction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfirmationManager @Inject constructor() {

    private var currentAction: PendingAction? = null

    fun startConfirmation(action: PendingAction) {
        currentAction = action
    }

    fun confirm(): Boolean {
        return currentAction?.let {\ action ->
            action.confirmationsReceived++
            action.confirmationsReceived >= action.requiredConfirmations
        } ?: false
    }

    fun getCurrentAction(): PendingAction? {
        return currentAction
    }

    fun clear() {
        currentAction = null
    }
}
