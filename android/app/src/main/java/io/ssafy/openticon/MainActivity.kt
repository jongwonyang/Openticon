package io.ssafy.openticon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.ssafy.openticon.ui.navigation.AppNavHost
import io.ssafy.openticon.ui.component.BottomNavigationBar
import io.ssafy.openticon.ui.theme.OpenticonTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenticonTheme {
                val navController = rememberNavController()
                val showBottomBar = remember { mutableStateOf(true) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar.value) {
                            BottomNavigationBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AppNavHost(
                            navController = navController,
                            setShowBottomBar = { showBottomBar.value = it }
                        )
                    }
                }
            }
        }
    }
}
