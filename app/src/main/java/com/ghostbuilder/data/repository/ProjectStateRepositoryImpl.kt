package com.ghostbuilder.data.repository

import com.ghostbuilder.data.filesystem.FileSystemManager
import com.ghostbuilder.domain.models.ProjectState
import com.ghostbuilder.domain.repository.ProjectStateRepository
import com.google.gson.Gson
import javax.inject.Inject

class ProjectStateRepositoryImpl @Inject constructor(
    private val fileSystemManager: FileSystemManager,
    private val gson: Gson
) : ProjectStateRepository {

    private const val PROJECT_STATE_FILE_NAME = "project_state.json"

    override suspend fun getProjectState(projectId: String): ProjectState {
        val filePath = "$projectId/$PROJECT_STATE_FILE_NAME"
        val jsonContent = fileSystemManager.readFile(filePath)
        return gson.fromJson(jsonContent, ProjectState::class.java)
    }

    override suspend fun saveProjectState(projectId: String, projectState: ProjectState) {
        val filePath = "$projectId/$PROJECT_STATE_FILE_NAME"
        val jsonContent = gson.toJson(projectState)
        fileSystemManager.writeFile(filePath, jsonContent)
    }
}
