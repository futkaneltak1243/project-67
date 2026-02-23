package com.ghostbuilder.domain.service

import kotlinx.coroutines.flow.StateFlow

interface VoiceRecognitionService {
    val state: StateFlow<VoiceRecognitionState>

    fun startListening(languageCode: String)
    fun stopListening()
}

sealed class VoiceRecognitionState {
    object Idle : VoiceRecognitionState()
    data class Listening(val isPartial: Boolean) : VoiceRecognitionState()
    data class Result(val text: String) : VoiceRecognitionState()
    data class Error(val error: String) : VoiceRecognitionState()
}
