package com.ghostbuilder.data.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.ghostbuilder.domain.model.VoiceRecognitionState
import com.ghostbuilder.domain.service.VoiceRecognitionService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceRecognitionServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : VoiceRecognitionService {

    override val state: StateFlow<VoiceRecognitionState> = MutableStateFlow(VoiceRecognitionState.Idle)

    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

    init {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                (state as MutableStateFlow).value = VoiceRecognitionState.Listening
            }

            override fun onBeginningOfSpeech() {
                (state as MutableStateFlow).value = VoiceRecognitionState.Speaking
            }

            override fun onRmsChanged(rmsdB: Float) {
                // Not required by the prompt, can be used for UI feedback
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Not required by the prompt
            }

            override fun onEndOfSpeech() {
                (state as MutableStateFlow).value = VoiceRecognitionState.Idle
            }

            override fun onError(error: Int) {
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                    SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy"
                    SpeechRecognizer.ERROR_SERVER -> "Server error"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                    else -> "Unknown error: $error"
                }
                (state as MutableStateFlow).value = VoiceRecognitionState.Error(errorMessage)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val recognizedText = matches?.firstOrNull() ?: ""
                (state as MutableStateFlow).value = VoiceRecognitionState.Success(recognizedText)
            }

            override fun onPartialResults(partialResults: Bundle?) {
                // Not required by the prompt, can be used for real-time transcription
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Not required by the prompt
            }
        })
    }

    override fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        speechRecognizer.startListening(intent)
    }

    override fun stopListening() {
        speechRecognizer.stopListening()
    }
}