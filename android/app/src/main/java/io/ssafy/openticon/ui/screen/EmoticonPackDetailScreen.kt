package io.ssafy.openticon.ui.screen

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import io.ssafy.openticon.R
import io.ssafy.openticon.domain.model.EmoticonPackDetail
import io.ssafy.openticon.ui.viewmodel.EmoticonPackDetailScreenViewModel
import io.ssafy.openticon.ui.viewmodel.EmoticonPackDetailScreenViewModel.UiState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmoticonPackDetailScreen(
    emoticonPackUuid: String,
    navController: NavController,
    viewModel: EmoticonPackDetailScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val purchaseState by viewModel.purchaseState.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val isDownloading by viewModel.isDownloading.collectAsState()
    val totalEmoticonCount by viewModel.totalEmoticonCount.collectAsState()
    val downloadedEmoticonCount by viewModel.downloadedEmoticonCount.collectAsState()
    var selectedEmoticonIndex by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }

    LaunchedEffect(emoticonPackUuid) {
        viewModel.fetchEmoticonPackDetail(emoticonPackUuid)
    }

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            val data = (uiState as UiState.Success).data
            viewModel.fetchPurchaseInfo(data.id)
        }
    }

    BackHandler(enabled = isDownloading) {
        // isDownloading이 true일 때 뒤로가기 버튼을 무시
        Toast.makeText(context, "다운로드 중에는 뒤로가기를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { if (!isDownloading) navController.navigateUp() },
                        enabled = !isDownloading // isDownloading이 true이면 비활성화
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showReportDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Report,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "https://share.openticon.store/$emoticonPackUuid"
                            )
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "공유하기"))
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
//                    if (isDownloading) {
//                        LinearProgressIndicator(
//                            progress = {
//                                downloadedEmoticonCount.toFloat() / totalEmoticonCount.toFloat()
//                            },
//                            modifier = Modifier.fillMaxWidth().
//                            padding(0.dp)
//                        )
//                    }
                    when (purchaseState) {
                        is UiState.Loading -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is UiState.Success -> {
                            val purchaseInfo = (purchaseState as UiState.Success).data

                            if (!purchaseInfo.purchased) {
                                // 테이블에 없음 (구매 안됨)
                                // 구매 버튼 표시
                                PrimaryActionButton(
                                    onClick = {
                                        if (!isLoggedIn) {
                                            // 로그인 안됨
                                            // 로그인 화면으로
                                            navController.navigate("login")
                                        } else {
                                            // 로그인 됨
                                            // 구매 처리
                                            showDialog = true
                                        }
                                    },
                                    text = "구매"
                                )
                            } else {
                                // 테이블에 있음 (구매함)
                                if (!purchaseInfo.downloaded) {
                                    // 다운로드 안 됨
                                    if (isDownloading) {
                                        // 다운로드 진행 중일 때 진행 표시기 표시
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight()
                                                .padding(16.dp)
                                                .height(30.dp)
                                                .clip(RoundedCornerShape(30.dp))
                                                .background(MaterialTheme.colorScheme.primaryContainer),
                                        ) {
                                            LinearProgressIndicator(
                                                drawStopIndicator = {},
                                                trackColor = MaterialTheme.colorScheme.primaryContainer,
                                                progress = { downloadedEmoticonCount.toFloat() / totalEmoticonCount.toFloat() },
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .fillMaxWidth(),
                                                color = MaterialTheme.colorScheme.primary,
                                            )
                                            Text(
                                                text = "${(downloadedEmoticonCount.toFloat() / totalEmoticonCount.toFloat() * 100).toInt()}%",
                                                color = if (downloadedEmoticonCount.toFloat() / totalEmoticonCount.toFloat() < 0.5f)
                                                    MaterialTheme.colorScheme.onSurface
                                                else
                                                    MaterialTheme.colorScheme.onPrimary,
                                                style = MaterialTheme.typography.bodySmall,
                                                modifier = Modifier.align(Alignment.Center) // 텍스트를 중앙에 정렬
                                            )
                                        }
                                    } else {
                                        // 다운로드 버튼 표시
                                        PrimaryActionButton(
                                            onClick = {
                                                if (!isDownloading) {
                                                    viewModel.downloadEmoticonPack(
                                                        packId = purchaseInfo.packId,
                                                        uuid = purchaseInfo.uuid
                                                    )
                                                }
                                            },
                                            text = "다운로드",
                                            enabled = true
                                        )
                                    }
                                } else {
                                    // 다운로드 완료
                                    PrimaryActionButton(
                                        onClick = {},
                                        text = "다운로드 완료",
                                        enabled = false
                                    )
                                }

                            }
                        }

                        is UiState.Error -> {
                            Text("구매 정보를 불러오는데 실패했습니다.")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        when (uiState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                val emoticonPack = (uiState as UiState.Success<EmoticonPackDetail>).data

                LazyColumn(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    item {
                        Spacer(Modifier.height(32.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(emoticonPack.thumbnail)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                imageLoader = ImageLoader.Builder(LocalContext.current)
                                    .components {
                                        add(GifDecoder.Factory())
                                    }
                                    .build(),
                                modifier = Modifier
                                    .size(128.dp),
                                placeholder = painterResource(R.drawable.loading_img),
                                error = painterResource(R.drawable.ic_broken_image),
                            )
                        }
                    }
                    item {
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = emoticonPack.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50)),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = if (emoticonPack.price == 0) "무료" else "${emoticonPack.price} 포인트",
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = emoticonPack.description,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                    items(emoticonPack.items.chunked(3).withIndex().toList()) { (rowIndex, row) ->
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (row.size == 3) {
                                for ((colIndex, item) in row.withIndex()) {
                                    val isSelected =
                                        selectedEmoticonIndex == Pair(rowIndex, colIndex)
                                    val scale by animateFloatAsState(
                                        targetValue = when {
                                            isSelected -> 1.25f
                                            selectedEmoticonIndex == null -> 1f
                                            else -> 0.75f
                                        }
                                    )

                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(item)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = null,
                                        imageLoader = ImageLoader.Builder(LocalContext.current)
                                            .components {
                                                add(GifDecoder.Factory())
                                            }
                                            .build(),
                                        modifier = Modifier
                                            .scale(scale)
//                                            .height(size)
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .clickable(
                                                indication = null, // 클릭 효과 제거
                                                interactionSource = remember { MutableInteractionSource() } // 상호작용 효과 제거
                                            ) {
                                                Log.d(
                                                    "EmoticonPackDetailScreen",
                                                    "Clicked: $rowIndex, $colIndex"
                                                )
                                                // 다른 곳을 클릭하면 선택 해제
                                                selectedEmoticonIndex =
                                                    if (isSelected) null else Pair(
                                                        rowIndex,
                                                        colIndex
                                                    )
                                            },
                                        placeholder = painterResource(R.drawable.loading_img),
                                        error = painterResource(R.drawable.ic_broken_image),
                                    )
                                }
                            } else {
                                for ((colIndex, item) in row.withIndex()) {
                                    val isSelected =
                                        selectedEmoticonIndex == Pair(rowIndex, colIndex)
                                    val scale by animateFloatAsState(
                                        targetValue = when {
                                            isSelected -> 1.25f
                                            selectedEmoticonIndex == null -> 1f
                                            else -> 0.75f
                                        }
                                    )

                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(item)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = null,
                                        imageLoader = ImageLoader.Builder(LocalContext.current)
                                            .components {
                                                add(GifDecoder.Factory())
                                            }
                                            .build(),
                                        modifier = Modifier
                                            .scale(scale)
//                                            .height(size)
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .clickable(
                                                indication = null, // 클릭 효과 제거
                                                interactionSource = remember { MutableInteractionSource() } // 상호작용 효과 제거
                                            ) {
                                                // 다른 곳을 클릭하면 선택 해제
                                                selectedEmoticonIndex =
                                                    if (isSelected) null else Pair(
                                                        rowIndex,
                                                        colIndex
                                                    )
                                            },
                                        placeholder = painterResource(R.drawable.loading_img),
                                        error = painterResource(R.drawable.ic_broken_image),
                                    )
                                }
                                for (i in row.size until 3) {
                                    Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    item {
                        Column {
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "제작",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            ListItem(
                                leadingContent = {
                                    if (emoticonPack.authorProfilePic.isBlank()) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .size(40.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Image(
                                                painter = painterResource(R.drawable.ic_launcher_background),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop
                                            )
                                            Image(
                                                painter = painterResource(R.drawable.ic_launcher_foreground),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    } else {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(emoticonPack.authorProfilePic)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .size(40.dp)
                                        )
                                    }
                                },
                                headlineContent = { Text(emoticonPack.authorNickname) },
                                supportingContent = { Text(emoticonPack.createdAt) },
                                trailingContent = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier.clickable {
                                    Log.d(
                                        "EmoticonPackDetailScreen",
                                        "${emoticonPack.authorId} clicked!!"
                                    )
                                    navController.navigate("writer/${emoticonPack.authorNickname}")
                                }
                            )
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("실패")
                }
            }
        }
    }

    if (purchaseState is UiState.Success && showDialog) {
        val purchaseInfo = (purchaseState as UiState.Success).data

        AlertDialog(
            icon = {
                Icon(
                    Icons.Outlined.Payments,
                    contentDescription = null
                )
            },
            title = {
                Text(text = "이모티콘 구매")
            },
            text = {
                Text(text = "이모티콘을 구매할까요?")
            },
            onDismissRequest = {
                showDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.purchaseEmoticonPack(packId = purchaseInfo.packId)
                        showDialog = false
                    }
                ) {
                    Text("구매")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("취소")
                }
            }
        )
    }

    if (showReportDialog) {
        ReportDialog(
            onDismissRequest = { showReportDialog = false },
            onConfirmation = { reason ->
                showReportDialog = false
                viewModel.reportPack(emoticonPackUuid, reason)
            }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.toastEvent.collectLatest {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun PrimaryActionButton(
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text(text = text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ScaffoldPreview() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Preview") }) },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text("body")
        }
    }
}

@Composable
fun ReportDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (reason: String) -> Unit
) {
    var reportReason by remember { mutableStateOf("") }
    val maxCharCount = 200
    val charCount = reportReason.length
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        icon = {
            Icon(Icons.Filled.Report, contentDescription = null)
        },
        title = {
            Text(text = "이모티콘 팩 신고")
        },
        text = {
            Column {
                Text(text = "부적절한 이모티콘 팩으로 신고할까요?")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = reportReason,
                    onValueChange = {
                        if (it.length <= maxCharCount) {
                            reportReason = it
                            isError = it.isBlank() // 입력 값이 비어 있으면 오류 상태로 표시
                        }
                    },
                    label = { Text("신고 사유 (필수)") },
                    placeholder = { Text("신고 사유를 입력하세요") },
                    isError = isError,
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = {
                        Text(
                            text = "$charCount/$maxCharCount",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }
                )
                if (isError) {
                    Text(
                        text = "신고 사유는 필수 입력 항목입니다.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    )
                }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (reportReason.isNotBlank()) {
                        onConfirmation(reportReason)
                    } else {
                        isError = true // 입력 값이 비어 있으면 오류 메시지 표시
                    }
                },
                enabled = reportReason.isNotBlank()
            ) {
                Text("신고")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("취소")
            }
        }
    )
}
