package com.ghostbuilder.data.repositories

import com.ghostbuilder.data.datasources.ProjectLocalDataSource
import com.ghostbuilder.domain.models.Project
import com.ghostbuilder.domain.repositories.ProjectRepository
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val localDataSource: ProjectLocalDataSource
) : ProjectRepository {

    override suspend fun getProject(): Project? {
        return localDataSource.getProject()
    }

    override suspend fun saveProject(project: Project) {
        localDataSource.saveProject(project)
    }

    override suspend fun createProject(projectName: String): Project {
        return localDataSource.createProject(projectName)
    }

    override suspend fun deleteProject() {
        localDataSource.deleteProject()
    }
}
