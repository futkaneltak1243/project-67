package com.ghosts.of.history.voice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.ghosts.of.history.R
import com.ghosts.of.history.voice.TTSManager
import com.ghosts.of.history.voice.VoiceCommandProcessor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VoiceInputService : Service(), RecognitionListener {

    @Inject lateinit var ttsManager: TTSManager
    @Inject lateinit var voiceCommandProcessor: VoiceCommandProcessor

    private var speechRecognizer: SpeechRecognizer? = null

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "voice_input_channel"
        const val NOTIFICATION_CHANNEL_NAME = "Voice Input"
        const val NOTIFICATION_ID = 1
        const val ACTION_START_LISTENING = "com.ghosts.of.history.voice.ACTION_START_LISTENING"
        private fun startListening() {
        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        speechRecognizer?.startListening(recognizerIntent)
        Log.d("VoiceInputService", "Started listening...")
    }

    override fun onReadyForSpeech(params: Bundle?) {
        Log.d("VoiceInputService", "onReadyForSpeech")
    }

    override fun onBeginningOfSpeech() {
        Log.d("VoiceInputService", "onBeginningOfSpeech")
    }

    override fun onRmsChanged(rmsdB: Float) {
        // Log.d("VoiceInputService", "onRmsChanged: $rmsdB") // Can be noisy
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        Log.d("VoiceInputService", "onBufferReceived")
    }

    override fun onEndOfSpeech() {
        Log.d("VoiceInputService", "onEndOfSpeech")
    }

    override fun onError(error: Int) {
        val errorMessage = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Other client side errors"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network related errors"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network operation timed out"
            SpeechRecognizer.ERROR_NO_MATCH -> "No recognition result matched"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
            SpeechRecognizer.ERROR_SERVER -> "Server sends error status"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Unknown error"
        }
        Log.e("VoiceInputService", "onError: $errorMessage ($error)")
        if (error == SpeechRecognizer.ERROR_NO_MATCH || error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
            startListening() // Try again if no match or speech timeout
        }
    }

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (!matches.isNullOrEmpty()) {
            val recognizedText = matches[0]
            Log.d("VoiceInputService", "onResults: $recognizedText")
            voiceCommandProcessor.process(recognizedText)
        }
        startListening() // Continue listening
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (!matches.isNullOrEmpty()) {
            Log.d("VoiceInputService", "onPartialResults: ${matches[0]}")
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        Log.d("VoiceInputService", "onEvent: $eventType")
    }
}

    override fun onCreate() {
        super.onCreate()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer?.setRecognitionListener(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = buildNotification()
        startForeground(NOTIFICATION_ID, notification)

        if (intent?.action == ACTION_START_LISTENING) {
            startListening()
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer?.destroy()
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Placeholder icon
            .setContentTitle("GhostBuilder Active")
            .setContentText("Listening for commands...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
}
