package com.ghostbuilder.session.model

import java.io.Serializable

data class AuthoringSession(
    val filePath: String,
    val lastQuestion: String,
    val draftContent: String
) : Serializable
