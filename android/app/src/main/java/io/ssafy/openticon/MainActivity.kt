package io.ssafy.openticon

import android.app.Application
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import io.ssafy.openticon.data.local.TokenDataSource
import io.ssafy.openticon.ui.navigation.AppNavHost
import io.ssafy.openticon.ui.theme.AppTheme
import com.iamport.sdk.domain.core.Iamport

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // private val myViewModel: EmoticonViewModel by viewModels()
//private val likeEmoticonViewModel: LikeEmoticonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenDataSource.initialize(applicationContext)
        enableEdgeToEdge()
        Iamport.init(this)
//        Iamport.create(applicationContext as Application)
        setContentView(R.layout.new_compose_activity_layout)

//        myViewModel.sampleEmoticonPacks.observe(this) { packs ->
//            saveDataToPreferences(packs)
//        }
//
//        likeEmoticonViewModel.sampleEmoticonPacksLiveData.observe(this) { pack ->
//            if (pack != null) {
//                saveLikeDataToPreferences(pack)
//            }
//        }

        setContent {
            AppTheme {
                AppNavHost()
            }
        }
    }
    override fun onDestroy() {
        Iamport.close()
        super.onDestroy()
    }

//    private val json = Json { encodeDefaults = true } // Json 설정
//
//    private fun saveDataToPreferences(packs: List<SampleEmoticonPack>) {
//        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        val jsonString = json.encodeToString(packs)
//        Log.d("MainJson",jsonString)
//        editor.putString("emoticon_data", jsonString)
//        editor.apply()
//    }
//
//    private fun saveLikeDataToPreferences(pack: SampleEmoticonPack) {
//        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        val jsonString = json.encodeToString(pack)
//        Log.d("MainJson",jsonString)
//        editor.putString("like_emoticon_data", jsonString)
//        editor.apply()
//    }
}
