package com.ghostbuilder.data.datasource

import com.ghostbuilder.data.model.Project

interface ProjectLocalDataSource {
    suspend fun getProject(): Project?
    suspend fun saveProject(project: Project)
    suspend fun createProject(projectName: String): Project
}
