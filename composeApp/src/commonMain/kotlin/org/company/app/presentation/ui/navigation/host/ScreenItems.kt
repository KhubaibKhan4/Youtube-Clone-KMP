package org.company.app.presentation.ui.navigation.host

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.MusicVideo
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.outlined.Audiotrack
import androidx.compose.material.icons.outlined.Details
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.MusicVideo
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ScreenItems(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
) {
    data object Home : ScreenItems(
        title = "Home",
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        hasNews = false
    )
    data object Shorts : ScreenItems(
        title = "Shorts",
        unselectedIcon = Icons.Outlined.MusicVideo,
        selectedIcon = Icons.Filled.MusicVideo,
        hasNews = false
    )
    data object Subscription : ScreenItems(
        title = "Subscription",
        unselectedIcon = Icons.Outlined.Subscriptions,
        selectedIcon = Icons.Filled.Subscriptions,
        hasNews = false
    )
    data object Library : ScreenItems(
        title = "Library",
        unselectedIcon = Icons.Outlined.LocalLibrary,
        selectedIcon = Icons.Filled.LocalLibrary,
        hasNews = false
    )
    data object DetailScreen : ScreenItems(
        title = "Detail",
        unselectedIcon = Icons.Outlined.Details,
        selectedIcon = Icons.Filled.Details,
        hasNews = false
    )
}
