package com.ghostbuilder.ui.screens.projectdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.Button
import com.ghostbuilder.data.model.Project

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    onBackClick: () -> Unit,
    projectDetailViewModel: ProjectDetailViewModel = viewModel()
) {
    val project by projectDetailViewModel.project.collectAsState()

    ProjectDetailScreenContent(
        project = project,
        onBackClick = onBackClick,
        onAdvanceStageClick = projectDetailViewModel::advanceProjectStage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreenContent(
    project: Project?,
    onBackClick: () -> Unit,
    onAdvanceStageClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(project?.name ?: "Loading Project...") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (project != null) {
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Stage: ${project.stage}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(onClick = onAdvanceStageClick) {
                    Text("Advance Stage")
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}
