package com.ghosts.of.history.data.services

import com.ghosts.of.history.domain.models.VoiceCommand
import com.ghosts.of.history.domain.services.VoiceCommandProcessor
import javax.inject.Inject

class VoiceCommandProcessorImpl @Inject constructor() : VoiceCommandProcessor {
    override fun process(text: String): VoiceCommand {
        val normalizedText = text.lowercase().trim()

        return when {
            normalizedText.startsWith("open file") -> {
                val fileName = normalizedText.removePrefix("open file").trim()
                VoiceCommand.OpenFile(fileName)
            }
            normalizedText.startsWith("edit file") -> {
                val description = normalizedText.removePrefix("edit file").trim()
                // The VoiceCommand.EditFile data class requires a 'target' parameter.
                // As the requirement only specifies extracting the 'description' from the text,
                // 'target' is set to an empty string.
                VoiceCommand.EditFile(target = "", description = description)
            }
            normalizedText == "confirm" || normalizedText == "yes" -> VoiceCommand.Confirm
            normalizedText == "cancel" || normalizedText == "no" -> VoiceCommand.Cancel
            normalizedText == "scroll down" -> VoiceCommand.ScrollDown
            normalizedText == "scroll up" -> VoiceCommand.ScrollUp
            // Commands like 'save file', 'run tests', 'go back', 'undo', 'redo'
            // are not defined in the provided VoiceCommand.kt sealed interface.
            // Therefore, they fall through to the 'else' branch and are treated as Unknown.
            else -> VoiceCommand.Unknown(normalizedText)
        }
    }
}
