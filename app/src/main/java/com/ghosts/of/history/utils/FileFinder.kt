package com.ghosts.of.history.utils

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileFinder @Inject constructor() {

    sealed interface FileSearchResult

    data class SingleMatch(val filePath: String) : FileSearchResult
    data class MultipleMatches(val filePaths: List<String>) : FileSearchResult
    object NoMatch : FileSearchResult

    fun findFile(fileNameQuery: String, sourceFiles: List<String>): FileSearchResult {
        val matches = sourceFiles.filter { filePath ->
            filePath.endsWith(fileNameQuery, ignoreCase = true)
        }

        return when (matches.size) {
            0 -> NoMatch
            1 -> SingleMatch(matches.first())
            else -> MultipleMatches(matches)
        }
    }
}
