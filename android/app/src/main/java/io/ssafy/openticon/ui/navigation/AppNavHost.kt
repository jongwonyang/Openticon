package io.ssafy.openticon.ui.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.ssafy.openticon.ui.screen.LoginScreen
import io.ssafy.openticon.ui.screen.MainScreen
import io.ssafy.openticon.ui.screen.LoginSuccessScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current as Activity

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
        composable(
            route = "login_success?accessToken={accessToken}&refreshToken={refreshToken}",
            arguments = listOf(
                navArgument("accessToken") { defaultValue = "" },
            )
        ) { backStackEntry ->
            val accessToken = backStackEntry.arguments?.getString("accessToken") ?: ""
            LoginSuccessScreen(accessToken, navController)
        }
    }

    context.intent?.data?.let { uri ->
        if (uri.toString().contains("successLogin")) {
            val accessToken = uri.getQueryParameter("access_token") ?: ""
            navController.navigate("login_success?accessToken=$accessToken")
        }
    }
}