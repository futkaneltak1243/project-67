package com.ghostbuilder.domain.usecases

import com.ghostbuilder.domain.models.ProjectStage
import com.ghostbuilder.domain.repositories.ProjectRepository
import javax.inject.Inject

/**
 * Use case for updating the stage of the current project.
 */
final class UpdateProjectStageUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val getCurrentProjectUseCase: GetCurrentProjectUseCase
) {

    /**
     * Invokes the use case to update the current project's stage.
     *
     * @param newStage The new [ProjectStage] to set for the project.
     * @return A [Result] indicating success or failure. On failure, it contains an [IllegalStateException]
     *         if no project is found to update.
     */
    suspend operator fun invoke(newStage: ProjectStage): Result<Unit> {
        return getCurrentProjectUseCase().fold(
            onSuccess = { currentProject ->
                val updatedProject = currentProject.copy(stage = newStage)
                projectRepository.updateProject(updatedProject)
            },
            onFailure = { throwable ->
                Result.failure(IllegalStateException("No project found to update.", throwable))
            }
        )
    }
}
