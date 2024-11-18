package io.ssafy.openticon.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.ssafy.openticon.ui.viewmodel.EmoticonAllViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmoticonAllScreen(
    navController: NavController,
    type: String?,
    tag: String?,
    viewModel: EmoticonAllViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val emoticonPack by viewModel.emoticonPack.collectAsState()
    val lastPageReached by viewModel.lastPageReached.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.fetchEmoticonPack(type ?: "popular", tag, isLoadMore = false)
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1
        }.collect { isAtEnd ->
            if (isAtEnd && !lastPageReached) {
                viewModel.fetchEmoticonPack(type ?: "popular", tag, isLoadMore = true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if (type == "new") "신규" else if (type == "popular") "인기" else ("#" + tag))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(

                )
            )
        },
        content = { paddingValues ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(paddingValues)
//                    .padding(top = 16.dp)
            ) {
                item {
                    Spacer(Modifier.height(16.dp))
                }
                // 검색 결과가 없는 경우 메시지 표시
                if (emoticonPack.isEmpty() && !isLoading) {
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
                    // 검색 결과 표시
                    items(
                        items = emoticonPack,
                        key = { it.id }
                    ) { item ->
                        SearchResultItem(
                            item = item,
                            navController = navController
                        )
                    }

                    // 스크롤 시 추가 로딩 표시
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
