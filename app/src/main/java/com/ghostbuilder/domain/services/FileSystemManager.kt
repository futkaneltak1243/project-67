package com.ghostbuilder.domain.services

import com.ghostbuilder.domain.models.ProjectState
import java.io.File

interface FileSystemManager {
    suspend fun getProjectIds(): Result<List<String>>
    suspend fun readProjectState(projectId: String): Result<ProjectState>
    suspend fun writeProjectState(projectId: String, projectState: ProjectState): Result<Unit>
    fun getProjectRoot(projectId: String): File
    suspend fun createProjectDirectories(projectId: String): Result<Unit>
    suspend fun copyAssetsToProject(projectId: String, assetsPath: String): Result<Unit>
}
