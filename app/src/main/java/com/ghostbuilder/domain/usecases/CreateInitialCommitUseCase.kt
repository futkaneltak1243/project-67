package com.ghostbuilder.domain.usecases

import com.ghostbuilder.domain.interfaces.IGitHubRepository
import com.ghostbuilder.domain.interfaces.IProjectRepository
import javax.inject.Inject

class CreateInitialCommitUseCase @Inject constructor(
    private val gitHubRepository: IGitHubRepository
) {
    suspend operator fun invoke(repoName: String, files: Map<String, String>): Result<Unit> {
        return gitHubRepository.createInitialCommit(repoName, files)
    }
}
