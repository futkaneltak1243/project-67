package com.ghostbuilder.domain.usecase

import com.ghostbuilder.domain.model.Command
import com.ghostbuilder.domain.services.VoiceCommandParser
import javax.inject.Inject

class ParseVoiceCommandUseCase @Inject constructor(
    private val voiceCommandParser: VoiceCommandParser
) {
    suspend operator fun invoke(commandText: String): Command {
        return voiceCommandParser.parse(commandText)
    }
}
