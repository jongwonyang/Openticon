package io.ssafy.openticon.ui.screen

import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.ssafy.openticon.R
import io.ssafy.openticon.data.local.TokenDataSource
import io.ssafy.openticon.ui.viewmodel.MemberViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val memberViewModel: MemberViewModel = hiltViewModel()
    val tokenDataSource = TokenDataSource
    val coroutineScope = rememberCoroutineScope()

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
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Help,
                            contentDescription = null
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
                            .clickable {
                                coroutineScope.launch {
                                    val token = tokenDataSource.token.firstOrNull()
                                    Log.d("Token", token ?: "No token found")
                                }
                            }
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
                        painter = painterResource(R.drawable.google),
                        contentDescription = "Google Image",
                        modifier = Modifier.size(23.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("구글로 시작하기", fontSize = 17.sp, color = Color.Black)
                }
            }
        }
    }
}
