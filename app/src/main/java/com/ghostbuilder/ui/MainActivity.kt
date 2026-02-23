package com.ghostbuilder.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import com.ghostbuilder.ui.components.VoiceCommandBar
import com.ghostbuilder.ui.components.VoiceCommandViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import com.ghostbuilder.ui.screens.CreateProjectScreen
import com.ghostbuilder.ui.screens.ProjectListScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.ghostbuilder.ui.screens.ProjectDetailScreen
import com.ghostbuilder.ui.screens.ProjectDetailViewModel
import com.ghostbuilder.domain.models.Project
import com.ghostbuilder.ui.screens.ProjectListViewModel
import com.ghostbuilder.ui.theme.GhostBuilderTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GhostBuilderTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    bottomBar = { VoiceCommandBar(isListening = isListening, onToggleListening = voiceCommandViewModel::toggleListening) }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        val navController = rememberNavController()
                    val projectListViewModel: ProjectListViewModel = hiltViewModel()
                    val voiceCommandViewModel: VoiceCommandViewModel = hiltViewModel()
                    val isListening by voiceCommandViewModel.isListening.collectAsStateWithLifecycle()
                    NavHost(navController = navController, startDestination = "project_list") {
                        composable("project_list") {
                            ProjectListScreen(
                                onNewProjectClick = {
                                    navController.navigate("create_project")
                                },
                                onProjectClick = { project: Project ->
                                    navController.navigate("project_detail/${project.id}")
                                },
                                viewModel = projectListViewModel
                            )
                        }
                        composable("create_project") {
                            CreateProjectScreen(
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onCreateClick = { projectName, projectDescription ->
                                    projectListViewModel.addProject(projectName, projectDescription)
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(
                            route = "project_detail/{projectId}",
                            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val projectId = backStackEntry.arguments?.getString("projectId")
                            val projectDetailViewModel: ProjectDetailViewModel = hiltViewModel()
                            ProjectDetailScreen(
                                projectId = projectId,
                                viewModel = projectDetailViewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                } /* Closing Box */
            }
        }
    }
}

