package com.ghostbuilder.domain.usecase

import com.ghostbuilder.domain.model.ProjectState
import com.ghostbuilder.domain.repository.ProjectStateRepository
import javax.inject.Inject

class GetProjectStateUseCase @Inject constructor(
    private val repository: ProjectStateRepository
) {
    suspend operator fun invoke(): ProjectState? {
        return repository.getProjectState()
    }
}
