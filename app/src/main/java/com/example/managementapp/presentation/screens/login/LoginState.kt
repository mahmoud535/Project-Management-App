package com.example.managementapp.presentation.screens.login

data class LoginState(
    val username: String = "",
    val token: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null
)