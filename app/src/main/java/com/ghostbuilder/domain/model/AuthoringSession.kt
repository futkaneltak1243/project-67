package com.ghostbuilder.domain.model

data class AuthoringSession(
    val filePath: String,
    val lastQuestion: String?,
    val draftContent: String?
)
