package io.ssafy.openticon.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import io.ssafy.openticon.ui.viewmodel.SearchKey
import io.ssafy.openticon.ui.viewmodel.SearchScreenViewModel

@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel(),
    navController: NavController
) {
    val selectedKey by viewModel.searchKey.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val searchResult by viewModel.searchResult.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()

    var isImageSearch by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val contentResolver = context.contentResolver

    Column {
        Spacer(Modifier.height(16.dp))

        if(!isImageSearch){
            SearchBar(
                selectedKey = selectedKey,
                onKeyChange = { viewModel.onSearchKeyChange(it) },
                searchText = searchText,
                onTextChange = { viewModel.onSearchTextChange(it) },
                navController = navController,
                isImageSearch = isImageSearch,  // Pass the state here
                onImageSearchToggle = { isImageSearch = it }
            )
        }
        else{
            ImageSearchScreen(
                isImageSearch = isImageSearch,  // Pass the state here
                onImageSearchToggle = { isImageSearch = it },
                contentResolver = contentResolver
            )
        }

        Text(
            text = "검색결과",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(state = listState) {
            items(
                items = searchResult,
                key = { it.id }
            ) { item ->
                ListItem(
                    leadingContent = {
                        AsyncImage(
                            model = item.thumbnail,
                            contentDescription = null,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    },
                    headlineContent = { Text(item.title) },
                    supportingContent = { Text(item.author) },
                    modifier = Modifier
                        .clickable {
                            navController.navigate("emoticonPack/${item.id}")
                        }
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

        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collect { lastVisibleItemIndex ->
                    if (lastVisibleItemIndex == searchResult.size - 1 && !isLoading) {
                        if(!isImageSearch) {
                            viewModel.loadMoreSearchResult()
                        }
                        else{
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
fun SearchBar(
    selectedKey: SearchKey,
    onKeyChange: (SearchKey) -> Unit,
    searchText: String,
    onTextChange: (String) -> Unit,
    viewModel: SearchScreenViewModel = hiltViewModel(),
    navController: NavController,
    isImageSearch: Boolean,  // Accept the isImageSearch state
    onImageSearchToggle: (Boolean) -> Unit  // Callback to toggle image search
) {
    val focusManager = LocalFocusManager.current
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.CenterStart
    ) {
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
                        viewModel.search()
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
                    viewModel.search()
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
                    onImageSearchToggle(!isImageSearch)
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
    }

}