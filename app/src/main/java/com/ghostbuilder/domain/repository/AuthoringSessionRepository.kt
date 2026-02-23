package com.ghostbuilder.domain.repository

import com.ghostbuilder.domain.models.AuthoringSessionState

interface AuthoringSessionRepository {
    suspend fun saveSession(state: AuthoringSessionState)
    suspend fun loadSession(): AuthoringSessionState?
    suspend fun clearSession()
}