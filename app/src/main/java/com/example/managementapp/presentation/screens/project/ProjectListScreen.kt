package com.example.managementapp.presentation.screens.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun ProjectListScreen(
    navController: NavHostController,
    viewModel: ProjectListViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value

    ProjectListView(
        projects = state.projects ?: emptyList(),
        isLoading = state.isLoading,
        error = state.error,
        onProjectClick = { project ->
            navController.navigate("project_details/${project.owner?.login}/${project.name}")
        }
    )
}
