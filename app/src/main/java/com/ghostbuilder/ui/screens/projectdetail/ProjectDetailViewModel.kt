package com.ghostbuilder.ui.screens.projectdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghostbuilder.domain.models.Project
import com.ghostbuilder.domain.models.ProjectStage
import com.ghostbuilder.domain.usecases.GetProjectByIdUseCase
import com.ghostbuilder.domain.usecases.UpdateProjectStageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val updateProjectStageUseCase: UpdateProjectStageUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _project = MutableStateFlow<Project?>(null)
    val project: StateFlow<Project?> = _project.asStateFlow()

    init {
        savedStateHandle.get<String>("projectId")?.let {\ projectId ->
            viewModelScope.launch {
                _project.value = getProjectByIdUseCase(projectId)
            }
        }
    }

    fun advanceProjectStage() {
        viewModelScope.launch {
            val currentProject = _project.value
            if (currentProject != null) {
                val currentStage = currentProject.stage
                val stages = ProjectStage.values()
                val currentIndex = stages.indexOf(currentStage)

                if (currentIndex < stages.lastIndex) {
                    val nextStage = stages[currentIndex + 1]
                    val updatedProject = currentProject.copy(stage = nextStage)
                    updateProjectStageUseCase(updatedProject)
                    _project.value = updatedProject
                }
            }
        }
    }
}
