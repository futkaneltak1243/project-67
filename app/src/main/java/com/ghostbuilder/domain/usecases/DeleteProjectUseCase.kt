package com.ghostbuilder.domain.usecases

import javax.inject.Inject
import com.ghostbuilder.domain.repositories.ProjectRepository

class DeleteProjectUseCase @Inject constructor(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: String) {
        projectRepository.deleteProject(projectId)
    }
}