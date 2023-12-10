package org.company.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun TopBar() {
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
                onClick = { /* Handle account button click */ }
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