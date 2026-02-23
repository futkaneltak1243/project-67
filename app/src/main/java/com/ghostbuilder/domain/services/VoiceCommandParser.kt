package com.ghostbuilder.domain.services

import com.ghostbuilder.domain.model.Command

interface VoiceCommandParser {
    fun parse(voiceInput: String): Command
}
