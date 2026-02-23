package com.ghosts.of.history.domain.repositories

import com.ghosts.of.history.domain.models.ProjectState

interface ProjectStateRepository {
    suspend fun getProjectState(projectId: String): Result<ProjectState>
    suspend fun saveProjectState(projectId: String, projectState: ProjectState): Result<Unit>
}
