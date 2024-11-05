package io.ssafy.openticon.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.ssafy.openticon.data.local.TokenDataSource
import io.ssafy.openticon.data.model.EditDeviceTokenRequestDto
import io.ssafy.openticon.ui.viewmodel.MemberViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginSuccessScreen(
    accessToken: String,
    navController: NavController,
    viewModel: MemberViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val memberViewModel: MemberViewModel = hiltViewModel()
    val memberEntity by memberViewModel.memberEntity.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var isSuccessHandled by remember { mutableStateOf(false) }
    // 화면이 실행되자마자 saveDeviceToken 호출 및 회원 정보 가져오기
    LaunchedEffect(Unit) {
        val deviceToken = "your_device_token"
        coroutineScope.launch {
            val tokenDataSource = TokenDataSource
            tokenDataSource.saveToken(accessToken)
            saveDeviceToken(accessToken, deviceToken, isMobile = true, context)
            memberViewModel.fetchMemberInfo()
        }
    }
//    LaunchedEffect(memberViewModel.isLoading.collectAsState().value) {
//        if (isFirstLoad) {
//            isFirstLoad = false
//        } else {
//            if (!memberViewModel.isLoading.value) {
//                if (memberViewModel.isLoggedIn.value) {
//                    Log.d("로그인 성공", "로그인 성공했습니다.")
//                    navController.popBackStack()
//                    Log.d(
//                        "popBackStack",
//                        "popBackStack 호출 후 현재 destination: ${navController.currentDestination?.route}"
//                    )
//                }
////                else {
////                    Log.d("로그인 실패", "로그인 실패했습니다.")
////                    navController.navigate("login")
////                }
//            }
//        }
//    }

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
                Text(
                    text = "로딩 중...",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            is MemberViewModel.UiState.Success -> {
//                val memberEntity = (uiState as MemberViewModel.UiState.Success).data
//                Text(text = "로그인 성공!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(text = "환영합니다, ${memberEntity?.nickname}!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
//                Text(text = "이메일: ${memberEntity?.email}", fontSize = 16.sp, color = Color.Gray)
//                Text(text = "포인트: ${memberEntity?.point}", fontSize = 16.sp, color = Color.Gray)
                Log.d("login success", "로그인 성공")
                if (!isSuccessHandled) {
                    isSuccessHandled = true
                    navController.popBackStack()
                }
            }

            is MemberViewModel.UiState.Error -> {
                Log.d("login error", "로그인 실패")
                // 에러 다이얼로그를 띄움
                if (showErrorDialog) {
                    ErrorDialog {
                        showErrorDialog = false // 다이얼로그 닫기
                    }
                }
            }

            is MemberViewModel.UiState.UnAuth -> {
                Log.d("login unauth", "로그인 실패")
                if (showErrorDialog) {
                    ErrorDialog {
                        showErrorDialog = false // 다이얼로그 닫기
                    }
                }
            }
        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//        Button(onClick = {
//            coroutineScope.launch {
//                viewModel.fetchMemberInfo()
//            }
//        }) {
//            Text(text = "내 정보 새로고침", fontSize = 16.sp)
//        }
    }
}

@Composable
fun ErrorDialog(onDismiss: () -> Unit) {

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "오류 발생") },
        text = { Text(text = "로그인에 실패했습니다. 로그인 페이지로 이동합니다.") },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("확인")
            }
        }
    )
}

// 디바이스 토큰 저장 함수
suspend fun saveDeviceToken(
    accessToken: String,
    deviceToken: String,
    isMobile: Boolean,
    context: Context
) {
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
