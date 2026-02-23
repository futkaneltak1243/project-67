package com.ghosts.of.history.data.repositories

import com.ghosts.of.history.data.filesystem.FileSystemManager
import com.ghosts.of.history.domain.models.ProjectState
import com.ghosts.of.history.domain.repositories.ProjectStateRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import javax.inject.Inject

class ProjectStateRepositoryImpl @Inject constructor(
    private val fileSystemManager: FileSystemManager,
    private val gson: Gson
) : ProjectStateRepository {

    companion object {
        private const val PROJECT_STATE_FILENAME = "project_state.json"
    }

    override suspend fun getProjectState(projectId: String): Result<ProjectState> {
        return fileSystemManager.readFile(projectId, PROJECT_STATE_FILENAME).fold(
            onSuccess = { jsonString ->
                try {
                    Result.success(gson.fromJson(jsonString, ProjectState::class.java))
                } catch (e: JsonSyntaxException) {
                    Result.failure(e)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )
    }

    override suspend fun saveProjectState(projectId: String, projectState: ProjectState): Result<Unit> {
        val jsonString = gson.toJson(projectState)
        return fileSystemManager.writeFile(projectId, PROJECT_STATE_FILENAME, jsonString)
    }
}
