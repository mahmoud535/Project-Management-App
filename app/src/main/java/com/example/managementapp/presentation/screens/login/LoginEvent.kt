package com.example.managementapp.presentation.screens.login

sealed class LoginEvent {
    data class UsernameChanged(val username: String) : LoginEvent()
    data class TokenChanged(val token: String) : LoginEvent()
    data object Login : LoginEvent()
}