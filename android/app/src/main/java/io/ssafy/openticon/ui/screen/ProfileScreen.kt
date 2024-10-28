package io.ssafy.openticon.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun ProfileScreen(
    navController: NavController
) {
    Column {
        Text("Profile Screen")
        Button(
            onClick = {
                navController.navigate("login")
            }
        ) {
            Text("Go to login")
        }
    }
}