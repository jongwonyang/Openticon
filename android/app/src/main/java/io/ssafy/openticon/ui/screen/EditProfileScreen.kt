package io.ssafy.openticon.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import io.ssafy.openticon.ui.viewmodel.EditProfileViewModel
import io.ssafy.openticon.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val viewModel: EditProfileViewModel = hiltViewModel()
    val memberEntity by viewModel.memberEntity.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }
    var isFocused by remember { mutableStateOf(false) }
    var nickname by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "회원 정보 수정") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Apply the padding values here
                    .padding(16.dp), // Additional padding
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)

                    ) {
                        AsyncImage(
                            model = if (memberEntity?.profileImage.isNullOrEmpty()) {
                                "https://lh3.googleusercontent.com/a/ACg8ocKR5byM6QoaU-8EG4pDglN1rnU3RIqI9Ght42cZJ8Ym0YdDDA=s96-c"
                            } else {
                                memberEntity?.profileImage
                            },
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .align(Alignment.Center)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp)) // Use width for horizontal spacing in a Row

                    // Edit Profile Button
                    Button(
                        onClick = { /* TODO: Handle profile edit */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF42A5F5) // Blue color
                        )
                    ) {
                        Text(text = "프로필 수정", color = Color.White)
                    }
                }

                // Profile Image
                Spacer(modifier = Modifier.height(50.dp))

                // Nickname Input Field
                TextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    placeholder = { Text("수정할 닉네임을 입력해 주세요.") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f), // Set width to 80% of available space
                    shape = RoundedCornerShape(0.dp), // Square corners
                    interactionSource = interactionSource
                )

                // Draw a custom bottom border if not focused
//                if (!isFocused) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 8.dp)
//                            .height(1.dp)
//                            .background(Color.Gray)
//                    )
//                }

                Spacer(modifier = Modifier.height(48.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {

                            /* TODO: Confirm action */
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF42A5F5) // Confirm button color
                        ),
                        modifier = Modifier.width(100.dp)
                    ) {
                        Text(text = "확인", color = Color.White)
                    }

                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB0B0B0) // Cancel button color
                        ),
                        modifier = Modifier.width(100.dp)
                    ) {
                        Text(text = "취소", color = Color.White)
                    }
                }
            }
        }
    )
}
