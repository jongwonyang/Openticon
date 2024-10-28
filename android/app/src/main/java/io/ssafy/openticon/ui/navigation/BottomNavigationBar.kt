package io.ssafy.openticon.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.ssafy.openticon.R

@Composable
fun BottomNavigationBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        stringResource(R.string.navigation_store),
        stringResource(R.string.navigation_search),
        stringResource(R.string.navigation_my_emoticons),
        stringResource(R.string.navigation_profile)
    )
    val icons = listOf(
        Icons.Filled.Storefront,
        Icons.Outlined.Search,
        Icons.Outlined.EmojiEmotions,
        Icons.Outlined.Person
    )

    NavigationBar(modifier = modifier) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        icons[index],
                        contentDescription = item
                    )
                },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}
