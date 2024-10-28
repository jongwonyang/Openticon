package io.ssafy.openticon.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.ui.graphics.vector.ImageVector
import io.ssafy.openticon.R

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    data object Store : BottomNavItem(
        "store",
        Icons.Outlined.Storefront,
        R.string.navigation_store
    )

    data object Search : BottomNavItem(
        "search",
        Icons.Outlined.Search,
        R.string.navigation_search
    )

    data object MyEmoticons : BottomNavItem(
        "my_emoticons",
        Icons.Outlined.EmojiEmotions,
        R.string.navigation_my_emoticons
    )

    data object Profile : BottomNavItem(
        "profile",
        Icons.Outlined.Person,
        R.string.navigation_profile
    )
}