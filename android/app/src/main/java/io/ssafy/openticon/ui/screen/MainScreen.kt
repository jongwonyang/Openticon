package io.ssafy.openticon.ui.screen

import StoreScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import io.ssafy.openticon.ui.component.BottomNavigationBar

@Composable
fun MainScreen(navController: NavController) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedItem,
                onItemSelected = { index -> selectedItem = index }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (selectedItem) {
                0 -> StoreScreen()
                1 -> SearchScreen()
                2 -> MyEmoticonsScreen()
                3 -> ProfileScreen(navController = navController)
            }
        }
    }
}