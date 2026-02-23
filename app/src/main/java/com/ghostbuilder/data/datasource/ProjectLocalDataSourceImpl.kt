package com.ghostbuilder.data.datasource

import android.content.Context
import com.google.gson.Gson
import com.ghostbuilder.domain.model.Project
import com.ghostbuilder.domain.model.ProjectStage
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProjectLocalDataSourceImpl @Inject constructor(
    private val context: Context,
    private val gson: Gson
) : ProjectLocalDataSource {

    private val PROJECT_STATE_FILE_NAME = "project_state.json"

    override suspend fun getProject(): Project? = withContext(Dispatchers.IO) {
        val file = File(context.filesDir, PROJECT_STATE_FILE_NAME)
        if (!file.exists()) {
            return@withContext null
        }

        try {
            val jsonString = file.readText()
            gson.fromJson(jsonString, Project::class.java)
        } catch (e: FileNotFoundException) {
            // Log error if necessary
            null
        } catch (e: IOException) {
            // Log error if necessary
            null
        } catch (e: Exception) {
            // Catch any other deserialization errors
            null
        }
    }

    override suspend fun saveProject(project: Project) = withContext(Dispatchers.IO) {
        val file = File(context.filesDir, PROJECT_STATE_FILE_NAME)
        try {
            val jsonString = gson.toJson(project)
            file.writeText(jsonString)
        } catch (e: IOException) {
            // Log error if necessary
            throw e // Re-throw or handle as per application's error policy
        }
    }

    override suspend fun createProject(projectName: String): Project {
        val newProject = Project(
            id = UUID.randomUUID().toString(),
            name = projectName,
            stage = ProjectStage.SEEDS
        )
        saveProject(newProject)
        return newProject
    }
}
