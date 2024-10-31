package io.ssafy.openticon

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.ssafy.openticon.data.local.TokenDataSource
import io.ssafy.openticon.data.model.EmoticonPack
import io.ssafy.openticon.ui.navigation.AppNavHost
import io.ssafy.openticon.ui.theme.OpenticonTheme
import io.ssafy.openticon.ui.viewmodel.EmoticonViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val myViewModel: EmoticonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenDataSource.initialize(applicationContext)
        enableEdgeToEdge()
        setContentView(R.layout.new_compose_activity_layout)
        myViewModel.emoticonPacks.observe(this) { packs ->
            saveDataToPreferences(packs)
        }
        setContent {
            OpenticonTheme {
                AppNavHost()
            }
        }
    }

    private val json = Json { encodeDefaults = true } // Json 설정

    private fun saveDataToPreferences(packs: List<EmoticonPack>) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val jsonString = json.encodeToString(packs)
        Log.d("MainJson",jsonString)
        editor.putString("emoticon_data", jsonString)
        editor.apply()
    }
}
