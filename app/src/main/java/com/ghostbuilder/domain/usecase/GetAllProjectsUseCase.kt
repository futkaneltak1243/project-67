package com.ghostbuilder.domain.usecase

import com.ghostbuilder.domain.model.Project
import com.ghostbuilder.domain.repository.ProjectRepository
import javax.inject.Inject

class GetAllProjectsUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(): List<Project> {
        return repository.getAllProjects()
    }
}
