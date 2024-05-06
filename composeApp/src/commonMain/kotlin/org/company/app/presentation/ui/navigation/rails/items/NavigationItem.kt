package org.company.app.presentation.ui.navigation.rails.items

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
)
