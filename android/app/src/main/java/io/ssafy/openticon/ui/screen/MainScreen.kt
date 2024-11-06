package io.ssafy.openticon.ui.screen

import android.Manifest
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import io.ssafy.openticon.FloatingService
import io.ssafy.openticon.ui.component.BottomNavigationBar
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityManager
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.rememberImagePainter
import io.ssafy.openticon.KeyboardAccessibilityService
import io.ssafy.openticon.R
import io.ssafy.openticon.data.model.EmoticonPackWithEmotions
import io.ssafy.openticon.data.model.LikeEmoticonPack
import io.ssafy.openticon.data.model.SampleEmoticonPack
import io.ssafy.openticon.ui.component.UnAuthModal
import io.ssafy.openticon.ui.viewmodel.EmoticonViewModel
import io.ssafy.openticon.ui.viewmodel.LikeEmoticonViewModel
import io.ssafy.openticon.ui.viewmodel.MainViewModel
import io.ssafy.openticon.ui.viewmodel.MemberViewModel
import io.ssafy.openticon.ui.viewmodel.MyEmoticonViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Composable
fun MainScreen(
    navController: NavController,
    myViewModel: EmoticonViewModel = hiltViewModel() ,
    likeEmoticonViewModel: LikeEmoticonViewModel = hiltViewModel(),
    myEmoticonViewModel: MyEmoticonViewModel = hiltViewModel()
) {

    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mainViewModel: MainViewModel = hiltViewModel()
    val isLoggedIn by mainViewModel.isLoggedIn.collectAsState()


    myViewModel.sampleEmoticonPacks.observe(lifecycleOwner) { packs ->
        saveDataToPreferences(packs, context)
    }

    likeEmoticonViewModel.sampleEmoticonPacksLiveData.observe(lifecycleOwner) { pack ->
        if (pack != null) {
            saveLikeDataToPreferences(pack, context)
        }
    }

    Log.d("isLoggedIn", isLoggedIn.toString());
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedItem,
                onItemSelected = {index ->
                    if (index == 3) {
                        mainViewModel.isLoggedIn
                        if (isLoggedIn) {
                            selectedItem = index
                        } else {
                            navController.navigate("login")
                        }
                    } else {
                        selectedItem = index
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (allPermissionsGranted(context)) {
                        Log.d("mainScreen","allPermission")
                        startFloatingService(context, myViewModel, likeEmoticonViewModel, lifecycleOwner)
                    } else {
                        Log.d("mainScreen","notAllPermission")
                        requestPermissionsAndStartService(context, myViewModel, likeEmoticonViewModel, lifecycleOwner)
                    }
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.sin),
                    contentDescription = "Start Service Icon",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LaunchedEffect(selectedItem) {
                if (selectedItem == 2) {
                    // 특정 함수 호출
                    myEmoticonViewModel.loadEmoticonPacks()
                }
            }

            when (selectedItem) {
                0 -> StoreScreen(navController = navController)
                1 -> SearchScreen(navController = navController)
                2 -> MyEmoticonsScreen(navController = navController)
                3 -> ProfileScreen(navController = navController)
            }
        }
    }
}
fun requestPermissionsAndStartService(context: Context, myViewModel:EmoticonViewModel,
                                      likeEmoticonViewModel: LikeEmoticonViewModel,
                                      lifecycleOwner: LifecycleOwner) {
    // 1. 알림 권한 요청
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            REQUEST_NOTIFICATION_PERMISSION
        )
        Log.d("mainScreen","AlertPermission")
    }

    // 2. 다른 앱 위에 표시 권한 요청
    if (!Settings.canDrawOverlays(context)) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        )
        (context as Activity).startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
        Log.d("mainScreen","DisplayOnPermission")
    }

    // 3. 접근성 권한 요청
//    if (!isAccessibilityServiceEnabled(context, KeyboardAccessibilityService::class.java)) {
//        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
//        context.startActivity(intent)
//        Log.d("mainScreen","AccessPermission")
//    }

    // 모든 권한이 부여되었는지 확인 후 서비스 시작
    if (allPermissionsGranted(context)) {
        startFloatingService(context, myViewModel, likeEmoticonViewModel, lifecycleOwner)
    }
}

fun allPermissionsGranted(context: Context): Boolean {
    val notificationPermissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    } else true

    if(notificationPermissionGranted){
        Log.d("mainScreen","alert Permission")
    }
    if(Settings.canDrawOverlays(context)){
        Log.d("mainScreen","Draw Permission")
    }

    return notificationPermissionGranted &&
            Settings.canDrawOverlays(context)
            //&& isAccessibilityServiceEnabled(context, KeyboardAccessibilityService::class.java)
}

private fun startFloatingService(
    context: Context,
    myViewModel: EmoticonViewModel,
    likeEmoticonViewModel: LikeEmoticonViewModel,
    lifecycleOwner: LifecycleOwner
) {
    val intent = Intent(context, FloatingService::class.java)

    // 서비스 실행 여부 확인
    if (isServiceRunning(context, FloatingService::class.java)) {
        // 이미 실행 중인 경우 종료
        context.stopService(intent)
    } else {


        // LiveData를 한 번만 관찰하여 데이터 저장 후 완료 알림

        CoroutineScope(Dispatchers.Main).launch {
            myViewModel.loadEmoticonPacks() // suspend 함수로 비동기 처리
            likeEmoticonViewModel.initEmoticonDataFromPreferences() // 이 함수가 완료된 후 실행

            delay(100L)
            // 데이터 저장이 모두 완료된 후에 서비스 시작
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }
}


// 서비스 실행 여부를 확인하는 함수
private fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}

private val json = Json { encodeDefaults = true } // Json 설정

private fun saveDataToPreferences(packs: List<EmoticonPackWithEmotions>, context: Context) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val jsonString = json.encodeToString(packs)
    Log.d("MainJson", jsonString)

    // commit을 사용하여 동기적으로 저장
    val success = editor.putString("emoticon_data", jsonString).commit()

    if (success) {
        Log.d("MainJson", "Data saved successfully.")
    } else {
        Log.e("MainJson", "Failed to save data.")
    }
}

private fun saveLikeDataToPreferences(pack: LikeEmoticonPack, context: Context) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val jsonString = json.encodeToString(pack)
    Log.d("MainJson", jsonString)

    // commit을 사용하여 동기적으로 저장
    val success = editor.putString("like_emoticon_data", jsonString).commit()

    if (success) {
        Log.d("MainJson", "Like data saved successfully.")
    } else {
        Log.e("MainJson", "Failed to save like data.")
    }
}


// Constants for permission requests
private const val REQUEST_NOTIFICATION_PERMISSION = 1001
private const val REQUEST_OVERLAY_PERMISSION = 1002