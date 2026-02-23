package com.ghosts.of.history.domain.models

sealed class DraftSection {
    abstract val content: String

    data class Unchanged(
        override val content: String
    ) : DraftSection()

    data class Modified(
        override val content: String,
        val question: String,
        val answer: String
    ) : DraftSection()

    data class Added(
        override val content: String
    ) : DraftSection()
}
