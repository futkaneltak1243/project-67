package com.ghostbuilder.domain.usecases

import com.ghostbuilder.domain.models.Project
import com.ghostbuilder.domain.models.ProjectStage
import com.ghostbuilder.domain.repositories.ProjectRepository
import com.ghostbuilder.domain.usecases.CheckRepoAvailabilityUseCase
import com.ghostbuilder.domain.usecases.CreateGitHubRepositoryUseCase
import com.ghostbuilder.domain.usecases.CreateInitialCommitUseCase
import com.ghostbuilder.domain.usecases.SanitizeProjectNameUseCase
import java.util.UUID
import javax.inject.Inject

class CreateProjectUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val sanitizeProjectNameUseCase: SanitizeProjectNameUseCase,
    private val checkRepoAvailabilityUseCase: CheckRepoAvailabilityUseCase,
    private val createGitHubRepositoryUseCase: CreateGitHubRepositoryUseCase,
    private val createInitialCommitUseCase: CreateInitialCommitUseCase
) {
    suspend operator fun invoke(displayName: String): Result<Project> {
        return runCatching {
            val sanitizedName = sanitizeProjectNameUseCase(displayName)

            val availableRepoName = checkRepoAvailabilityUseCase(sanitizedName)
                .getOrThrow()

            createGitHubRepositoryUseCase(availableRepoName, "Project created by GhostBuilder")
                .getOrThrow()

            val projectId = UUID.randomUUID().toString()
            val githubUrl = "https://github.com/ghostbuilder/$availableRepoName"
            val newProject = Project(
                id = projectId,
                name = displayName,
                githubUrl = githubUrl,
                stage = ProjectStage.SEEDS
            )
            projectRepository.createProject(newProject)

            val initialFiles = mapOf("README.md" to "# $displayName")
            createInitialCommitUseCase(availableRepoName, initialFiles)
                .getOrThrow()

            newProject
        }
    }
}
