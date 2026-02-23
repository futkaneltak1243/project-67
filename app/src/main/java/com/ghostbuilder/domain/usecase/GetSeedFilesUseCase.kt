package com.ghostbuilder.domain.usecase

import com.ghostbuilder.domain.repository.ProjectFilesRepository
import javax.inject.Inject

class GetSeedFilesUseCase @Inject constructor(
    private val projectFilesRepository: ProjectFilesRepository
) {
    suspend operator fun invoke(projectId: String): List<String> {
        return projectFilesRepository.getSeedFiles(projectId)
    }
}
