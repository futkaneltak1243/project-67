package com.ghosts.of.history.data.filesystem

import java.io.File

interface FileSystemManager {
    fun getProjectRoot(projectId: String): File
    fun readFile(projectId: String, path: String): Result<String>
    fun writeFile(projectId: String, path: String, content: String): Result<Unit>
    fun fileExists(projectId: String, path: String): Boolean
    fun listFiles(projectId: String, path: String): Result<List<File>>
}
