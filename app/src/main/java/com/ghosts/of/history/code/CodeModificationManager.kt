package com.ghosts.of.history.code

import android.util.Log
import com.ghosts.of.history.parser.DraftFileParser
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CodeModificationManager @Inject constructor(
    private val draftFileParser: DraftFileParser
) {

    fun applyEdit(file: File, instruction: String) {
        Log.d("CodeModificationManager", "Applying edit to file: ${file.absolutePath} with instruction: $instruction")

        val fileContent = file.readText()
        val draftFile = draftFileParser.parse(fileContent)

        Log.d("CodeModificationManager", "Parsed file: ${file.absolutePath}, sections: ${draftFile.sections.size}")
    }
}
