package com.ghostbuilder.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface AiCommand

@Serializable
data class RequestFiles(
    val reason: String
) : AiCommand

@Serializable
data class WriteFile(
    @SerialName("file_name") val fileName: String,
    val content: String
) : AiCommand

@Serializable
data class EditFile(
    @SerialName("file_name") val fileName: String,
    val description: String
) : AiCommand

@Serializable
data class ReportProgress(
    val summary: String
) : AiCommand
