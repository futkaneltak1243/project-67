package com.ghostbuilder.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VoiceCommandBar(modifier: Modifier = Modifier, isListening: Boolean, onToggleListening: () -> Unit) {
    Surface(
        modifier = modifier,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onToggleListening) {
                if (isListening) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = "Pause Button"
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Button"
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Now Playing")
                Text(text = "Project Name")
            }

@Preview
@Composable
fun VoiceCommandBarPreview() {
    VoiceCommandBar(isListening = true, onToggleListening = {})
}

        }
    }
}
