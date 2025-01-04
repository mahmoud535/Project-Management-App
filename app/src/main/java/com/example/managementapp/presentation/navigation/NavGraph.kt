package com.example.managementapp.presentation.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.managementapp.presentation.screens.login.LoginScreen

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navigation: () -> Unit,
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                navigation = { navigation.invoke()}
            )
        }
    }
}

