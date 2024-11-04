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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import io.ssafy.openticon.KeyboardAccessibilityService
import io.ssafy.openticon.R
import io.ssafy.openticon.ui.component.UnAuthModal
import io.ssafy.openticon.ui.viewmodel.MemberViewModel


@Composable
fun MainScreen(navController: NavController) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val context = LocalContext.current
    val memberViewModel: MemberViewModel = hiltViewModel()
    val isLoggedIn by memberViewModel.isLoggedIn.collectAsState()
    Log.d("isLoggedIn", isLoggedIn.toString());
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedItem,
                onItemSelected = {index ->
                    if (index == 3) {
                        memberViewModel.isLoggedIn
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
                        startFloatingService(context)
                    } else {
                        Log.d("mainScreen","notAllPermission")
                        requestPermissionsAndStartService(context)
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
            when (selectedItem) {
                0 -> StoreScreen()
                1 -> SearchScreen(navController = navController)
                2 -> MyEmoticonsScreen()
                3 -> ProfileScreen(navController = navController)
            }
        }
    }
}
fun requestPermissionsAndStartService(context: Context) {
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
        startFloatingService(context)
    }
}

//fun isAccessibilityServiceEnabled(context: Context, serviceClass: Class<*>): Boolean {
//    val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
//    val enabledServices: List<AccessibilityServiceInfo> =
//        accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
//
//    for (serviceInfo in enabledServices) {
//        val enabledServiceInfo = serviceInfo.resolveInfo?.serviceInfo
//        if (enabledServiceInfo != null) {
//            val enabledPackageName = enabledServiceInfo.packageName
//            val enabledServiceName = enabledServiceInfo.name
//            if (enabledPackageName == context.packageName && enabledServiceName == serviceClass.name) {
//                return true
//            }
//        }
//    }
//    return false
//}

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
//    if(isAccessibilityServiceEnabled(context, KeyboardAccessibilityService::class.java)){
//        Log.d("mainScreen","KeyboardAccessibilityService Permission")
//    }

    return notificationPermissionGranted &&
            Settings.canDrawOverlays(context)
            //&& isAccessibilityServiceEnabled(context, KeyboardAccessibilityService::class.java)
}

private fun startFloatingService(context: Context) {
    val intent = Intent(context, FloatingService::class.java)

    // 서비스 실행 여부 확인
    if (isServiceRunning(context, FloatingService::class.java)) {
        // 이미 실행 중인 경우 종료
        context.stopService(intent)
    } else {
        // 서비스가 실행 중이 아니면 시작
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
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



// Constants for permission requests
private const val REQUEST_NOTIFICATION_PERMISSION = 1001
private const val REQUEST_OVERLAY_PERMISSION = 1002