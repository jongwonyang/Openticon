package io.ssafy.openticon.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import io.ssafy.openticon.R

@Composable
fun ProfileScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 프로필 이미지
        AsyncImage(
            model = "https://cdn.ppomppu.co.kr/zboard/data3/2022/0509/m_20220509173224_d9N4ZGtBVR.jpeg",
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
            Text(
                text = "김용원",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Image(
                painter = painterResource(id = R.drawable.writer_mark),
                contentDescription = "Writer Mark",
                modifier = Modifier.size(20.dp)
            )
        }

        // 가입 날짜
        Text(
            text = "2024.10.21",
            fontSize = 14.sp,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 포인트
        Text(
            text = "50,000 포인트",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 회원 수정, 회원 탈퇴 버튼
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { /* 회원 수정 기능 */ },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(Color.LightGray)
            ) {
                Text("회원 수정", color = Color.Black)
            }
            Button(
                onClick = { /* 회원 탈퇴 기능 */ },
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
    }
}
