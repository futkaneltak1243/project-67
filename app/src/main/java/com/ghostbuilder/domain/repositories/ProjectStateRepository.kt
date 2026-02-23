package com.ghostbuilder.domain.repositories

import com.ghostbuilder.domain.models.ProjectState

interface ProjectStateRepository {
    suspend fun getProjectState(): Result<ProjectState>
}
