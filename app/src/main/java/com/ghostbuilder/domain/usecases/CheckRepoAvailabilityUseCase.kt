package com.ghostbuilder.domain.usecases

import com.ghostbuilder.domain.interfaces.IGitHubRepository
import javax.inject.Inject

class CheckRepoAvailabilityUseCase @Inject constructor(private val gitHubRepository: IGitHubRepository) {
    suspend operator fun invoke(baseName: String): Result<String> {
        val initialCheckResult = gitHubRepository.checkRepositoryExists(baseName)

        initialCheckResult.onFailure { return Result.failure(it) }

        if (!initialCheckResult.getOrThrow()) {
            return Result.success(baseName)
        }

        for (i in 1..100) {
            val newName = "$baseName-$i"
            val checkResult = gitHubRepository.checkRepositoryExists(newName)

            checkResult.onFailure { return Result.failure(it) }

            if (!checkResult.getOrThrow()) {
                return Result.success(newName)
            }
        }

        return Result.failure(Exception("Could not find an available repository name for $baseName after 100 attempts."))
    }
}
