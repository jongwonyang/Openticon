package io.ssafy.openticon.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.ssafy.openticon.R

@Composable
fun BottomNavigationBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf(
        R.string.navigation_store,
        R.string.navigation_search,
        R.string.navigation_my_emoticons,
        R.string.navigation_profile
    )
    val icons = listOf(
        Icons.Outlined.Storefront,
        Icons.Outlined.Search,
        Icons.Outlined.EmojiEmotions,
        Icons.Outlined.Person
    )
    NavigationBar {
        items.forEachIndexed { index, item ->
            val label = stringResource(item)
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = label
                    )
                },
                label = { Text(label) },
                selected = index == selectedItem,
                onClick = { onItemSelected(index) }
            )
        }
    }
}
