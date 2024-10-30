package io.ssafy.openticon.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import io.ssafy.openticon.data.local.TokenDataSource
import io.ssafy.openticon.ui.data.model.EditDeviceTokenRequestDto
import io.ssafy.openticon.ui.data.remote.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.ssafy.openticon.ui.data.remote.MemberApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.reflect.Member

@Composable
fun LoginSuccessScreen(accessToken: String) {
    val context = LocalContext.current
    val memberApi = ApiClient(context).memberApi
    val coroutineScope = rememberCoroutineScope()
    Log.d("LoginSuccess", "Access Token: $accessToken")

    // 화면이 실행되자마자 saveDeviceToken 호출
    LaunchedEffect(Unit) {
        val deviceToken = "your_device_token"
        coroutineScope.launch {
            saveDeviceToken(accessToken, deviceToken, isMobile = true, context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "로그인 성공!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "환영합니다! 서비스를 이용하실 수 있습니다.", fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Access Token: $accessToken", fontSize = 14.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            coroutineScope.launch {
                try {
                    // 사용자 정보 가져오기
                    val response = memberApi.getMemberInfo()
                    if (response.isSuccessful) {
                        println("User info: ${response.body()}")
                    } else {
                        println("Failed with status code: ${response.code()}")
                    }
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                }
            }
        }) {
            Text(text = "내 정보", fontSize = 16.sp)
        }
    }
}

// 디바이스 토큰 저장 함수
suspend fun saveDeviceToken(accessToken: String, deviceToken: String, isMobile: Boolean, context: Context) {
    val tokenDataSource = TokenDataSource(context)
    val memberApi = ApiClient(context).memberApi
    val request = EditDeviceTokenRequestDto(deviceToken, isMobile)
    tokenDataSource.saveToken("Bearer $accessToken")

//    try {
//        val response = memberApi.updateDeviceToken("Bearer $accessToken", request)
//        if (response.isSuccessful) {
//            Log.d("DeviceToken", "Device token saved successfully.")
//        } else {
//            Log.e("DeviceToken", "Failed to save device token: ${response.code()}")
//        }
//    } catch (e: HttpException) {
//        Log.e("DeviceToken", "Error: ${e.message}")
//    }
}
