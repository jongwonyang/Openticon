package io.ssafy.openticon.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.ssafy.openticon.R
import io.ssafy.openticon.data.local.TokenDataSource
import io.ssafy.openticon.ui.viewmodel.MemberViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val memberViewModel: MemberViewModel = hiltViewModel()
    val tokenDataSource = TokenDataSource
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "로그인",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (!navController.popBackStack()) {
                                navController.navigate("main") {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val url = "https://apitest.openticon.store/"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Language,
                            contentDescription = "웹사이트"
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.clip(RoundedCornerShape(40))
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = null,
                )
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "OPENTICON",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "세상의 모든 이모티콘",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(Modifier.height(128.dp))

            ElevatedButton(
                onClick = {
                    val googleLoginUrl =
                        memberViewModel.baseUrl + "/api/v1/oauth2/authorization/kakao?redirect_uri=openticon://successLogin&mode=login"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleLoginUrl))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE500)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.kakao),
                        contentDescription = "Kakao Image",
                        modifier = Modifier.size(23.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("카카오로 시작하기", fontSize = 17.sp, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Button for Naver Login with Image
            ElevatedButton(
                onClick = {
                    val googleLoginUrl =
                        memberViewModel.baseUrl + "/api/v1/oauth2/authorization/naver?redirect_uri=openticon://successLogin&mode=login"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleLoginUrl))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2DB400)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.naver), // Make sure your image resource is correctly added
                        contentDescription = "Naver Image",
                        modifier = Modifier
                            .size(23.dp)
//                            .clickable {
//                                coroutineScope.launch {
//                                    val token = tokenDataSource.token.firstOrNull()
//                                    Log.d("Token", token ?: "No token found")
//                                }
//                            }
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    Text("네이버로 시작하기", fontSize = 17.sp, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Button for Google Login with Image
            ElevatedButton(
                onClick = {
                    val googleLoginUrl =
                        memberViewModel.baseUrl + "/api/v1/oauth2/authorization/google?redirect_uri=openticon://successLogin&mode=login"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleLoginUrl))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.google_icon),
                        contentDescription = "Google Image",
                        modifier = Modifier.size(23.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("구글로 시작하기", fontSize = 17.sp, color = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            // 서비스 이용약관 동의 문구
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "로그인 시 ",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 12.sp
                )
                Text(
                    text = "서비스 이용약관",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { showDialog = true }
                )
                Text(
                    text = "에 동의하게 됩니다.",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 12.sp
                )
            }
        }
    }

    // 이용약관 모달 창
    if (showDialog) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(0.8f),
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("닫기")
                }
            },
            title = {
                Text(text = "서비스 이용약관")
            },
            text = {
                // Scrollable Text area
                Box(
                    modifier = Modifier
                        .height(300.dp) // Set maximum height for scrollable area
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "오픈티콘은 이용자의 개인정보를 중요시하며, 「개인정보 보호법」에 따라 이용자의 개인정보를 보호하고 이와 관련한 고충을 원활하게 처리할 수 있도록 아래와 같이 개인정보 수집 및 이용에 대한 동의를 구합니다.\n" +
                                "\n" +
                                "1. 개인정보 수집 및 이용 목적\n" +
                                "오픈티콘은 이용자의 개인정보를 다음의 목적으로 수집 및 이용합니다.\n" +
                                "- 길 안내 서비스 제공 및 맞춤형 추천 장소 정보 제공\n" +
                                "- 서비스 이용 기록 및 통계 분석을 통한 서비스 개선\n" +
                                "- 회원 관리 및 인증 절차 수행\n" +
                                "- 서비스 관련 공지 및 고객 문의 응대\n" +
                                "\n" +
                                "2. 수집하는 개인정보 항목\n" +
                                "오픈티콘은 서비스 제공을 위해 아래와 같은 개인정보를 수집합니다.\n" +
                                "\n" +
                                "수집 항목: 이름, 이메일 주소, 생년월일, 프로필 사진\n" +
                                "\n" +
                                "3. 개인정보의 보유 및 이용 기간\n" +
                                "오픈티콘 이용자의 개인정보를 수집 및 이용 목적이 달성될 때까지 보유하며, 회원 탈퇴 시 또는 수집된 개인정보의 이용 목적이 달성되었을 때 해당 정보를 지체 없이 파기합니다. \n" +
                                "\n" +
                                "4. 개인정보의 제공 및 공유\n" +
                                "오픈티콘은 이용자의 동의 없이 개인정보를 제3자에게 제공하지 않으며, 법령에 의해 요구되는 경우에 한해 제공될 수 있습니다.\n" +
                                "\n" +
                                "5. 동의 거부 권리\n" +
                                "이용자는 개인정보 수집 및 이용에 대한 동의를 거부할 권리가 있습니다. 다만, 개인정보 활용 동의를 거부할 경우 오픈티콘의 서비스 이용이 제한될 수 있습니다."
                    )
                }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        )
    }
}
