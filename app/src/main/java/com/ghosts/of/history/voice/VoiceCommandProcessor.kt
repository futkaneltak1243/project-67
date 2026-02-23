package com.ghosts.of.history.voice

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton
import com.ghosts.of.history.voice.ConfirmationManager
import com.ghosts.of.history.voice.TTSManager
import com.ghosts.of.history.voice.model.PendingAction
import com.ghosts.of.history.utils.FileFinder
import com.ghosts.of.history.code.CodeModificationManager

@Singleton
class VoiceCommandProcessor @Inject constructor(
    private val confirmationManager: ConfirmationManager,
    private val ttsManager: TTSManager,
    private val fileFinder: FileFinder,
    private val codeModificationManager: CodeModificationManager
) {

    fun process(text: String) {
        Log.d("VoiceCommandProcessor", "Received text: $text")
        val normalizedText = text.lowercase().trim()

        when (normalizedText) {
            "confirm", "yes" -> {
                confirmationManager.confirmCurrentAction()
            }
            "cancel", "no" -> {
                confirmationManager.cancelCurrentAction()
                ttsManager.speak("Cancelled")
            }
            else -> {
                confirmationManager.cancelCurrentAction() // Clear any pending actions
                if (normalizedText.startsWith("edit file ")) {
                    val fileName = normalizedText.substring("edit file ".length).trim()
                    when (val result = fileFinder.findFile(fileName)) {
                        is FileFinder.Result.Success -> {
                            val confirmationPrompt = "Did you say edit file ${result.filePath}?"
                            val pendingAction = PendingAction(
                                actionType = "EDIT_FILE",
                                payload = mapOf("file_path" to result.filePath),
                                confirmationPrompt = confirmationPrompt,
                                requiredConfirmations = 1
                            )
                            confirmationManager.setPendingAction(pendingAction)
                            ttsManager.speak(confirmationPrompt)
                        }
                        FileFinder.Result.NotFound -> {
                            ttsManager.speak("File not found.")
                        }
                        FileFinder.Result.MultipleMatches -> {
                            ttsManager.speak("Multiple files found. Please be more specific.")
                        }
                        is FileFinder.Result.Error -> {
                            ttsManager.speak("An error occurred while searching for the file.")
                        }
                    }
                } else {
                    ttsManager.speak("Command not understood.")
                }
            }
        }
    }
}
