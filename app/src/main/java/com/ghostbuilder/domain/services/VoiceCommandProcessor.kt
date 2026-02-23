package com.ghostbuilder.domain.services

import com.ghostbuilder.domain.managers.ConfirmationManager
import com.ghostbuilder.domain.managers.TTSManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
final class VoiceCommandProcessor @Inject constructor(
    private val confirmationManager: ConfirmationManager,
    private val ttsManager: TTSManager
) {

    suspend fun processCommand(text: String) {
        // Body can be empty for now
    }
}
