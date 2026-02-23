package com.ghostbuilder.domain.service

interface FileSystemManager {
    fun createProjectWorkspace(projectId: String): Result<String>
    fun readFile(filePath: String): Result<String>
    fun writeFile(filePath: String, content: String): Result<Unit>
    fun deleteFile(filePath: String): Result<Unit>
    fun fileExists(filePath: String): Boolean
}
