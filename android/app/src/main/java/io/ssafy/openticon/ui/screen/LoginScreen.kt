package io.ssafy.openticon.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.ssafy.openticon.R
import io.ssafy.openticon.data.local.TokenDataSource
import io.ssafy.openticon.di.NetworkModule
import io.ssafy.openticon.ui.viewmodel.MemberViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun LoginScreen() {

    val network : NetworkModule = NetworkModule;
    val context = LocalContext.current
    val memberViewModel: MemberViewModel = hiltViewModel()
    val tokenDataSource = TokenDataSource
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.otter),
            contentDescription = "Otter Character",
            modifier = Modifier
                .size(240.dp)
                .padding(bottom = 24.dp)
                .clickable {
                    coroutineScope.launch {
                        tokenDataSource.saveToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxd3NhNTIyQG5hdmVyLmNvbSIsImlhdCI6MTczMDE2Njg2OSwiZXhwIjoxNzMwMTY4NjY5fQ.Ysto7YjMC5u3mI9Hcs0SgiVvfRvsOQDZSyjYkN2gySUm5RpUymT5OwQorwXK2ZKEx5xdVDywuVFVhiR9i5xFag") // Replace with real token logic
                    }
                },
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(170.dp))

        Button(
            onClick = {
                val googleLoginUrl = memberViewModel.baseUrl+"/api/v1/oauth2/authorization/kakao?redirect_uri=openticon://successLogin&mode=login"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleLoginUrl))
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE500)),
            shape = RoundedCornerShape(0.dp)
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

        Spacer(modifier = Modifier.height(10.dp))

        // Button for Naver Login with Image
        Button(
            onClick = {
                val googleLoginUrl = memberViewModel.baseUrl+"/api/v1/oauth2/authorization/naver?redirect_uri=openticon://successLogin&mode=login"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleLoginUrl))
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2DB400)
            ),
            shape = RoundedCornerShape(0.dp)
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
        Spacer(modifier = Modifier.height(10.dp))

        // Button for Google Login with Image
        Button(
            onClick = {
                val googleLoginUrl = memberViewModel.baseUrl+"/api/v1/oauth2/authorization/google?redirect_uri=openticon://successLogin&mode=login"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleLoginUrl))
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(0.dp)
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
