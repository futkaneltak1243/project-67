package com.ghostbuilder.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghostbuilder.domain.model.ProjectState
import com.ghostbuilder.domain.usecase.CreateProjectUseCase
import com.ghostbuilder.domain.usecase.GetProjectStateUseCase
import com.ghostbuilder.domain.usecase.GetSeedFilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProjectUiState {
    object Loading : ProjectUiState
    object NoProject : ProjectUiState
    data class ProjectLoaded(val projectState: ProjectState) : ProjectUiState
    data class SeedStage(val seedFiles: List<String>) : ProjectUiState
    object Error : ProjectUiState
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProjectStateUseCase: GetProjectStateUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val getSeedFilesUseCase: GetSeedFilesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProjectUiState>(ProjectUiState.Loading)
    val uiState: StateFlow<ProjectUiState> = _uiState.asStateFlow()

    init {
        loadProjectState()
    }

    private fun loadProjectState() {
        viewModelScope.launch {
            _uiState.value = ProjectUiState.Loading
            try {
                val projectState = getProjectStateUseCase()
                if (projectState.currentStage == ProjectStage.SEED) {
                    viewModelScope.launch {
                        try {
                            val seedFiles = getSeedFilesUseCase(projectState.projectId)
                            _uiState.value = ProjectUiState.SeedStage(seedFiles)
                        } catch (e: Exception) {
                            _uiState.value = ProjectUiState.Error
                        }
                    }
                } else {
                    _uiState.value = ProjectUiState.ProjectLoaded(projectState)
                }
            } catch (e: Exception) {
                _uiState.value = ProjectUiState.NoProject
            }
        }
    }

    fun createProject(projectName: String) {
        viewModelScope.launch {
            createProjectUseCase(projectName)
            loadProjectState() // Refresh state after creating a project
        }
    }
}
