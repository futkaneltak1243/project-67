package com.ghosts.of.history.domain.services

import com.ghosts.of.history.domain.models.DraftFile

interface DraftManager {
    fun parse(filePath: String, content: String): DraftFile
}
