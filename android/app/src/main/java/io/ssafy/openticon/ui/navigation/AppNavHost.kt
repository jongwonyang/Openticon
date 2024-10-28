package io.ssafy.openticon.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.ssafy.openticon.ui.screen.LoginScreen
import io.ssafy.openticon.ui.screen.MyEmoticonsScreen
import io.ssafy.openticon.ui.screen.ProfileScreen
import io.ssafy.openticon.ui.screen.SearchScreen
import io.ssafy.openticon.ui.screen.StoreScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    setShowBottomBar: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Store.route,
    ) {
        composable(BottomNavItem.Store.route) {
            setShowBottomBar(true)
            StoreScreen()
        }
        composable(BottomNavItem.Search.route) {
            setShowBottomBar(true)
            SearchScreen()
        }
        composable(BottomNavItem.MyEmoticons.route) {
            setShowBottomBar(true)
            MyEmoticonsScreen()
        }
        composable(BottomNavItem.Profile.route) {
            setShowBottomBar(true)
            ProfileScreen(navController = navController)
        }
        composable("login") {
            setShowBottomBar(false)
            LoginScreen()
        }
    }
}