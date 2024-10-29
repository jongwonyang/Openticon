package io.ssafy.openticon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import io.ssafy.openticon.ui.navigation.AppNavHost
import io.ssafy.openticon.ui.theme.OpenticonTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenticonTheme {
                AppNavHost()
            }
        }
    }
}
