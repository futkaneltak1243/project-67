package com.ghostbuilder.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghostbuilder.domain.model.AddFunctionCommand
import com.ghostbuilder.domain.model.CreateFileCommand
import com.ghostbuilder.domain.model.EditFileCommand
import com.ghostbuilder.domain.model.NavigateCommand
import com.ghostbuilder.domain.model.UnknownCommand
import com.ghostbuilder.domain.usecase.ParseVoiceCommandUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val parseVoiceCommandUseCase: ParseVoiceCommandUseCase
) : ViewModel() {

    private val _fileName: MutableStateFlow<String?> = MutableStateFlow(null)
    val fileName: StateFlow<String?> = _fileName.asStateFlow()

    private val _fileContent: MutableStateFlow<String> = MutableStateFlow("")
    val fileContent: StateFlow<String> = _fileContent.asStateFlow()

    fun processVoiceCommand(voiceInput: String) {
        viewModelScope.launch {
            val command = parseVoiceCommandUseCase(voiceInput)
            when (command) {
                is CreateFileCommand -> {
                    _fileName.value = command.fileName
                    _fileContent.value = "// New file: ${command.fileName}"
                    Log.d("EditorViewModel", "Created file: ${command.fileName}")
                }
                is NavigateCommand -> {
                    _fileName.value = command.targetPath
                    _fileContent.value = "// Loading file: ${command.targetPath}..."
                    Log.d("EditorViewModel", "Navigated to: ${command.targetPath}")
                }
                is EditFileCommand -> {
                    Log.d("EditorViewModel", "EditFileCommand not yet handled: $command")
                }
                is AddFunctionCommand -> {
                    Log.d("EditorViewModel", "AddFunctionCommand not yet handled: $command")
                }
                is UnknownCommand -> {
                    Log.d("EditorViewModel", "UnknownCommand not yet handled: $command")
                }
            }
        }
    }
}
