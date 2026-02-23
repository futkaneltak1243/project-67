package com.ghostbuilder.domain.usecases

import com.ghostbuilder.domain.services.FileSystemManager
import javax.inject.Inject

class ProvisionDefaultSeedsUseCase @Inject constructor(
    private val fileSystemManager: FileSystemManager
) {
    suspend operator fun invoke(projectId: String): Result<Unit> {
        return fileSystemManager.copyAssetsToProject(projectId, "seeds")
    }
}
