package com.ghostbuilder.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object ProjectsList : Screen("projects_list")
    object ProjectView : Screen("project_view/{projectId}") {
        const val PROJECT_ID_KEY = "projectId"
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.ProjectsList.route
    ) {
        composable(route = Screen.ProjectsList.route) {
            // ProjectsListScreen will go here
            Text(text = "Projects List Screen")
        }
        composable(
            route = Screen.ProjectView.route,
            arguments = listOf(
                navArgument(Screen.ProjectView.PROJECT_ID_KEY) {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString(Screen.ProjectView.PROJECT_ID_KEY)
            // ProjectViewScreen will go here
            Text(text = "Project View Screen for ID: $projectId")
        }
    }
}
