package com.ghostbuilder.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthoringSessionState(
    val projectId: String,
    val filePath: String,
    val lastQuestion: String? = null,
    val draftContent: String? = null
)