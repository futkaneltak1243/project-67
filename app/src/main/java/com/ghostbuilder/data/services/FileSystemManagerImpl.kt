package com.ghostbuilder.data.services

import android.content.Context
import com.ghostbuilder.domain.models.ProjectState
import com.ghostbuilder.domain.services.FileSystemManager
import com.google.gson.Gson
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.FileOutputStream
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Singleton
class FileSystemManagerImpl @Inject constructor(
    private val context: Context,
    private val gson: Gson
) : FileSystemManager {

    private val projectsDir: File by lazy {
        File(context.filesDir, "projects").apply { mkdirs() }
    }

    override suspend fun getProjectIds(): Result<List<String>> {
        return try {
            val ids = projectsDir.listFiles { file -> file.isDirectory }?.map { it.name } ?: emptyList()
            Result.success(ids)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getProjectRoot(projectId: String): File {
        return File(projectsDir, projectId).apply { mkdirs() }
    }

    override suspend fun readProjectState(projectId: String): Result<ProjectState> {
        return try {
            val projectRoot = getProjectRoot(projectId)
            val stateFile = File(projectRoot, "project_state.json")
            if (!stateFile.exists()) {
                return Result.failure(FileNotFoundException("Project state file not found for project $projectId"))
            }
            val json = stateFile.readText()
            val projectState = gson.fromJson(json, ProjectState::class.java)
            Result.success(projectState)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun writeProjectState(projectId: String, projectState: ProjectState): Result<Unit> {
        return try {
            val projectRoot = getProjectRoot(projectId)
            val stateFile = File(projectRoot, "project_state.json")
            val json = gson.toJson(projectState)
            stateFile.writeText(json)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createProjectDirectories(projectId: String): Result<Unit> {
        return try {
            val projectRoot = getProjectRoot(projectId)
            projectRoot.mkdirs()

            val requiredSubdirectories = listOf("app", "brain", "seed", "walkthrough", "logs", "transcripts")
            requiredSubdirectories.forEach { dirName ->
                File(projectRoot, dirName).mkdir()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getProjectFile(projectId: String, relativePath: String): File {
        val projectRoot = getProjectRoot(projectId)
        return File(projectRoot, relativePath)
    }

    fun readFile(projectId: String, relativePath: String): Result<String> {
        return try {
            val file = getProjectFile(projectId, relativePath)
            if (!file.exists() || !file.isFile) {
                return Result.failure(FileNotFoundException("File not found: $relativePath in project $projectId"))
            }
            Result.success(file.readText())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun writeFile(projectId: String, relativePath: String, content: String): Result<Unit> {
        return try {
            val file = getProjectFile(projectId, relativePath)
            file.parentFile?.mkdirs() // Ensure parent directories exist
            file.writeText(content)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun deleteFile(projectId: String, relativePath: String): Result<Unit> {
        return try {
            val file = getProjectFile(projectId, relativePath)
            if (file.exists()) {
                if (file.delete()) {
                    Result.success(Unit)
                } else {
                    Result.failure(IOException("Failed to delete file: $relativePath in project $projectId"))
                }
            } else {
                Result.success(Unit) // File doesn't exist, consider it deleted
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getBrainIndex(projectId: String): Result<List<String>> {
        return try {
            val brainDir = getProjectFile(projectId, "brain")
            if (!brainDir.exists() || !brainDir.isDirectory) {
                return Result.success(emptyList()) // Brain directory doesn't exist or is not a directory
            }
            val fileNames = brainDir.listFiles()?.filter { it.isFile }?.map { it.name } ?: emptyList()
            Result.success(fileNames)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getSourceManifest(projectId: String): Result<List<String>> {
        return try {
            val appSrcDir = getProjectFile(projectId, "app/src")
            if (!appSrcDir.exists() || !appSrcDir.isDirectory) {
                return Result.success(emptyList()) // app/src directory doesn't exist or is not a directory
            }

            val filePaths = mutableListOf<String>()
            appSrcDir.walkTopDown()
                .filter { it.isFile }
                .forEach { file ->
                    val relativePath = file.toRelativeString(appSrcDir)
                    filePaths.add(relativePath)
                }
            Result.success(filePaths)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun copyAssetsToProject(projectId: String, assetsPath: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val projectSeedDir = File(getProjectRoot(projectId), "seed").apply { mkdirs() }

            fun copyAsset(currentAssetPath: String, targetDir: File) {
                val assetList = context.assets.list(currentAssetPath)

                if (assetList == null) {
                    // It's a file
                    val fileName = currentAssetPath.substringAfterLast('/')
                    val targetFile = File(targetDir, fileName)
                    context.assets.open(currentAssetPath).use { inputStream ->
                        FileOutputStream(targetFile).use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                } else {
                    // It's a directory (could be empty)
                    val dirName = currentAssetPath.substringAfterLast('/')
                    val newTargetDir = if (dirName.isEmpty()) targetDir else File(targetDir, dirName)
                    newTargetDir.mkdirs()

                    assetList.forEach { assetFileName ->
                        copyAsset("$currentAssetPath/$assetFileName", newTargetDir)
                    }
                }
            }

            // Initial call to the helper function
            copyAsset(assetsPath, projectSeedDir)
            Unit // Return Unit for success
        }
    }
}
