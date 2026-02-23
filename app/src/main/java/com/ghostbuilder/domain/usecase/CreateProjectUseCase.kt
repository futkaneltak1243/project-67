package com.ghostbuilder.domain.usecase

import com.ghostbuilder.domain.model.ProjectState
import com.ghostbuilder.domain.model.ProjectStage
import com.ghostbuilder.domain.repository.ProjectStateRepository
import java.util.UUID
import javax.inject.Inject

class CreateProjectUseCase @Inject constructor(
    private val projectStateRepository: ProjectStateRepository
) {
    suspend operator fun invoke(projectName: String) {
        val projectId = UUID.randomUUID().toString()
        val newProjectState = ProjectState(
            projectId = projectId,
            projectName = projectName,
            currentStage = ProjectStage.SEED
        )
        projectStateRepository.saveProjectState(newProjectState)
    }
}
