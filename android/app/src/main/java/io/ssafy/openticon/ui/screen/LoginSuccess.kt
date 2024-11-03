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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.ssafy.openticon.data.local.TokenDataSource
import io.ssafy.openticon.data.model.EditDeviceTokenRequestDto
import io.ssafy.openticon.data.remote.MemberApiService
import io.ssafy.openticon.ui.component.UnAuthModal
import kotlinx.coroutines.launch

@Composable
fun LoginSuccessScreen(accessToken: String, navController: NavController,viewModel: MemberViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val memberViewModel: MemberViewModel = hiltViewModel()
    val isLoggedIn by memberViewModel.isLoggedIn.collectAsState()
    val memberEntity by memberViewModel.memberEntity.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var isSuccessHandled by remember { mutableStateOf(false) }
    // 화면이 실행되자마자 saveDeviceToken 호출 및 회원 정보 가져오기
    LaunchedEffect(Unit) {
        val deviceToken = "your_device_token"
        coroutineScope.launch {
            // 1. saveDeviceToken을 동기적으로 실행
            saveDeviceToken(accessToken, deviceToken, isMobile = true, context)
            // 2. fetchMemberInfo가 완료될 때까지 기다렸다가 상태 확인
            memberViewModel.fetchMemberInfo()
        }
    }
    Log.d("isLoggedIn3", isLoggedIn.toString())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (uiState) {
            is MemberViewModel.UiState.Loading -> {
                Text(text = "로딩 중...", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
            is MemberViewModel.UiState.Success -> {
                if (!isSuccessHandled) {
                    isSuccessHandled = true
                    navController.popBackStack()
                }
            }
            is MemberViewModel.UiState.Error -> {
                if (showErrorDialog) {
                    UnAuthModal(
                        navController = navController,
                        onDismiss = {
                            showErrorDialog = false
                        }
                    )
                }
            }
            is MemberViewModel.UiState.UnAuth -> {
                if (showErrorDialog) {
                    UnAuthModal(
                        navController = navController,
                        onDismiss = {
                            showErrorDialog = false
                        }
                    )
                }
            }
        }
    }
}

// 디바이스 토큰 저장 함수
suspend fun saveDeviceToken(accessToken: String, deviceToken: String, isMobile: Boolean, context: Context) {
    val tokenDataSource = TokenDataSource
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
