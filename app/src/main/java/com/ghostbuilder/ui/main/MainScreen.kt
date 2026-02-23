package com.ghostbuilder.ui.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

// MainViewModel is expected to be defined elsewhere.
// For now, this Composable accepts it as a parameter.
@Composable
fun MainScreen(viewModel: MainViewModel) {
    Text(text = "Project List")
}
