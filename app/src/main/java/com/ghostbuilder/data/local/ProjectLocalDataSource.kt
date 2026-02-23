package com.ghostbuilder.data.local

import com.ghostbuilder.domain.model.Project

interface ProjectLocalDataSource {
    suspend fun createProject(project: Project): Result<Unit>
    suspend fun getProject(projectId: String): Result<Project?>
    suspend fun getAllProjects(): Result<List<Project>>
    suspend fun updateProject(project: Project): Result<Unit>
    suspend fun deleteProject(projectId: String): Result<Unit>
}
