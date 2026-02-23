package com.ghostbuilder.domain.repositories

import com.ghostbuilder.domain.models.Project

interface ProjectRepository {
    suspend fun createProject(projectName: String): Project
    suspend fun getProject(projectId: String): Project?
    suspend fun getAllProjects(): List<Project>
    suspend fun updateProject(project: Project)
    suspend fun deleteProject(projectId: String)
}
