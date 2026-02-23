package com.ghosts.of.history.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TTSManager @Inject constructor(
    @ApplicationContext private val context: Context
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized: Boolean = false

    private val TAG = "TTSManager"

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "The language specified (Locale.US) is not supported.")
            } else {
                isInitialized = true
                Log.d(TAG, "TTSManager initialized successfully with Locale.US")
            }
        } else {
            Log.e(TAG, "TTSManager initialization failed with status: $status")
        }
    }

    fun speak(text: String) {
        if (isInitialized) {
            tts?.speak(text, TextToSpeech.QUEUE_ADD, null, null)
            Log.d(TAG, "Speaking: \"$text\"")
        } else {
            Log.w(TAG, "TTSManager is not ready. Cannot speak: \"$text\"")
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        isInitialized = false
        Log.d(TAG, "TTSManager shut down.")
    }
}