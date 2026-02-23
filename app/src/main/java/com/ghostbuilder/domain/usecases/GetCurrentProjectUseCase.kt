package com.ghostbuilder.domain.usecases

import com.ghostbuilder.domain.models.Project
import com.ghostbuilder.domain.repositories.ProjectRepository
import javax.inject.Inject

class GetCurrentProjectUseCase @Inject constructor(
    private val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(): Project? {
        return projectRepository.getCurrentProject()
    }
}
