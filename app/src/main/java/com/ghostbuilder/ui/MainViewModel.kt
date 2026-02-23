package com.ghostbuilder.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghostbuilder.domain.models.Project
import com.ghostbuilder.domain.usecases.CreateProjectUseCase
import com.ghostbuilder.domain.usecases.GetCurrentProjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Sealed interface representing the UI state for the Main screen.
 */
sealed interface MainUiState {
    /** Indicates that the project is currently being loaded. */
    object Loading : MainUiState

    /** Indicates that a project has been successfully loaded. */
    data class ProjectLoaded(val project: Project) : MainUiState

    /** Indicates that an error occurred during project loading or creation. */
    data class Error(val message: String) : MainUiState
}

/**
 * ViewModel for the Main screen, responsible for managing the UI state related to project loading and creation.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentProjectUseCase: GetCurrentProjectUseCase,
    private val createProjectUseCase: CreateProjectUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        loadOrCreateProject()
    }

    /**
     * Attempts to load the current project. If no project exists, it attempts to create a new one.
     * Updates the [_uiState] based on the outcome.
     */
    private fun loadOrCreateProject() {
        viewModelScope.launch {
            _uiState.value = MainUiState.Loading
            try {
                val currentProjectResult = getCurrentProjectUseCase()
                if (currentProjectResult.isSuccess) {
                    val project = currentProjectResult.getOrNull()
                    if (project != null) {
                        _uiState.value = MainUiState.ProjectLoaded(project)
                    } else {
                        // No current project found, attempt to create a new one
                        val newProjectResult = createProjectUseCase()
                        if (newProjectResult.isSuccess) {
                            val newProject = newProjectResult.getOrNull()
                            if (newProject != null) {
                                _uiState.value = MainUiState.ProjectLoaded(newProject)
                            } else {
                                _uiState.value = MainUiState.Error("Failed to create project: returned null.")
                            }
                        } else {
                            _uiState.value = MainUiState.Error(
                                "Failed to create project: ${newProjectResult.exceptionOrNull()?.localizedMessage ?: "Unknown error"}"
                            )
                        }
                    }
                } else {
                    _uiState.value = MainUiState.Error(
                        "Failed to load current project: ${currentProjectResult.exceptionOrNull()?.localizedMessage ?: "Unknown error"}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = MainUiState.Error("An unexpected error occurred: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }
}