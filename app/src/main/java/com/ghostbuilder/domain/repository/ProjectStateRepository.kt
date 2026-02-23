package com.ghostbuilder.domain.repository

import com.ghostbuilder.domain.models.ProjectState

interface ProjectStateRepository {
    suspend fun getProjectState(projectId: String): Result<ProjectState>
    suspend fun saveProjectState(projectId: String, projectState: ProjectState): Result<Unit>
}
