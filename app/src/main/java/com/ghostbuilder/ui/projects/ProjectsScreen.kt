package com.ghostbuilder.ui.projects

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Assuming ProjectsViewModel and Project data class are defined elsewhere
// For example:
// data class Project(val id: String, val name: String)
// class ProjectsViewModel : ViewModel() { /* ... */ }

@Composable
fun ProjectsScreen(viewModel: ProjectsViewModel) {
    val projects by viewModel.projects.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(projects) {
            project ->
            Row(modifier = Modifier.padding(16.dp)) {
                Text(text = project.name)
            }
        }
    }
}
