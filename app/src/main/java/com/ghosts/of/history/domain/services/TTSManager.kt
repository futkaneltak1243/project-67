package com.ghosts.of.history.domain.services

interface TTSManager {
    suspend fun speak(text: String)
    fun shutdown()
}
