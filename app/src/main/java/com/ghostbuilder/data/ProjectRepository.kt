package com.ghostbuilder.data

import com.ghostbuilder.domain.interfaces.IFileSystemManager
import com.ghostbuilder.domain.interfaces.IProjectRepository
import com.ghostbuilder.data.models.ProjectState
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val fileSystemManager: IFileSystemManager
) : IProjectRepository {

    override suspend fun getProjectState(projectId: String): Result<ProjectState> {
        return fileSystemManager.getProjectState(projectId)
    }

    override suspend fun saveProjectState(
        projectId: String,
        projectState: ProjectState
    ): Result<Unit> {
        return fileSystemManager.saveProjectState(projectId, projectState)
    }

    override suspend fun getProjectFiles(projectId: String): Result<List<String>> {
        return fileSystemManager.getProjectFiles(projectId)
    }

    override suspend fun getFileContent(projectId: String, filePath: String): Result<String> {
        return fileSystemManager.getFileContent(projectId, filePath)
    }

    override suspend fun saveFileContent(
        projectId: String,
        filePath: String,
        content: String
    ): Result<Unit> {
        return fileSystemManager.saveFileContent(projectId, filePath, content)
    }
}