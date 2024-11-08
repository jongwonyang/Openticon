package io.ssafy.openticon.ui.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import io.ssafy.openticon.R
import io.ssafy.openticon.domain.model.SearchEmoticonPacksListItem
import io.ssafy.openticon.ui.component.SearchBar
import io.ssafy.openticon.ui.viewmodel.SearchScreenViewModel
import io.ssafy.openticon.ui.viewmodel.Sort

@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel(),
    navController: NavController
) {
    val selectedKey by viewModel.searchKey.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val searchResult by viewModel.searchResult.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchSort by viewModel.searchSort.collectAsState()
    val listState = rememberLazyListState()

    val imageUrl by viewModel.selectedImageUri.collectAsState()

    var isImageSearch by remember { mutableStateOf(false) }
    var isSortExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val contentResolver = context.contentResolver

    Column {
        Spacer(Modifier.height(16.dp))

        SearchBar(
            selectedKey = selectedKey,
            onKeyChange = { viewModel.onSearchKeyChange(it) },
            searchText = searchText,
            onTextChange = { viewModel.onSearchTextChange(it) },
            listState = listState
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "검색결과",
                style = MaterialTheme.typography.labelMedium,
            )
            Column {
                TextButton(onClick = { isSortExpanded = !isSortExpanded }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = searchSort.displayName,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                DropdownMenu(
                    expanded = isSortExpanded,
                    onDismissRequest = { isSortExpanded = false }
                ) {
                    Sort.entries.forEach {
                        DropdownMenuItem(
                            text = { Text(it.displayName) },
                            onClick = {
                                viewModel.onSortChange(it, contentResolver, context, listState)
                                isSortExpanded = false
                            }
                        )
                    }
                }
            }
        }

        if (searchResult.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLoading) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Text(
                        text = "\u0028\u0020\uaa5c\u0020\u1bc5\u0020\uaa5c\u003b\u0029", // ( ꩜ ᯅ ꩜;)
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "검색 결과가 없습니다.",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        } else {
            LazyColumn(state = listState) {
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

        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collect { lastVisibleItemIndex ->
                    if (lastVisibleItemIndex == searchResult.size - 1 && !isLoading) {
                        if (!isImageSearch) {
                            viewModel.loadMoreSearchResult()
                        } else {
                            Log.d("imageSearchScroll", imageUrl.toString())
                            viewModel.loadMoreImageSearchResult(
                                contentResolver
                            )
                        }
                    }
                }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen(
        navController = rememberNavController()
    )
}

@Composable
fun SearchResultItem(
    item: SearchEmoticonPacksListItem,
    navController: NavController
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable { navController.navigate("emoticonPack/${item.uuid}") }
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .height(IntrinsicSize.Min)
            ) {
                AsyncImage(
                    model = item.thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.loading_img),
                    error = painterResource(R.drawable.ic_broken_image),
                )
                Spacer(Modifier.width(16.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = item.author,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}