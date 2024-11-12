package io.ssafy.openticon

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.ssafy.openticon.data.local.TokenDataSource
import io.ssafy.openticon.ui.navigation.AppNavHost
import io.ssafy.openticon.ui.theme.AppTheme
import com.iamport.sdk.domain.core.Iamport
import io.ssafy.openticon.ui.screen.MainScreen
import io.ssafy.openticon.ui.screen.allPermissionsGranted
import io.ssafy.openticon.ui.screen.startFloatingService
import io.ssafy.openticon.ui.screen.stopFloatingService
import io.ssafy.openticon.ui.viewmodel.EmoticonViewModel
import io.ssafy.openticon.ui.viewmodel.LikeEmoticonViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity(
) : ComponentActivity() {

    private val myViewModel: EmoticonViewModel by viewModels()
    private val likeEmoticonViewModel: LikeEmoticonViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenDataSource.initialize(applicationContext)
        enableEdgeToEdge()
        Iamport.init(this)
//        Iamport.create(applicationContext as Application)
        setContentView(R.layout.new_compose_activity_layout)

        myViewModel.sampleEmoticonPacks.observe(this) { emoticonPacks ->
            // emoticonPacks 데이터를 이용해 UI 업데이트 로직 수행
        }

        likeEmoticonViewModel.sampleEmoticonPacksLiveData.observe(this) { emoticonPacks ->
            // emoticonPacks 데이터를 이용해 UI 업데이트 로직 수행
        }



        handleIntent(intent)
        setContent {
            AppTheme {
                AppNavHost(myViewModel, likeEmoticonViewModel)
            }
        }
    }
    override fun onDestroy() {
        Iamport.close()
        super.onDestroy()
    }

    private fun handleIntent(intent: Intent) {
        // 전달된 "navigate_to" 값 받기 (null 가능성 체크)
        val isLaunched = intent.getBooleanExtra("isLaunched", false)

        if (isLaunched) {
            likeEmoticonViewModel.updateIsLaunched(true)
        } else {
            // "navigate_to" 값이 없을 때 (앱 초기 실행 시의 기본 동작)
            likeEmoticonViewModel.updateIsLaunched(false)
        }
    }


    override fun onResume() {
        super.onResume()
        // 권한이 허용된 경우에만 실행
        if (allPermissionsGranted(this)) {
            stopFloatingService(this)
            Log.d("MainScreen", "MainActivity가 포그라운드로 전환되었습니다.")
        }
    }

    override fun onPause() {
        super.onPause()
        if (allPermissionsGranted(this)) {
            Log.d("LikeEmoticonViewModelHash", "MainActivity ViewModel hash: ${System.identityHashCode(likeEmoticonViewModel)}")

            likeEmoticonViewModel.debugPrint()
            lifecycleScope.launch {
                likeEmoticonViewModel.isLaunched.collect { launched ->
                    if (launched) {
                        Log.d("MainScreen", "서비스가 실행되었습니다.")
                        startFloatingService(this@MainActivity, myViewModel, likeEmoticonViewModel)
                    }
                }
            }
        }
        Log.d("MainScreen", "MainActivity가 백그라운드로 전환되었습니다.")
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
