package com.ghostbuilder.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ProjectState(
    val pipelineStage: String = "",
    val fileStatuses: Map<String, String> = emptyMap()
)
