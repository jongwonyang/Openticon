package io.ssafy.openticon.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import io.ssafy.openticon.R
import io.ssafy.openticon.ui.viewmodel.WriterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriterScreen(
    navController: NavController,
    nickname: String,
    viewModel: WriterViewModel = hiltViewModel()
) {
    val memberEntity by viewModel.memberEntity.collectAsState()
    val searchResult by viewModel.searchResult.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.resetState()
        viewModel.fetchWriterInfo(nickname)
        viewModel.loadMoreSearchResult(nickname)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "작가의 작품", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                item {
                    when (uiState) {
                        is WriterViewModel.UiState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is WriterViewModel.UiState.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "작가 정보를 불러오는 중 오류가 발생했습니다.",
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 16.sp
                                )
                            }
                        }
                        is WriterViewModel.UiState.Success -> {
                            // 작가 정보 카드
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surface),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    // 프로필 이미지
                                    Box(
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.secondaryContainer)
                                            .border(
                                                width = 3.dp,
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = CircleShape
                                            )
                                    ) {
                                        if (memberEntity?.profile_image.isNullOrEmpty()) {
                                            Image(
                                                painter = painterResource(R.drawable.ic_launcher_foreground),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        } else {
                                            AsyncImage(
                                                model = memberEntity?.profile_image,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                placeholder = painterResource(R.drawable.loading_img),
                                                error = painterResource(R.drawable.ic_broken_image),
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    // 작가 닉네임
                                    Text(
                                        text = memberEntity?.nickname ?: "",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    // 작가 소개 텍스트
                                    Text(
                                        text = memberEntity?.bio ?: "상태 메세지가 없습니다.",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 작품 리스트 섹션
                item {
                    Text(
                        text = "작가의 작품 모음",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (searchResult.isEmpty() && !isLoading) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "( ꩜ ᯅ ꩜;)", color = MaterialTheme.colorScheme.secondary)
                            Spacer(Modifier.height(4.dp))
                            Text(text = "검색 결과가 없습니다.", color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                } else {
                    items(
                        items = searchResult,
                        key = { it.id }
                    ) { item ->
                        SearchResultItem(
                            item = item,
                            navController = navController
                        )
                    }

                    if (isLoading) {
                        item {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    )
}
