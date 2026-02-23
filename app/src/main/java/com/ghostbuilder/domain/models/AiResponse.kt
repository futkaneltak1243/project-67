package com.ghostbuilder.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Question(
    @SerialName("question_id") val questionId: String,
    val prompt: String,
    val options: List<String>
)

@Serializable
data class AiResponse(
    @SerialName("schema_version") val schemaVersion: String,
    @SerialName("response_id") val responseId: String,
    val speech: String,
    val question: Question? = null,
    val commands: List<AiCommand>? = null
)
