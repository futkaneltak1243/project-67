package com.ghostbuilder.domain.model

import java.util.UUID

data class Project(
    val id: UUID,
    val name: String,
    val local_path: String,
    val github_repo_url: String,
    val project_state: ProjectState
)
