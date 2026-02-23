package com.ghosts.of.history.code

import com.ghosts.of.history.data.models.DraftFile
import com.ghosts.of.history.data.models.DraftSection
import com.ghosts.of.history.data.models.DraftSectionStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DraftFileParser @Inject constructor() {

    fun parse(fileContent: String, originalPath: String): DraftFile {
        val section = DraftSection(
            content = fileContent,
            status = DraftSectionStatus.Original
        )
        return DraftFile(
            originalPath = originalPath,
            sections = listOf(section)
        )
    }
}
