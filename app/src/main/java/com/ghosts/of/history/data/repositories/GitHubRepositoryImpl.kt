package com.ghosts.of.history.data.repositories

import com.ghosts.of.history.data.network.GitHubApiService
import com.ghosts.of.history.data.network.models.CreateBlobRequest
import com.ghosts.of.history.data.network.models.CreateCommitRequest
import com.ghosts.of.history.data.network.models.CreateRepoRequest
import com.ghosts.of.history.data.network.models.CreateTreeRequest
import com.ghosts.of.history.data.network.models.GitHubRepo
import com.ghosts.of.history.data.network.models.TreeEntry
import com.ghosts.of.history.data.network.models.UpdateRefRequest
import com.ghosts.of.history.domain.repositories.GitHubRepository
import javax.inject.Inject

class GitHubRepositoryImpl @Inject constructor(private val apiService: GitHubApiService) : GitHubRepository {

    override suspend fun checkRepoExists(token: String, owner: String, repo: String): Result<Boolean> = runCatching {
        val response = apiService.checkRepoExists(token, owner, repo)
        when (response.code()) {
            200 -> Result.success(true)
            404 -> Result.success(false)
            else -> Result.failure(Exception("Unexpected response code for checkRepoExists: ${response.code()} - ${response.errorBody()?.string()}"))
        }
    }.getOrElse { Result.failure(it) }

    override suspend fun createRepo(token: String, name: String, description: String, isPrivate: Boolean): Result<GitHubRepo> = runCatching {
        val request = CreateRepoRequest(name = name, description = description, private = isPrivate)
        val response = apiService.createRepo(token, request)
        if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception("Failed to create repository: ${response.code()} - ${response.errorBody()?.string()}"))
        }
    }.getOrElse { Result.failure(it) }

    override suspend fun createBlob(token: String, owner: String, repo: String, content: String): Result<String> = runCatching {
        val request = CreateBlobRequest(content = content, encoding = "utf-8")
        val response = apiService.createBlob(token, owner, repo, request)
        if (response.isSuccessful && response.body()?.sha != null) {
            Result.success(response.body()!!.sha)
        } else {
            Result.failure(Exception("Failed to create blob: ${response.code()} - ${response.errorBody()?.string()}"))
        }
    }.getOrElse { Result.failure(it) }

    override suspend fun createTree(
        token: String,
        owner: String,
        repo: String,
        baseTreeSha: String?,
        treeEntries: List<TreeEntry>
    ): Result<String> = runCatching {
        val request = CreateTreeRequest(base_tree = baseTreeSha, tree = treeEntries)
        val response = apiService.createTree(token, owner, repo, request)
        if (response.isSuccessful && response.body()?.sha != null) {
            Result.success(response.body()!!.sha)
        } else {
            Result.failure(Exception("Failed to create tree: ${response.code()} - ${response.errorBody()?.string()}"))
        }
    }.getOrElse { Result.failure(it) }

    override suspend fun createCommit(
        token: String,
        owner: String,
        repo: String,
        message: String,
        treeSha: String,
        parentShas: List<String>
    ): Result<String> = runCatching {
        val request = CreateCommitRequest(message = message, tree = treeSha, parents = parentShas)
        val response = apiService.createCommit(token, owner, repo, request)
        if (response.isSuccessful && response.body()?.sha != null) {
            Result.success(response.body()!!.sha)
        } else {
            Result.failure(Exception("Failed to create commit: ${response.code()} - ${response.errorBody()?.string()}"))
        }
    }.getOrElse { Result.failure(it) }

    override suspend fun updateReference(token: String, owner: String, repo: String, ref: String, commitSha: String): Result<Unit> = runCatching {
        val request = UpdateRefRequest(sha = commitSha, force = true)
        val response = apiService.updateReference(token, owner, repo, ref, request)
        if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to update reference: ${response.code()} - ${response.errorBody()?.string()}"))
        }
    }.getOrElse { Result.failure(it) }
}
