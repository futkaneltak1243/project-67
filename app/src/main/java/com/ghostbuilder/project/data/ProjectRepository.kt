package com.ghostbuilder.project.data

import android.content.Context
import com.ghostbuilder.project.model.Project
import com.ghostbuilder.project.model.ProjectState
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import java.io.File
import java.io.IOException

class ProjectRepository(private val moshi: Moshi, private val context: Context) {

    // Adapter for ProjectState, as project_state.json is expected to contain ProjectState
    private val projectStateJsonAdapter = moshi.adapter<ProjectState>()

    private fun getProjectStateFile(projectDirectory: File): File {
        return File(projectDirectory, "project_state.json")
    }

    /**
     * Loads a Project by reading and parsing project_state.json from the given directory.
     * Assumes project_state.json contains a ProjectState object, which is then converted to a Project.
     * @param projectDirectory The directory containing the project_state.json file.
     * @return The loaded Project object.
     * @throws IOException if the file does not exist or parsing fails.
     */
    fun loadProject(projectDirectory: File): Project {
        val projectStateFile = getProjectStateFile(projectDirectory)
        if (!projectStateFile.exists()) {
            throw IOException("Project state file not found: ${projectStateFile.absolutePath}")
        }

        val json = projectStateFile.readText()
        val projectState = projectStateJsonAdapter.fromJson(json)
            ?: throw IOException("Failed to parse project_state.json into ProjectState")

        // Assuming Project has a static factory method or constructor to convert from ProjectState.
        // This is necessary to reconcile the requirement to return 'Project' after parsing 'ProjectState'.
        return Project.fromProjectState(projectState)
    }

    /**
     * Serializes and writes the given ProjectState to project_state.json in the specified directory.
     * @param projectDirectory The directory where the project_state.json file should be written.
     * @param projectState The ProjectState object to save.
     * @throws IOException if writing the file fails.
     */
    fun saveProjectState(projectDirectory: File, projectState: ProjectState) {
        val projectStateFile = getProjectStateFile(projectDirectory)
        val json = projectStateJsonAdapter.toJson(projectState)
        projectStateFile.writeText(json)
    }
}
