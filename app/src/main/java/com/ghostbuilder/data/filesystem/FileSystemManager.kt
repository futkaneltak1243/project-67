package com.ghostbuilder.data.filesystem

import android.content.Context
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FileSystemManager @Inject constructor(private val context: Context) {

    /**
     * Returns the root directory for a given project UUID.
     * The project root is located within the app's internal files directory.
     */
    fun getProjectRoot(projectId: String): File {
        return File(context.filesDir, "projects/$projectId")
    }

    /**
     * Reads the content of a file within a project's directory.
     * @param projectId The UUID of the project.
     * @param relativePath The path to the file relative to the project root.
     * @return The content of the file as a String.
     * @throws java.io.IOException if the file cannot be read.
     */
    suspend fun readFile(projectId: String, relativePath: String): String = withContext(Dispatchers.IO) {
        val projectRoot = getProjectRoot(projectId)
        val file = projectRoot.resolve(relativePath)
        if (!file.exists() || !file.isFile) {
            throw java.io.FileNotFoundException("File not found: ${file.absolutePath}")
        }
        file.readText()
    }

    /**
     * Writes content to a file within a project's directory, creating parent directories if they don't exist.
     * @param projectId The UUID of the project.
     * @param relativePath The path to the file relative to the project root.
     * @param content The string content to write to the file.
     * @throws java.io.IOException if the file cannot be written.
     */
    suspend fun writeFile(projectId: String, relativePath: String, content: String) = withContext(Dispatchers.IO) {
        val projectRoot = getProjectRoot(projectId)
        val file = projectRoot.resolve(relativePath)

        // Ensure parent directories exist
        file.parentFile?.mkdirs()

        file.writeText(content)
    }
}
