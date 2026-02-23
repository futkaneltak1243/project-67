package com.ghostbuilder.ui.projectlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghostbuilder.domain.model.Project
import com.ghostbuilder.domain.usecase.CreateProjectUseCase
import com.ghostbuilder.domain.usecase.GetAllProjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProjectListState(
    val isLoading: Boolean = false,
    val projects: List<Project> = emptyList()
)

@HiltViewModel
class ProjectListViewModel @Inject constructor(
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val createProjectUseCase: CreateProjectUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectListState())
    val uiState: StateFlow<ProjectListState> = _uiState.asStateFlow()

    init {
        loadProjects()
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val projects = getAllProjectsUseCase()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    projects = projects
                )
            }
        }
    }

    fun createProject(projectName: String) {
        viewModelScope.launch {
            createProjectUseCase(projectName)
            loadProjects() // Refresh the list after creating a project
        }
    }
}