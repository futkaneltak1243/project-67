package com.ghostbuilder.domain.usecases

import com.ghostbuilder.domain.interfaces.IGitHubRepository
import javax.inject.Inject

class CreateGitHubRepositoryUseCase @Inject constructor(
    private val gitHubRepository: IGitHubRepository
) {
    suspend operator fun invoke(name: String, description: String): Result<String> {
        return gitHubRepository.createRepository(name, description)
    }
}
