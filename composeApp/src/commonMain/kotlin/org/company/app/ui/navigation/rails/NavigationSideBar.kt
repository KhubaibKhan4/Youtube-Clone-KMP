package org.company.app.ui.navigation.rails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator

@Composable
fun NavigationSideBar(
    items: List<NavigationItem>, selectedItemIndex: Int, onNavigate: (Int) -> Unit
) {
    var isTitleVisible by remember { mutableStateOf(false) }
    NavigationRail(
        header = {
            IconButton(onClick = {
                isTitleVisible = !isTitleVisible
            }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
        },
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            items.forEachIndexed { index, item ->
                NavigationRailItem(
                    selected = selectedItemIndex == index,
                    onClick = {
                        onNavigate(index)
                    },
                    icon = {
                        NavigationIcon(
                            item = item, selected = selectedItemIndex == index
                        )
                    },
                    label = {
                        if (isTitleVisible) {
                            Text(
                                text = item.title,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize
                            )
                        } else {
                            // TODO: Nothing to be here
                        }

                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationIcon(
    item: NavigationItem, selected: Boolean
) {
    BadgedBox(badge = {
        if (item.badgeCount != null) {
            Badge {
                Text(text = item.badgeCount.toString())
            }
        } else if (item.hasNews) {
            Badge()
        }
    }) {
        Icon(
            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.title
        )
    }
}