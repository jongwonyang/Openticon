package io.ssafy.openticon.ui.component

import android.Manifest
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.ImageSearch
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import io.ssafy.openticon.ui.viewmodel.SearchKey
import io.ssafy.openticon.ui.viewmodel.SearchScreenViewModel

@Composable
fun SearchBar(
    selectedKey: SearchKey,
    onKeyChange: (SearchKey) -> Unit,
    searchText: String,
    onTextChange: (String) -> Unit,
    viewModel: SearchScreenViewModel = hiltViewModel(),
    listState: LazyListState
) {
    val focusManager = LocalFocusManager.current
    var isExpanded by remember { mutableStateOf(false) }
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    val isEmptyField by viewModel.isEmptyField.collectAsState()
    val context = LocalContext.current

    // 이미지 선택 런처
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setImageUri(uri)
            viewModel.performSearch(context.contentResolver, context, listState)
        } else {
            Log.e("ImageSearch", "Error: Selected file URI is null.")
        }
    }

    // 권한 요청 런처 (Android 10 이하)
    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            imagePickerLauncher.launch("image/*")
        } else {
            Log.e("ImageSearch", "Storage permission not granted.")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .then(
                if (isEmptyField) Modifier.border(
                    2.dp,
                    MaterialTheme.colorScheme.error,
                    RoundedCornerShape(50)
                ) else Modifier
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        if (selectedImageUri == null) {
            // text search
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.height(48.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(selectedKey.displayName)
                        Icon(
                            imageVector = if (isExpanded) {
                                Icons.Filled.ArrowDropUp
                            } else {
                                Icons.Filled.ArrowDropDown
                            },
                            contentDescription = null
                        )
                    }
                }
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    SearchKey.entries.forEach {
                        DropdownMenuItem(
                            text = { Text(it.displayName) },
                            onClick = {
                                onKeyChange(it)
                                isExpanded = false
                            }
                        )
                    }
                }
                BasicTextField(
                    value = searchText,
                    onValueChange = { onTextChange(it) },
                    maxLines = 1,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (searchText.isEmpty()) {
                                Text(
                                    text = "검색어 입력",
                                    color = Color.Gray
                                )
                            }
                            innerTextField()
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                            viewModel.search(context, listState)
                        }
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .wrapContentHeight()
                )
                if (searchText.isNotEmpty()) {
                    IconButton(
                        onClick = { onTextChange("") },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "텍스트 초기화",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.search(context, listState)
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "텍스트 검색",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        selectImage(
                            imagePickerLauncher = imagePickerLauncher,
                            storagePermissionLauncher = storagePermissionLauncher
                        )
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ImageSearch,
                        contentDescription = "이미지 검색",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        } else {
            // image search
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Outlined.ImageSearch,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(Modifier.width(16.dp))
                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )
                }
                IconButton(
                    onClick = {
                        viewModel.clearSelectedImage()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = "이미지 초기화",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

private fun selectImage(
    imagePickerLauncher: ActivityResultLauncher<String>,
    storagePermissionLauncher: ActivityResultLauncher<String>
) {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    } else {
        imagePickerLauncher.launch("image/*")
    }
}