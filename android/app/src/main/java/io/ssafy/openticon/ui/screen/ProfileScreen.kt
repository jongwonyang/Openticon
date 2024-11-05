package io.ssafy.openticon.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import io.ssafy.openticon.R
import io.ssafy.openticon.ui.viewmodel.MemberViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ProfileScreen(
    navController: NavController,
) {
    val viewModel: MemberViewModel = hiltViewModel()
    val memberEntity by viewModel.memberEntity.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

    fun getFormattedDate(timestamp: String): String {
        return try {
            // String 타입의 Unix 타임스탬프를 Long으로 변환
            val seconds = timestamp.toDouble().toLong()  // 초 단위로 변환
            val createdAt = Instant.ofEpochSecond(seconds)
                .atZone(ZoneId.of("Asia/Seoul"))
                .toOffsetDateTime()

            // 원하는 형식으로 포맷
            val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            createdAt.format(formatter)
        } catch (e: NumberFormatException) {
            "날짜 형식 오류"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // 프로필 이미지
        AsyncImage(
            model = if (memberEntity?.profile_image.isNullOrEmpty()) {
                "https://lh3.googleusercontent.com/a/ACg8ocKR5byM6QoaU-8EG4pDglN1rnU3RIqI9Ght42cZJ8Ym0YdDDA=s96-c"
            } else {
                memberEntity?.profile_image
            },
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop, // 잘리지 않게 조정
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.White, CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 이름과 인증 마크
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            memberEntity?.nickname?.let {
                Text(
                    text = it,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Image(
                painter = painterResource(id = R.drawable.writer_mark),
                contentDescription = "Writer Mark",
                modifier = Modifier.size(20.dp)
            )
        }

        // 가입 날짜
        memberEntity?.createdAt?.let {
            Text(
                text = it.slice(0..9),
                fontSize = 14.sp,
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 포인트
        Text(
            text = "${memberEntity?.point ?: 0} 포인트",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 회원 수정, 회원 탈퇴 버튼
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { navController.navigate("edit_profile") },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(Color.LightGray)
            ) {
                Text("회원 수정", color = Color.Black)
            }
            Button(
                onClick = {
                    coroutineScope.launch {
                        val result = viewModel.deleteMember()
                        if (result.isSuccess) {
                            // 회원 삭제 성공 시 모달 창 띄우기
                            showDeleteDialog = true
                        } else {
                            println("회원 삭제 실패: ${result.exceptionOrNull()?.message}")
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(Color.LightGray)
            ) {
                Text("회원 탈퇴", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 포인트 충전 버튼
        Button(
            onClick = { /* 포인트 충전 기능 */ },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF4C9EFF)) // 파란색 버튼
        ) {
            Text("포인트 충전", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Button(onClick = {
            navController.navigate("login")
        }) {
            Text("임시 로그인 버튼")
        }
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("회원 탈퇴") },
                text = { Text("회원 탈퇴가 성공적으로 완료되었습니다.") },
                confirmButton = {
                    Button(
                        onClick = {
                            showDeleteDialog = false
                            navController.navigate("main") // 메인 페이지로 이동
                        }
                    ) {
                        Text("확인")
                    }
                }
            )
        }
    }
}
