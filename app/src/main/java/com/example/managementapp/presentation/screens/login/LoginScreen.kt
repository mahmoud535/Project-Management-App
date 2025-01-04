package com.example.managementapp.presentation.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel(),
    navigation: () -> Unit,
) {
    val state = viewModel.state.collectAsState().value

    Box(modifier = Modifier.fillMaxSize()) {
        LoginView(
            username = state.username,
            token = state.token,
            isLoading = state.isLoading,
            error = state.error,
            onUsernameChanged = { viewModel.onEvent(LoginEvent.UsernameChanged(it)) },
            onTokenChanged = { viewModel.onEvent(LoginEvent.TokenChanged(it)) },
            onLoginClick = { viewModel.onEvent(LoginEvent.Login) }
        )

        if (state.isLoggedIn) {
            navigation.invoke()
        }
    }
}