package com.ghostbuilder.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import com.ghostbuilder.domain.model.ProjectState
import com.ghostbuilder.ui.main.MainViewModel
import com.ghostbuilder.ui.main.ProjectUiState
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.uiState.collectAsState()
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (uiState) {
                        is ProjectUiState.Loading -> LoadingScreen()
                        is ProjectUiState.NoProject -> NoProjectScreen(onCreateProject = { viewModel.createProject("New GhostBuilder Project") }

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun NoProjectScreen(onCreateProject: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No project found.")
            Button(onClick = onCreateProject) {
                Text("Create New Project")
            }
        }
    }
}

@Composable
fun ProjectLoadedScreen(projectState: ProjectState) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Project: ${projectState.projectName}")
            Text("Stage: ${projectState.currentStage}")
        }
    }
})
                        is ProjectUiState.Loaded -> ProjectLoadedScreen(projectState = (uiState as ProjectUiState.Loaded).projectState)
                    }
                }
            }
        }
    }
}
