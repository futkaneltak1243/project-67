package com.ghostbuilder.data.datasources

import android.content.Context
import com.ghostbuilder.domain.models.Project
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.io.FileNotFoundException

class ProjectLocalDataSourceImpl @Inject constructor(
    private val context: Context,
    private val json: Json
) : ProjectLocalDataSource {

    private const val PROJECT_STATE_FILE = "project_state.json"

    override suspend fun saveProject(project: Project) {
        withContext(Dispatchers.IO) {
            val jsonString = json.encodeToString(project)
            context.openFileOutput(PROJECT_STATE_FILE, Context.MODE_PRIVATE).use {
                it.write(jsonString.toByteArray())
            }
        }
    }

    override suspend fun getProject(): Project? {
        return withContext(Dispatchers.IO) {
            try {
                context.openFileInput(PROJECT_STATE_FILE).use {
                    val jsonString = it.bufferedReader().use { reader -> reader.readText() }
                    json.decodeFromString<Project>(jsonString)
                }
            } catch (e: FileNotFoundException) {
                null
            } catch (e: Exception) {
                // Log other potential deserialization or IO errors if needed
                null
            }
        }
    }

    override suspend fun deleteProject() {
        withContext(Dispatchers.IO) {
            context.deleteFile(PROJECT_STATE_FILE)
        }
    }
}
