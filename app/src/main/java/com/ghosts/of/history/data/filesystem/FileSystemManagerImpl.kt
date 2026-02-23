package com.ghosts.of.history.data.filesystem

import android.app.Application
import java.io.File
import java.io.IOException
import javax.inject.Inject

class FileSystemManagerImpl @Inject constructor(
    private val application: Application
) : FileSystemManager {

    override fun getProjectRoot(projectId: String): File {
        val projectRoot = File(application.filesDir, "projects/$projectId")
        if (!projectRoot.exists()) {
            projectRoot.mkdirs()
        }
        return projectRoot
    }

    override fun readFile(projectId: String, path: String): Result<String> = runCatching {
        val projectRoot = getProjectRoot(projectId)
        val file = File(projectRoot, path)
        file.readText(Charsets.UTF_8)
    }

    override fun writeFile(projectId: String, path: String, content: String): Result<Unit> = runCatching {
        val projectRoot = getProjectRoot(projectId)
        val file = File(projectRoot, path)
        file.parentFile?.mkdirs() // Ensure parent directories exist
        file.writeText(content, Charsets.UTF_8)
    }

    override fun fileExists(projectId: String, path: String): Boolean {
        val projectRoot = getProjectRoot(projectId)
        val file = File(projectRoot, path)
        return file.exists()
    }

    override fun listFiles(projectId: String, path: String): Result<List<File>> = runCatching {
        val projectRoot = getProjectRoot(projectId)
        val directory = File(projectRoot, path)

        if (!directory.exists() || !directory.isDirectory) {
            throw IOException("Directory not found or not a directory: ${directory.absolutePath}")
        }
        directory.listFiles()?.toList() ?: emptyList()
    }
}
