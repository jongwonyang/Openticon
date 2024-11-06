package io.ssafy.openticon.ui.component

import android.Manifest
import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import io.ssafy.openticon.ui.viewmodel.SearchScreenViewModel

@Composable
fun ImageSearchBar(
    onTextSearchClick: () -> Unit,
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
        Row() {
            Button(onClick = {
                searchViewModel.performSearch(contentResolver)  // 검색 버튼 클릭 시 수행
            }) {
                Text("검색")
            }
            Button(onClick = onTextSearchClick) {
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
