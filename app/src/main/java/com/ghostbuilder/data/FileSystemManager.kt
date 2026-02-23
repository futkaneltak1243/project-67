package com.ghostbuilder.data

import android.content.Context
import com.ghostbuilder.domain.interfaces.IFileSystemManager
import com.ghostbuilder.data.models.ProjectState
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import javax.inject.Inject
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.SerializationException

class FileSystemManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json
) : IFileSystemManager {

    private val projectsBaseDir: File
        get() = File(context.filesDir, "projects")

    override fun getProjectRoot(projectId: String): File {
        return File(projectsBaseDir, projectId)
    }

    override fun getProjectState(projectId: String): Result<ProjectState> {
        return try {
            val projectRoot = getProjectRoot(projectId)
            val stateFile = File(projectRoot, "project_state.json")

            if (!stateFile.exists()) {
                return Result.failure(IOException("Project state file not found for project $projectId"))
            }

            val content = stateFile.readText()
            val projectState = json.decodeFromString<ProjectState>(content)
            Result.success(projectState)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: SerializationException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun saveProjectState(projectId: String, state: ProjectState): Result<Unit> {
        return try {
            val projectRoot = getProjectRoot(projectId)
            projectRoot.mkdirs() // Ensure project root exists
            val stateFile = File(projectRoot, "project_state.json")

            val content = json.encodeToString(state)
            stateFile.writeText(content)
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: SerializationException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun readFile(projectId: String, relativePath: String): Result<String> {
        return try {
            val projectRoot = getProjectRoot(projectId)
            val file = File(projectRoot, relativePath)

            if (!file.exists() || !file.isFile) {
                return Result.failure(IOException("File not found or is not a file: $relativePath in project $projectId"))
            }

            Result.success(file.readText())
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun writeFile(projectId: String, relativePath: String, content: String): Result<Unit> {
        return try {
            val projectRoot = getProjectRoot(projectId)
            val file = File(projectRoot, relativePath)

            file.parentFile?.mkdirs() // Ensure parent directories exist
            file.writeText(content)
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getProjectStructure(projectId: String): Result<Map<String, List<String>>> {
        return try {
            val projectRoot = getProjectRoot(projectId)
            if (!projectRoot.exists() || !projectRoot.isDirectory) {
                return Result.failure(IOException("Project directory not found or is not a directory: $projectId"))
            }

            val structure = mutableMapOf<String, MutableList<String>>()
            val excludedDirs = setOf("logs", "transcripts")

            // Add the root directory itself as a key, even if empty, to represent the project base
            structure[""] = mutableListOf()

            projectRoot.walkTopDown().forEach { file ->
                if (file == projectRoot) return@forEach // Skip the root itself

                val relativePath = file.toRelativeString(projectRoot)
                val parentRelativePath = file.parentFile?.toRelativeString(projectRoot) ?: ""

                // Check if any part of the path (from the root down) is an excluded directory
                val pathSegments = relativePath.split(File.separator)
                if (pathSegments.any { it in excludedDirs }) {
                    return@forEach
                }

                if (file.isDirectory) {
                    // Add directory to map if it's not already there, with an empty list
                    // This ensures empty directories are represented.
                    structure.computeIfAbsent(relativePath) { mutableListOf() }
                } else if (file.isFile) {
                    // Add file to its parent directory's list
                    structure.computeIfAbsent(parentRelativePath) { mutableListOf() }.add(file.name)
                }
            }

            // Sort files within each directory for consistent output
            structure.forEach { (_, files) -> files.sort() }

            Result.success(structure)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
