package com.ghostbuilder.data.repositories

import android.content.res.AssetManager
import com.ghostbuilder.domain.models.ProjectState
import com.ghostbuilder.domain.repositories.ProjectStateRepository
import com.google.gson.Gson
import javax.inject.Inject

class ProjectStateRepositoryImpl @Inject constructor(
    private val assetManager: AssetManager,
    private val gson: Gson
) : ProjectStateRepository {

    override suspend fun getProjectState(): Result<ProjectState> {
        return try {
            val jsonString = assetManager.open("project_state.json")
                .bufferedReader()
                .use { it.readText() }
            val projectState = gson.fromJson(jsonString, ProjectState::class.java)
            Result.success(projectState)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
