package io.ssafy.openticon.ui.screen

import io.ssafy.openticon.ui.viewmodel.MemberViewModel
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.ssafy.openticon.data.local.TokenDataSource
import io.ssafy.openticon.data.model.EditDeviceTokenRequestDto
import io.ssafy.openticon.data.remote.ApiClient
import io.ssafy.openticon.data.remote.MemberApiService
import kotlinx.coroutines.launch

@Composable
fun LoginSuccessScreen(accessToken: String, navController: NavController) {
    val context = LocalContext.current
    val memberApi = ApiClient().memberApi
    val coroutineScope = rememberCoroutineScope()
    val memberApiService = MemberApiService(memberApi)
    val memberViewModel: MemberViewModel = viewModel()

    // stateCode와 memberEntity 각각을 관찰
    val isLoggedIn by memberViewModel.isLoggedIn.collectAsState()
    val memberEntity by memberViewModel.memberEntity.collectAsState()

    Log.d("LoginSuccess", "Access Token: $accessToken")

    // 화면이 실행되자마자 saveDeviceToken 호출 및 회원 정보 가져오기
    LaunchedEffect(Unit) {
        val deviceToken = "your_device_token"
        coroutineScope.launch {
            // 1. saveDeviceToken을 동기적으로 실행
            saveDeviceToken(accessToken, deviceToken, isMobile = true, context)

            // 2. fetchMemberInfo가 완료될 때까지 기다렸다가 상태 확인
            memberViewModel.fetchMemberInfo()
            if (!memberViewModel.isLoggedIn.value) {
                navController.navigate("login") // 로그인 실패 시 로그인 페이지로 이동
            }else{
                navController.popBackStack()
            }
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

        // stateCode와 memberEntity에 따라 정보 표시
        if (isLoggedIn && memberEntity != null) {
            Text(text = "환영합니다, ${memberEntity?.nickname}!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = "이메일: ${memberEntity?.email}", fontSize = 16.sp, color = Color.Gray)
            Text(text = "포인트: ${memberEntity?.point}", fontSize = 16.sp, color = Color.Gray)
        } else{
            Text(text = "로그인이 필요합니다.", fontSize = 16.sp, color = Color.Red)
//            Cour(Unit) {
//                navController.navigate("login") // 401 에러 시 로그인 페이지로 이동
//            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            coroutineScope.launch {
                memberViewModel.fetchMemberInfo()
            }
        }) {
            Text(text = "내 정보 새로고침", fontSize = 16.sp)
        }
    }
}

// 디바이스 토큰 저장 함수
suspend fun saveDeviceToken(accessToken: String, deviceToken: String, isMobile: Boolean, context: Context) {
    val tokenDataSource = TokenDataSource
    val memberApi = ApiClient().memberApi
    val request = EditDeviceTokenRequestDto(deviceToken, isMobile)
    tokenDataSource.saveToken(accessToken)

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
