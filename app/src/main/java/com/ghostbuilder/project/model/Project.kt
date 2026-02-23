package com.ghostbuilder.project.model

data class Project(
    val id: String,
    val name: String,
    val slug: String,
    val githubRepoUrl: String,
    val localPath: String,
    val projectState: ProjectState
)
