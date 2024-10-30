package io.ssafy.openticon.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import io.ssafy.openticon.ui.sample.EmoticonPackSampleData
import io.ssafy.openticon.ui.viewmodel.SearchScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel(),
    navController: NavController
) {
    val keyItems = listOf("제목", "태그", "작가")

    val key = viewModel.key.collectAsState()
    val query = viewModel.query.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.weight(0.4f)
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = key.value,
                    onValueChange = {},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    keyItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                viewModel.onKeyChange(item)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = query.value,
                onValueChange = { viewModel.onQueryChange(it) },
                maxLines = 1,
                singleLine = true,
                modifier = Modifier.weight(0.6f)
            )

            IconButton(
                onClick = {},
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn() {
            items(
                items = EmoticonPackSampleData.latestPacks,
                key = { it.id }
            ) { item ->
                Column {
                    ListItem(
                        leadingContent = {
                            AsyncImage(
                                model = item.thumbnail,
                                contentDescription = null
                            )
                        },
                        headlineContent = { Text(item.title) },
                        supportingContent = { Text(item.author) },
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.navigate("emoticonPack/${item.id}")
                            }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}