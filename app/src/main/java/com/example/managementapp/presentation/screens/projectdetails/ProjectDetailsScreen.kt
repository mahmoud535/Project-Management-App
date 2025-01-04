package com.example.managementapp.presentation.screens.projectdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.managementapp.presentation.components.ErrorMessage

@Composable
fun ProjectDetailsScreen(
    owner: String,
    repo: String,
    viewModel: ProjectDetailsViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value

    if (state.project == null) {
        viewModel.loadProjectDetails(owner, repo)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            state.error != null -> {
                ErrorMessage(
                    message = state.error,
                    onRetry = { viewModel.loadProjectDetails(owner, repo) }
                )
            }
            state.project != null -> {
                ProjectDetailsView(project = state.project, isLoading = state.isLoading, error = state.error)
            }
        }
    }
}