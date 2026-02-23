package com.ghostbuilder.data.repository

import com.ghostbuilder.domain.models.PipelineStage
import com.ghostbuilder.domain.models.Project
import com.ghostbuilder.domain.models.ProjectState
import com.ghostbuilder.domain.repository.ProjectRepository
import com.ghostbuilder.domain.services.FileSystemManager
import com.ghostbuilder.domain.services.GitHubService
import java.io.File
import java.util.UUID
import javax.inject.Inject
import kotlin.Result

class ProjectRepositoryImpl @Inject constructor(
    private val gitHubService: GitHubService,
    private val fileSystemManager: FileSystemManager
) : ProjectRepository {

    // Assuming a default owner for GitHub operations as it's not specified in the task.
    private val GITHUB_OWNER = "ghostbuilder-org" // Placeholder

    override suspend fun createProject(displayName: String): Result<Project> {
        // 1. Name sanitization logic
        var sanitizedName = displayName.lowercase()
            .replace(Regex("[^a-z0-9\\s-]"), "") // Remove invalid characters
            .replace(Regex("\\s+"), "-") // Replace spaces with hyphens
            .replace(Regex("-+"), "-") // Collapse multiple hyphens
            .trim('-') // Trim leading/trailing hyphens

        if (sanitizedName.isEmpty()) {
            return Result.failure(IllegalArgumentException("Display name cannot result in an empty repository name."))
        }

        // 2. Find unique repository name
        var repoName = sanitizedName
        var suffix = 0
        var uniqueRepoFound = false

        while (!uniqueRepoFound) {
            val checkResult = gitHubService.checkRepositoryExists(GITHUB_OWNER, repoName)
            if (checkResult.isSuccess) {
                if (!checkResult.getOrThrow()) { // getOrThrow() is safe here because isSuccess is true
                    uniqueRepoFound = true
                } else {
                    suffix++
                    repoName = "$sanitizedName-$suffix"
                }
            } else { // checkResult.isFailure
                return Result.failure(checkResult.exceptionOrNull() ?: Exception("Unknown error checking repository existence."))
            }
        }

        // 3. Create GitHub repository
        val createRepoResult = gitHubService.createRepository(
            owner = GITHUB_OWNER,
            repoName = repoName,
            description = "Project created by GhostBuilder: $displayName",
            isPrivate = true // Assuming private as a default
        )
        if (createRepoResult.isFailure) {
            return Result.failure(createRepoResult.exceptionOrNull() ?: Exception("Unknown error creating GitHub repository."))
        }

        // 4. Generate Project ID and create Project object
        val projectId = UUID.randomUUID().toString()
        val project = Project(id = projectId, displayName = displayName)

        // --- Local file system operations ---
        // The task requires calling `fileSystemManager.createProjectDirectories()`.
        // However, the provided `FileSystemManager` interface does not contain this method.
        // I will simulate directory creation using `fileSystemManager.getProjectRoot().mkdirs()`.
        val projectRoot = fileSystemManager.getProjectRoot(projectId)
        if (!projectRoot.mkdirs()) {
            // If directory creation fails, attempt to clean up the GitHub repository.
            // The task requires calling `gitHubService.deleteGitHubRepository()`.
            // However, the provided `GitHubService` interface does not contain this method.
            // Therefore, cleanup of the GitHub repository cannot be performed here.
            // This is a known discrepancy between the task requirements and provided interfaces.
            // gitHubService.deleteGitHubRepository(GITHUB_OWNER, repoName) // Placeholder for cleanup
            return Result.failure(Exception("Failed to create local project directories for project ID: $projectId"))
        }

        // Write initial ProjectState
        val initialProjectState = ProjectState(pipelineStage = PipelineStage.BRAIN, files = emptyList())
        val writeStateResult = fileSystemManager.writeProjectState(projectId, initialProjectState)
        if (writeStateResult.isFailure) {
            // If writing state fails, attempt to clean up.
            // GitHub repository cleanup cannot be performed due to missing method.
            // gitHubService.deleteGitHubRepository(GITHUB_OWNER, repoName) // Placeholder for cleanup
            projectRoot.deleteRecursively() // Attempt local cleanup
            return Result.failure(writeStateResult.exceptionOrNull() ?: Exception("Unknown error writing project state."))
        }

        // --- Create initial commit on GitHub ---
        // The task requires calling `gitHubService.createInitialCommit()`, but this is not a single method.
        // It implies a sequence of calls to create blob, tree, commit, and update branch reference.
        try {
            val readmeContent = "# ${project.displayName}\n\nThis project was created by GhostBuilder."
            val blobResult = gitHubService.createBlob(GITHUB_OWNER, repoName, readmeContent)
            val readmeBlobSha = blobResult.getOrThrow() // Propagate error if blob creation fails

            val treeItems = listOf(mapOf("path" to "README.md", "mode" to "100644", "type" to "blob", "sha" to readmeBlobSha))
            val treeResult = gitHubService.createTree(GITHUB_OWNER, repoName, null, treeItems) // No base tree for initial commit
            val treeSha = treeResult.getOrThrow() // Propagate error if tree creation fails

            val commitMessage = "Initial commit: Project setup"
            val commitResult = gitHubService.createCommit(GITHUB_OWNER, repoName, commitMessage, treeSha, emptyList()) // No parents for initial commit
            val commitSha = commitResult.getOrThrow() // Propagate error if commit creation fails

            val updateBranchResult = gitHubService.updateBranchReference(GITHUB_OWNER, repoName, "main", commitSha) // Assuming 'main' branch
            updateBranchResult.getOrThrow() // Propagate error if branch update fails

        } catch (e: Exception) {
            // If any GitHub operation for initial commit fails, attempt to clean up.
            runCatching {
                gitHubService.deleteGitHubRepository(GITHUB_OWNER, repoName)
            }.onFailure { cleanupError ->
                // Log or handle the cleanup error, but don't hide the original exception
                System.err.println("Warning: Failed to clean up GitHub repository $GITHUB_OWNER/$repoName after project creation failure: ${cleanupError.message}")
            }
            projectRoot.deleteRecursively() // Attempt local cleanup
            return Result.failure(e)
        }

        // If all steps succeed
        return Result.success(project)
    }
}
