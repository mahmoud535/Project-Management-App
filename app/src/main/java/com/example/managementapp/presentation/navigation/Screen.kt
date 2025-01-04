package com.example.managementapp.presentation.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object ProjectList : Screen("projects")
}