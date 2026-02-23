package com.ghostbuilder.domain.interfaces

import kotlin.Result

interface IGitHubRepository {
    suspend fun checkRepositoryExists(name: String): Result<Boolean>
    suspend fun createRepository(name: String, description: String): Result<String>
    suspend fun createInitialCommit(repoName: String, files: Map<String, String>): Result<Unit>
}
