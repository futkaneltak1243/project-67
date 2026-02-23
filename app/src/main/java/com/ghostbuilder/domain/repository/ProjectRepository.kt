package com.ghostbuilder.domain.repository

import com.ghostbuilder.domain.model.Project
import java.util.UUID

interface ProjectRepository {
    suspend fun createProject(name: String): Result<Project>
    suspend fun getProject(id: UUID): Result<Project?>
    suspend fun listProjects(): Result<List<Project>>
    suspend fun updateProject(project: Project): Result<Unit>
}
