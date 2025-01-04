package com.example.managementapp.presentation.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.managementapp.presentation.screens.project.ProjectListScreen
import com.example.managementapp.presentation.screens.projectdetails.ProjectDetailsScreen

@Composable
fun ProjectGraph(
    modifier: Modifier = Modifier,
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.ProjectList.route
    ) {
        composable(Screen.ProjectList.route) {
            ProjectListScreen(navController = navController)
        }

        composable(
            route = "project_details/{owner}/{repo}",
            arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("repo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val repo = backStackEntry.arguments?.getString("repo") ?: ""
            ProjectDetailsScreen(owner = owner, repo = repo)
        }

    }
}

