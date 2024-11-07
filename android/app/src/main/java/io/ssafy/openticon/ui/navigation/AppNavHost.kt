package io.ssafy.openticon.ui.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.ssafy.openticon.ui.screen.EditProfileScreen
import io.ssafy.openticon.ui.screen.EmoticonAllScreen
import io.ssafy.openticon.ui.screen.EmoticonPackDetailScreen
import io.ssafy.openticon.ui.screen.LoginScreen
import io.ssafy.openticon.ui.screen.LoginSuccessScreen
import io.ssafy.openticon.ui.screen.MainScreen
import io.ssafy.openticon.ui.screen.SettingsScreen

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
            LoginScreen(navController)
        }
        composable("edit_profile") {
            EditProfileScreen(navController)
        }
        composable("settings") {
            SettingsScreen(navController)
        }

        composable(
            route = "emoticonPack/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            if (id != null) {
                EmoticonPackDetailScreen(emoticonPackId = id, navController = navController)
            }
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

        composable(
            route = "emoticonAll/{type}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type")
            EmoticonAllScreen(navController, type, tag = null)
        }

        composable(
            route = "emoticonAlltag/{tag}",
            arguments = listOf(
                navArgument("tag") { type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val tag = backStackEntry.arguments?.getString("tag")
            EmoticonAllScreen(navController, "tag", tag)
        }

    }

    context.intent?.data?.let { uri ->
        if (uri.toString().contains("successLogin")) {
            val accessToken = uri.getQueryParameter("access_token") ?: ""
            navController.navigate("login_success?accessToken=$accessToken")
        }
    }
}