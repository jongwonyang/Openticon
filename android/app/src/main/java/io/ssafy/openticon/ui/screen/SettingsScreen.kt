package io.ssafy.openticon.ui.screen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "설정") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                item {
                    ListItem(
                        headlineContent = { Text("문의하기") },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Filled.Mail,
                                contentDescription = null
                            )
                        },
                        trailingContent = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .clickable {
                                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:openticon@gmail.com")
                                    putExtra(Intent.EXTRA_SUBJECT, "[오픈티콘 사용자 문의] 제목을 입력하세요")
                                }
                                if (emailIntent.resolveActivity(context.packageManager) != null) {
                                    context.startActivity(emailIntent)
                                } else {
                                    Toast
                                        .makeText(
                                            context,
                                            "이메일 앱이 설치되어 있지 않습니다.",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            }
                            .padding(vertical = 8.dp)
                    )
                    HorizontalDivider()
                }
            }
        }
    )
}
