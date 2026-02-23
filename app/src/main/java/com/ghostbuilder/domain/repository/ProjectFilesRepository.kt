package com.ghostbuilder.domain.repository

import java.io.File

interface ProjectFilesRepository {
    suspend fun getSeedFiles(projectId: String): List<File>
}
