package com.ghostbuilder.data.services

import com.ghostbuilder.data.remote.CreateBlobRequest
import com.ghostbuilder.data.remote.CreateCommitRequest
import com.ghostbuilder.data.remote.CreateRepoRequest
import com.ghostbuilder.data.remote.CreateTreeRequest
import com.ghostbuilder.data.remote.GitHubApi
import com.ghostbuilder.data.remote.TreeItem
import com.ghostbuilder.data.remote.UpdateRefRequest
import com.ghostbuilder.domain.services.GitHubService
import javax.inject.Inject
import javax.inject.Named
import retrofit2.HttpException
import kotlin.Result

class GitHubServiceImpl @Inject constructor(
    private val gitHubApi: GitHubApi,
    // Assumption: authenticatedUserLogin is needed for operations where owner is implicit or not passed
    // by the interface method signature but required by the underlying API (e.g., deleteGitHubRepository).
    // In a real application, this would be dynamically provided, e.g., from a user session.
    @Named("authenticatedUserLogin") private val authenticatedUserLogin: String = "ghostbuilder" // Placeholder
) : GitHubService {

    // Assumption: GitHub token is available. In a real application, this would be dynamically retrieved.
    private val githubToken: String = "Bearer YOUR_GITHUB_TOKEN" // Placeholder

    override suspend fun checkRepositoryExists(owner: String, repoName: String): Result<Boolean> {
        return try {
            val response = gitHubApi.getRepo(githubToken, owner, repoName)
            if (response.isSuccessful) {
                Result.success(true)
            } else if (response.code() == 404) {
                Result.success(false)
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: HttpException) {
            if (e.code() == 404) {
                Result.success(false)
            } else {
                Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createRepository(
        owner: String, // This 'owner' parameter is not directly used by gitHubApi.createRepo, as it creates for the authenticated user.
        repoName: String,
        description: String,
        isPrivate: Boolean
    ): Result<Unit> {
        return runCatching {
            val request = CreateRepoRequest(name = repoName, description = description, private = isPrivate)
            val response = gitHubApi.createRepo(githubToken, request)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            Unit
        }
    }

    override suspend fun deleteGitHubRepository(repoName: String): Result<Unit> {
        // Using authenticatedUserLogin as the owner, as the GitHubService interface method
        // does not provide an 'owner' parameter, but GitHubApi.deleteRepo requires it.
        return runCatching {
            val response = gitHubApi.deleteRepo(githubToken, authenticatedUserLogin, repoName)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            Unit
        }
    }

    override suspend fun createBlob(owner: String, repoName: String, content: String): Result<String> {
        return runCatching {
            val request = CreateBlobRequest(content = content)
            val response = gitHubApi.createBlob(githubToken, owner, repoName, request)
            response.body()?.sha ?: throw IllegalStateException("Blob SHA not found or request failed: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun createTree(
        owner: String,
        repoName: String,
        baseTreeSha: String?,
        treeItems: List<Map<String, String>>
    ): Result<String> {
        return runCatching {
            val apiTreeItems = treeItems.map { itemMap ->
                TreeItem(
                    path = itemMap["path"] ?: throw IllegalArgumentException("Tree item missing 'path'"),
                    mode = itemMap["mode"] ?: "100644", // Default to standard file mode
                    type = itemMap["type"] ?: "blob", // Default to blob type
                    sha = itemMap["sha"] ?: throw IllegalArgumentException("Tree item missing 'sha'")
                )
            }
            val request = CreateTreeRequest(base_tree = baseTreeSha, tree = apiTreeItems)
            val response = gitHubApi.createTree(githubToken, owner, repoName, request)
            response.body()?.sha ?: throw IllegalStateException("Tree SHA not found or request failed: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun createCommit(
        owner: String,
        repoName: String,
        message: String,
        treeSha: String,
        parentShas: List<String>
    ): Result<String> {
        return runCatching {
            val request = CreateCommitRequest(message = message, tree = treeSha, parents = parentShas)
            val response = gitHubApi.createCommit(githubToken, owner, repoName, request)
            response.body()?.sha ?: throw IllegalStateException("Commit SHA not found or request failed: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun updateBranchReference(
        owner: String,
        repoName: String,
        branchName: String,
        commitSha: String
    ): Result<Unit> {
        return runCatching {
            val refPath = "heads/$branchName"
            val request = UpdateRefRequest(sha = commitSha)
            val response = gitHubApi.updateRef(githubToken, owner, repoName, refPath, request)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            Unit
        }
    }

    // This method is specified in the task but is not part of the GitHubService interface.
    // It is added as an additional public method in the implementation.
    suspend fun createInitialCommit(owner: String, repo: String, files: Map<String, String>): Result<String> {
        return runCatching {
            // 1. Create blobs for each file
            val blobShas = mutableListOf<Pair<String, String>>() // Pair of (filePath, blobSha)
            for ((filePath, content) in files) {
                val blobSha = createBlob(owner, repo, content)
                    .getOrThrow() // Propagate failure if blob creation fails
                blobShas.add(filePath to blobSha)
            }

            // 2. Create a tree with the blob SHAs
            val treeItems = blobShas.map { (filePath, blobSha) ->
                mapOf(
                    "path" to filePath,
                    "mode" to "100644", // Standard file mode for a file
                    "type" to "blob",
                    "sha" to blobSha
                )
            }
            val treeSha = createTree(owner, repo, baseTreeSha = null, treeItems = treeItems)
                .getOrThrow() // Propagate failure if tree creation fails

            // 3. Create a commit with the tree SHA (initial commit has no parents)
            val commitMessage = "Initial commit"
            val commitSha = createCommit(owner, repo, commitMessage, treeSha, parentShas = emptyList())
                .getOrThrow() // Propagate failure if commit creation fails

            // 4. Update the 'main' branch reference to point to the new commit SHA
            updateBranchReference(owner, repo, "main", commitSha)
                .getOrThrow() // Propagate failure if ref update fails

            commitSha // Return the commit SHA on success
        }
    }
}
