package com.ghostbuilder.domain.managers

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Singleton
class TTSManager @Inject constructor(
    @ApplicationContext private val context: Context
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    companion object {
        private const val TAG = "TTSManager"
        private const val UTTERANCE_ID = "TTS_UTTERANCE_ID"
    }

    fun initialize() {
        if (tts == null) {
            tts = TextToSpeech(context, this)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "The language (US) is not supported or missing data.")
                isInitialized = false
            } else {
                isInitialized = true
                Log.d(TAG, "TextToSpeech initialized successfully.")
            }
        } else {
            isInitialized = false
            Log.e(TAG, "TextToSpeech initialization failed with status: $status")
        }
    }

    suspend fun speak(text: String) {
        if (!isInitialized) {
            Log.e(TAG, "TTSManager is not initialized. Cannot speak.")
            return
        }

        val deferred = CompletableDeferred<Unit>()

        coroutineScope.launch {
            tts?.apply {
                setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        Log.d(TAG, "Speech started for ID: $utteranceId")
                    }

                    override fun onDone(utteranceId: String?) {
                        Log.d(TAG, "Speech completed for ID: $utteranceId")
                        deferred.complete(Unit)
                    }

                    @Deprecated("Deprecated in API 21")
                    override fun onError(utteranceId: String?) {
                        Log.e(TAG, "Speech error for ID: $utteranceId")
                        deferred.completeExceptionally(RuntimeException("TTS speech error"))
                    }

                    override fun onError(utteranceId: String?, errorCode: Int) {
                        Log.e(TAG, "Speech error for ID: $utteranceId, code: $errorCode")
                        deferred.completeExceptionally(RuntimeException("TTS speech error with code $errorCode"))
                    }
                })
                speak(text, TextToSpeech.QUEUE_FLUSH, null, UTTERANCE_ID)
            } ?: run {
                Log.e(TAG, "TTS instance is null. Cannot speak.")
                deferred.completeExceptionally(IllegalStateException("TTS instance is null"))
            }
        }

        deferred.await()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
        Log.d(TAG, "TextToSpeech shut down.")
    }
}