package com.ghosts.of.history.domain.services

import com.ghosts.of.history.domain.models.VoiceCommand

interface VoiceCommandProcessor {
    fun process(text: String): VoiceCommand
}
