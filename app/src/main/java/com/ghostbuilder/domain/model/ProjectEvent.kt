package com.ghostbuilder.domain.model

data class ProjectEvent(
    val timestamp: Long,
    val type: String,
    val summary: String,
    val details: Map<String, String> = emptyMap()
)
