package io.ssafy.openticon.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.ssafy.openticon.ui.screen.LoginScreen
import io.ssafy.openticon.ui.screen.MainScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(navController)
        }

        composable("login") {
            LoginScreen()
        }
    }
}