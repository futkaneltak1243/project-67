package com.ghostbuilder.domain.interfaces

import com.ghostbuilder.data.models.ProjectState
import java.io.File

interface IFileSystemManager {
    fun getProjectRoot(projectId: String): File
    fun readFile(projectId: String, filePath: String): String
    fun writeFile(projectId: String, filePath: String, content: String)
    fun readProjectState(projectId: String): ProjectState
    fun writeProjectState(projectId: String, projectState: ProjectState)
}
