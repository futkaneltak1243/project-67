package com.ghosts.of.history.domain.models

sealed interface VoiceCommand {
    data class OpenFile(val fileName: String) : VoiceCommand
    data class CreateFile(val fileName: String) : VoiceCommand
    data class EditFile(val target: String, val description: String) : VoiceCommand
    data class ReadFile(val fileName: String? = null) : VoiceCommand
    object ListFiles : VoiceCommand
    object ScrollUp : VoiceCommand
    object ScrollDown : VoiceCommand
    object Confirm : VoiceCommand
    object Cancel : VoiceCommand
    object StopListening : VoiceCommand
    data class Unknown(val transcription: String) : VoiceCommand
}
