package com.ghostbuilder.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghostbuilder.domain.models.Project
import com.ghostbuilder.domain.repositories.ProjectRepository
import com.ghostbuilder.domain.usecases.CreateProjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateProjectUiState(
    val projectName: String = "",
    val projectDescription: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val creationSuccess: Boolean = false
)

@HiltViewModel
class CreateProjectViewModel @Inject constructor(
    private val createProjectUseCase: CreateProjectUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateProjectUiState())
    val uiState: StateFlow<CreateProjectUiState> = _uiState.asStateFlow()

    fun onProjectNameChange(name: String) {
        _uiState.update { it.copy(projectName = name) }
    }

    fun onProjectDescriptionChange(description: String) {
        _uiState.update { it.copy(projectDescription = description) }
    }

    fun createProject() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val projectName = uiState.value.projectName
            createProjectUseCase(projectName)
                .onSuccess {
                    _uiState.update { it.copy(creationSuccess = true, isLoading = false) }
                }
                .onFailure {\ exception ->
                    _uiState.update { it.copy(error = exception.message, isLoading = false) }
                }
        }
    }

    fun onCreationSuccessHandled() {
        _uiState.update { it.copy(creationSuccess = false) }
    }
}
