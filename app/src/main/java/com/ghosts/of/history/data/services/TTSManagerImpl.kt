package com.ghosts.of.history.data.services

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import com.ghosts.of.history.domain.services.TTSManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TTSManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TTSManager, TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var isTtsInitialized = false

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTSManagerImpl", "The language specified is not supported or missing data.")
            } else {
                isTtsInitialized = true
                Log.d("TTSManagerImpl", "TTS initialized successfully with Locale.US")
            }
        } else {
            Log.e("TTSManagerImpl", "TTS Initialization failed with status: $status")
        }
    }

    override suspend fun speak(text: String) {
        if (isTtsInitialized) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            Log.w("TTSManagerImpl", "TTS not initialized, cannot speak: $text")
        }
    }

    override fun shutdown() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
            isTtsInitialized = false
            Log.d("TTSManagerImpl", "TTS shut down.")
        }
    }
}
