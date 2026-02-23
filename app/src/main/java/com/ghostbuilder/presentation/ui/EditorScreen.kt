package com.ghostbuilder.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.hilt.navigation.compose.hiltViewModel
import com.ghostbuilder.presentation.EditorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    viewModel: EditorViewModel = hiltViewModel()
) {
    val fileName by viewModel.fileName.collectAsStateWithLifecycle()
    val fileContent by viewModel.fileContent.collectAsStateWithLifecycle()

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    ?.let { results ->
                        results.firstOrNull()
                    }
            spokenText?.let {
                viewModel.processVoiceCommand(it)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(fileName) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { speechRecognizerLauncher.launch(createSpeechRecognizerIntent()) }
            ) {
                Icon(Icons.Filled.Mic, "Voice Command")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TextField(
                value = fileContent,
                onValueChange = { /* TODO: Implement content update */ },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun createSpeechRecognizerIntent(): Intent {
    return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak a command...")
    }
}

@Preview(showBackground = true)
@Composable
fun EditorScreenPreview() {
    // For preview purposes, we might need a mock ViewModel or a simple placeholder.
    // Since hiltViewModel() cannot be used directly in previews without Hilt setup,
    // we'll just show the UI structure.
    // In a real app, you'd provide a mock ViewModel.
    EditorScreen(viewModel = object : EditorViewModel() {
        override fun processVoiceCommand(command: String) {
            // Do nothing for preview
        }
    })
}
