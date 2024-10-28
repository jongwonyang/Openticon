package io.ssafy.openticon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import io.ssafy.openticon.ui.navigation.BottomNavigationBar
import io.ssafy.openticon.ui.navigation.BottomNavigationViewModel
import io.ssafy.openticon.ui.screen.MyEmoticonsScreen
import io.ssafy.openticon.ui.screen.ProfileScreen
import io.ssafy.openticon.ui.screen.SearchScreen
import io.ssafy.openticon.ui.screen.StoreScreen
import io.ssafy.openticon.ui.theme.OpenticonTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: BottomNavigationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenticonTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(
                            selectedItem = viewModel.selectedItem.collectAsState().value,
                            onItemSelected = { viewModel.onItemSelected(it) }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        val selectedItem by viewModel.selectedItem.collectAsState()
                        when (selectedItem) {
                            0 -> StoreScreen()
                            1 -> SearchScreen()
                            2 -> MyEmoticonsScreen()
                            3 -> ProfileScreen()
                        }
                    }
                }
            }
        }
    }
}
