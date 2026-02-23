package com.ghosts.of.history.data.models

data class DraftSection(
    val content: String,
    val status: DraftSectionStatus
)

sealed class DraftSectionStatus {
    object Unchanged : DraftSectionStatus()
    object Added : DraftSectionStatus()
    object Modified : DraftSectionStatus()
}
