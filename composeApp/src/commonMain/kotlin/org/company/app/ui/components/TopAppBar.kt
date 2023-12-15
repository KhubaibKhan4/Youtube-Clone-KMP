package org.company.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.company.app.theme.LocalThemeIsDark
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class, InternalComposeApi::class)
@Composable
fun TopBar() {
    var isDark by LocalThemeIsDark.current
    TopAppBar(
        title = {
            Image(org.jetbrains.compose.resources.painterResource("logo.webp"), contentDescription = null,
                modifier = Modifier.size(120.dp))
        },
        actions = {
            IconButton(onClick = {}){
                Icon(imageVector = Icons.Default.Cast, contentDescription = "Cast")
            }
            IconButton(
                onClick = { isDark = !isDark }
            ) {
                Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "Account")
            }
            IconButton(
                onClick = { /* Handle search button click */ }
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        }
    )
}