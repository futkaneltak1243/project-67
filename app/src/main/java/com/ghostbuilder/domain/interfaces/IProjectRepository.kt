package com.ghostbuilder.domain.interfaces

import com.ghostbuilder.data.models.ProjectState

interface IProjectRepository {
    suspend fun getProjectState(projectId: String): Result<ProjectState>
    suspend fun saveProjectState(projectId: String, projectState: ProjectState): Result<Unit>
    suspend fun getProjectFiles(projectId: String): Result<List<String>>
    suspend fun readFileContent(projectId: String, filePath: String): Result<String>
    suspend fun writeFileContent(projectId: String, filePath: String, content: String): Result<Unit>
}
