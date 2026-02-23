package com.ghostbuilder.ui.screens.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.ghostbuilder.domain.voiceservice.VoiceRecognitionService
import com.ghostbuilder.domain.voiceservice.VoiceRecognitionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val voiceRecognitionService: VoiceRecognitionService
) : ViewModel() {

    private val _state = MutableStateFlow(VoiceRecognitionState.IDLE)
    val state: StateFlow<VoiceRecognitionState> = _state.asStateFlow()

    private val _recognizedText = MutableStateFlow("")
    val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()

    init {
        viewModelScope.launch {
            voiceRecognitionService.state.collect {
                _state.value = it
            }
        }
        viewModelScope.launch {
            voiceRecognitionService.recognizedText.collect {
                _recognizedText.value = it
            }
        }
    }

    fun startListening() {
        voiceRecognitionService.startListening()
    }

    fun stopListening() {
        voiceRecognitionService.stopListening()
    }
}
