package com.ghostbuilder.domain.usecase

import com.ghostbuilder.domain.model.Project
import com.ghostbuilder.domain.repository.ProjectRepository

class GetProjectsUseCase(
    private val repository: ProjectRepository
) {
    operator fun invoke(): List<Project> {
        return repository.getAllProjects()
    }
}
