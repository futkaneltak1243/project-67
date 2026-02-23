package com.ghostbuilder.data.services

import com.ghostbuilder.domain.model.Command
import com.ghostbuilder.domain.model.CreateFileCommand
import com.ghostbuilder.domain.model.AddFunctionCommand
import com.ghostbuilder.domain.model.EditFileCommand
import com.ghostbuilder.domain.model.UnknownCommand
import com.ghostbuilder.domain.model.NavigateCommand
import com.ghostbuilder.domain.services.VoiceCommandParser
import javax.inject.Inject

class VoiceCommandParserImpl @Inject constructor() : VoiceCommandParser {

    override fun parse(voiceInput: String): Command {
        val lowerCaseInput = voiceInput.lowercase()
        val createFilePrefix = "create file "
        val editFilePrefix = "edit file "
        val addFunctionPrefix = "add function "
        val andSeparator = " and "

        return when {
            lowerCaseInput.startsWith(editFilePrefix) && lowerCaseInput.contains(andSeparator) -> {
                val contentAfterPrefix = voiceInput.substring(editFilePrefix.length)
                val andIndex = contentAfterPrefix.indexOf(andSeparator)

                val baseFileName = contentAfterPrefix.substring(0, andIndex).trim()
                val fileName = "$baseFileName.kt"
                val changeDescription = contentAfterPrefix.substring(andIndex + andSeparator.length).trim()
                EditFileCommand(fileName, changeDescription)
            }
            lowerCaseInput.startsWith(addFunctionPrefix) -> {
                val addFunctionRegex = Regex("add function (.+) returning (.+)", RegexOption.IGNORE_CASE)
                val matchResult = addFunctionRegex.matchEntire(voiceInput)
                if (matchResult != null) {
                    val (functionName, returnType) = matchResult.destructured
                    AddFunctionCommand(functionName.trim(), returnType.trim(), emptyList())
                } else {
                    UnknownCommand(voiceInput)
                }
            }
            lowerCaseInput.startsWith("navigate to") || lowerCaseInput.startsWith("open") -> {
                val navigateRegex = Regex("(?:navigate to|open) (.+)", RegexOption.IGNORE_CASE)
                val matchResult = navigateRegex.matchEntire(lowerCaseInput)
                if (matchResult != null) {
                    val fileName = matchResult.groupValues[1].trim()
                    NavigateCommand(fileName)
                } else {
                    UnknownCommand(voiceInput)
                }
            }
            else -> {
                if (lowerCaseInput.startsWith(createFilePrefix)) {
                    val fileName = voiceInput.substring(createFilePrefix.length).trim()
                    CreateFileCommand(fileName)
                } else {
                    UnknownCommand(voiceInput)
                }
            }
        }
    }
}
