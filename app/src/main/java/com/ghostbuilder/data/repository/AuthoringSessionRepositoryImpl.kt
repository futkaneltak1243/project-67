package com.ghostbuilder.data.repository

import android.content.Context
import com.ghostbuilder.domain.models.AuthoringSessionState
import com.ghostbuilder.domain.repository.AuthoringSessionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthoringSessionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json
) : AuthoringSessionRepository {

    private companion object {
        private const val SESSION_FILE_NAME = "authoring_session.json"
    }

    private fun getSessionFile(): File {
        return File(context.cacheDir, SESSION_FILE_NAME)
    }

    override suspend fun saveSession(state: AuthoringSessionState) {
        withContext(Dispatchers.IO) {
            try {
                val jsonString = json.encodeToString(state)
                getSessionFile().writeText(jsonString)
            } catch (e: Exception) {
                // Log error if necessary
                e.printStackTrace()
            }
        }
    }

    override suspend fun loadSession(): AuthoringSessionState? {
        return withContext(Dispatchers.IO) {
            try {
                val sessionFile = getSessionFile()
                if (sessionFile.exists()) {
                    val jsonString = sessionFile.readText()
                    json.decodeFromString<AuthoringSessionState>(jsonString)
                } else {
                    null
                }
            } catch (e: Exception) {
                // Log error if necessary
                e.printStackTrace()
                null
            }
        }
    }

    override suspend fun clearSession() {
        withContext(Dispatchers.IO) {
            try {
                val sessionFile = getSessionFile()
                if (sessionFile.exists()) {
                    sessionFile.delete()
                }
            } catch (e: Exception) {
                // Log error if necessary
                e.printStackTrace()
            }
        }
    }
}
