package com.ghosts.of.history.domain.repositories

import com.ghosts.of.history.data.network.models.GitHubRepo
import com.ghosts.of.history.data.network.models.TreeEntry

interface GitHubRepository {
    suspend fun checkRepoExists(token: String, owner: String, repo: String): Result<Boolean>
    suspend fun createRepo(token: String, name: String, description: String, isPrivate: Boolean): Result<GitHubRepo>
    suspend fun createBlob(token: String, owner: String, repo: String, content: String): Result<String>
    suspend fun createTree(
        token: String,
        owner: String,
        repo: String,
        baseTreeSha: String?,
        treeEntries: List<TreeEntry>
    ): Result<String>

    suspend fun createCommit(
        token: String,
        owner: String,
        repo: String,
        message: String,
        treeSha: String,
        parentShas: List<String>
    ): Result<String>

    suspend fun updateReference(token: String, owner: String, repo: String, ref: String, commitSha: String): Result<Unit>
}
