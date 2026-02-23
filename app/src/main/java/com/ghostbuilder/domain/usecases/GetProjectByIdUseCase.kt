package com.ghostbuilder.domain.usecases

import com.ghostbuilder.domain.models.Project
import com.ghostbuilder.domain.repositories.ProjectRepository
import javax.inject.Inject

class GetProjectByIdUseCase @Inject constructor(
    private val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(projectId: String): Project? {
        return projectRepository.getProjectById(projectId)
    }
}
