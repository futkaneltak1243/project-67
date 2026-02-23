package com.ghostbuilder.data.repository

import android.content.Context
import com.ghostbuilder.domain.repository.ProjectFilesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ProjectFilesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ProjectFilesRepository {

    override suspend fun getSeedFiles(projectId: String): List<String> = withContext(Dispatchers.IO) {
        val projectDir = File(context.filesDir, projectId)
        val seedDir = File(projectDir, "seed")

        if (seedDir.exists() && seedDir.isDirectory) {
            seedDir.listFiles()?.map { it.name } ?: emptyList()
        } else {
            emptyList()
        }
    }
}
