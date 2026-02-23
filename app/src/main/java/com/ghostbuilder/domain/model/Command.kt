package com.ghostbuilder.domain.model

sealed interface Command

data class CreateFileCommand(val fileName: String) : Command
data class EditFileCommand(val fileName: String) : Command
data class AddFunctionCommand(
    val functionName: String,
    val parameters: List<String>,
    val returnType: String
) : Command
data class NavigateCommand(val destination: String) : Command
data class UnknownCommand(val rawTranscript: String) : Command
