package com.ghostbuilder.data.datasources

import com.ghostbuilder.domain.models.Project

interface ProjectLocalDataSource {
    suspend fun createProject(project: Project)
    suspend fun getProject(projectId: String): Project?
    suspend fun updateProject(project: Project)
    suspend fun deleteProject(projectId: String)
    suspend fun getAllProjects(): List<Project>
}
