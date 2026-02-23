package com.ghostbuilder.ui.screens.nowplaying

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ghostbuilder.ui.theme.GhostBuilderTheme

// Placeholder ViewModel for demonstration purposes
class NowPlayingViewModel : ViewModel() {
    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    val listeningStatus: StateFlow<String> = isListening.map { isListening ->
        if (isListening) "Listening..." else "Not Listening"
    }.asStateFlow()

    fun toggleListeningState() {
        _isListening.value = !_isListening.value
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceCommandScreen(viewModel: NowPlayingViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Now Playing") }
            )
        }
    ) { paddingValues ->
        val isListening by viewModel.isListening.collectAsStateWithLifecycle()
        val listeningStatus by viewModel.listeningStatus.collectAsStateWithLifecycle()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = listeningStatus)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.toggleListeningState() }) {
                Text(if (isListening) "Stop Listening" else "Start Listening")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NowPlayingScreenPreview() {
    GhostBuilderTheme {
        VoiceCommandScreen(viewModel = NowPlayingViewModel())
    }
}
