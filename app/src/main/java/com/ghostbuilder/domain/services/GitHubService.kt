package com.ghostbuilder.domain.services

import kotlin.Result

interface GitHubService {
    suspend fun checkRepositoryExists(owner: String, repoName: String): Result<Boolean>
    suspend fun createRepository(owner: String, repoName: String, description: String, isPrivate: Boolean): Result<Unit>
    suspend fun createBlob(owner: String, repoName: String, content: String): Result<String>
    suspend fun createTree(
        owner: String,
        repoName: String,
        baseTreeSha: String?,
        treeItems: List<Map<String, String>>
    ): Result<String>
    suspend fun createCommit(
        owner: String,
        repoName: String,
        message: String,
        treeSha: String,
        parentShas: List<String>
    ): Result<String>
    suspend fun updateBranchReference(owner: String, repoName: String, branchName: String, commitSha: String): Result<Unit>
    suspend fun deleteGitHubRepository(repoName: String): Result<Unit>
}
