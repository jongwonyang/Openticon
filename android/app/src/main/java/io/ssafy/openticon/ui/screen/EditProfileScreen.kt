package io.ssafy.openticon.ui.screen

import android.Manifest
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import io.ssafy.openticon.ui.viewmodel.EditProfileViewModel
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import io.ssafy.openticon.R
import io.ssafy.openticon.data.model.MemberEntity
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,

                      ) {
    val context = LocalContext.current
    val contentResolver = context.contentResolver
    val viewModel: EditProfileViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val memberEntity by viewModel.memberEntity.collectAsState()
    val isDuplicated by viewModel.isDuplicate.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }
    var isFocused by remember { mutableStateOf(false) }
//    var nickname by remember { mutableStateOf(memberEntity?.nickname?.let { TextFieldValue(it) }) }
//    var bio by remember { mutableStateOf(memberEntity?.bio?.let { TextFieldValue(it) }) }
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    var nickname by remember { mutableStateOf(TextFieldValue("")) }
    var bio by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(memberEntity) {
        if (memberEntity != null) {
            nickname = TextFieldValue(memberEntity!!.nickname ?: "")
            bio = TextFieldValue(memberEntity!!.bio ?: "")
        }
    }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(nickname) {
        isLoading = true
        snapshotFlow { nickname.text }
            .debounce(300)
            .filter { it.isNotEmpty() }
            .collect { newNickname ->
                viewModel.checkDuplicateNickname(newNickname)
                isLoading = false
            }
    }

    // 이미지 선택 런처 (GetContent 사용)
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setImageUri(uri)
        } else {
            Log.e("ImageSearch", "Error: Selected file URI is null.")
        }
    }

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            imagePickerLauncher.launch("image/*")
        } else {
            Log.e("ImageSearch", "Storage permission not granted.")
        }
    }



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
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
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
                            .background(Color.White)
                    ) {
                        if (selectedImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(model = selectedImageUri),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape), // Image에도 CircleShape 적용
                                contentScale = ContentScale.Crop
                            )
                        } else {
//                            AsyncImage(
//                                model = if (memberEntity?.profile_image.isNullOrEmpty()) {
//                                    "https://lh3.googleusercontent.com/a/ACg8ocKR5byM6QoaU-8EG4pDglN1rnU3RIqI9Ght42cZJ8Ym0YdDDA=s96-c"
//                                } else {
//                                    memberEntity?.profile_image
//                                },
//                                contentDescription = "Profile Image",
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .clip(CircleShape), // AsyncImage에도 CircleShape 적용
//                                contentScale = ContentScale.Crop
//                            )
                            if (memberEntity?.profile_image.isNullOrEmpty()) {
                                Image(
                                    painter = painterResource(R.drawable.ic_launcher_foreground),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            } else {
                                AsyncImage(
                                    model = memberEntity?.profile_image,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(R.drawable.loading_img),
                                    error = painterResource(R.drawable.ic_broken_image),
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                                storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            } else {
                                imagePickerLauncher.launch("image/*")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary // Blue color
                        )
                    ) {
                        Text(text = "이미지 수정", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(50.dp))
                TextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = {Text("닉네임")},
                    supportingText = {
                        when {
                            isLoading -> Text("검사 중...", color = MaterialTheme.colorScheme.onSurface)
                            isDuplicated -> Text("사용 중인 닉네임입니다.", color = MaterialTheme.colorScheme.error)
                            isDuplicated == false -> Text("사용 가능한 닉네임입니다.", color = MaterialTheme.colorScheme.primary)
                            else -> Text("닉네임을 입력해 주세요.")
                        }
                    },
                    placeholder = { Text("수정할 닉네임을 입력해 주세요.") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    shape = RoundedCornerShape(0.dp),
                    interactionSource = interactionSource,
                    isError = isDuplicated,
                    singleLine = true // 한 줄 입력 설정
                )
                Spacer(modifier = Modifier.height(48.dp))
                TextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("상태 메세지") },
                    supportingText = {Text("상태 메세지")},
                    placeholder = { Text("상태 메세지를 입력해 주세요.") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    shape = RoundedCornerShape(0.dp),
                    interactionSource = interactionSource
                )

                Spacer(modifier = Modifier.height(48.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        enabled = !isLoading && !isDuplicated,
                        onClick = {
                                viewModel.editProfile(contentResolver,nickname.text, bio.text)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary // Confirm button color
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

                // UI 상태에 따른 네비게이션 처리
                when (uiState) {
                    is EditProfileViewModel.UiState.Success -> {
                        Log.d("EditProfileScreen", "프로필 수정 성공")
                        viewModel.updateMemberEntity()
                        LaunchedEffect(Unit) {
                            navController.popBackStack()
                        }
                    }
                    is EditProfileViewModel.UiState.Error -> {
                        Log.d("EditProfileScreen", "프로필 수정 실패")
//                        LaunchedEffect(Unit) {
//                            navController.navigate("error_screen") // 오류 페이지로 이동
//                        }
                    }
                    else -> {}
                }
            }
        }
    )
}
