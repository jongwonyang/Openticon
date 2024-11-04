package io.ssafy.openticon.ui.screen

import android.Manifest
import android.content.ContentResolver
import android.os.Build
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import io.ssafy.openticon.ui.viewmodel.ImageSearchViewModel
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import io.ssafy.openticon.ui.viewmodel.SearchScreenViewModel

@OptIn(ExperimentalMaterial3Api::class) // Material3 Scaffold 사용 시 필요한 어노테이션
@Composable
fun ImageSearchScreen(
    isImageSearch: Boolean,  // Accept the isImageSearch state
    onImageSearchToggle: (Boolean) -> Unit,
    searchViewModel: SearchScreenViewModel = hiltViewModel(),
    contentResolver: ContentResolver
) {
    val selectedImageUri by searchViewModel.selectedImageUri.collectAsState()
    val searchResult by searchViewModel.searchResult.collectAsState()

    // 이미지 선택 런처 (GetContent 사용)
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            searchViewModel.setImageUri(uri)
        } else {
            Log.e("ImageSearch", "Error: Selected file URI is null.")
        }
    }

    // 권한 요청 런처 (Android 10 이하일 경우)
    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            imagePickerLauncher.launch("image/*")
        } else {
            Log.e("ImageSearch", "Storage permission not granted.")
        }
    }

    // 레이아웃 수정: 여백 없이 화면 위에 딱 붙이기
    Column(
        modifier = Modifier
            .fillMaxWidth()  // 너비를 가득 채우기
            .padding(16.dp),  // 필요한 패딩 설정
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top  // 레이아웃을 위쪽으로 정렬
    ) {
        // 이미지 선택 공간
        Box(
            modifier = Modifier
                .size(200.dp)
                .clickable {
                    // Android 10 이하일 경우 권한 요청
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                        storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    } else {
                        imagePickerLauncher.launch("image/*")
                    }
                }
        ) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = selectedImageUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("이미지 선택", modifier = Modifier.align(Alignment.Center))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(){
            Button(onClick = {
                searchViewModel.performSearch(contentResolver)  // 검색 버튼 클릭 시 수행
            }) {
                Text("검색")
            }
            Button(onClick = {
                onImageSearchToggle(!isImageSearch) // 검색 버튼 클릭 시 수행
            }) {
                Text("텍스트 검색")
            }
        }


        LaunchedEffect(searchResult) {
            searchResult.let {
                Log.d("imageSearch", searchResult.toString())
            }
        }
    }
}
