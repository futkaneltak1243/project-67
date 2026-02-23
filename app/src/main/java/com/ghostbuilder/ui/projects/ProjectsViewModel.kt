package com.ghostbuilder.ui.projects

import androidx.lifecycle.ViewModel
import com.ghostbuilder.domain.model.Project
import com.ghostbuilder.domain.usecase.GetProjectsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProjectsViewModel(getProjectsUseCase: GetProjectsUseCase) : ViewModel() {

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects

    init {
        _projects.value = getProjectsUseCase()
    }
}
