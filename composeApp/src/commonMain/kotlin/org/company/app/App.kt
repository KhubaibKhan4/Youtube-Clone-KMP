package org.company.app

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.contentColorFor
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.company.app.theme.AppTheme
import org.company.app.ui.navigation.HomeTab
import org.company.app.ui.navigation.LibraryTab
import org.company.app.ui.navigation.ShortsTab
import org.company.app.ui.navigation.SubscruptionsTab

@Composable
internal fun App() = AppTheme {

    TabNavigator(HomeTab) { tabNavigator ->
        Scaffold(
            bottomBar = {
                BottomNavigation(
                    modifier = Modifier.fillMaxWidth().height(69.dp),
                    backgroundColor = MaterialTheme.colorScheme.background,
                    contentColor = contentColorFor(Color.Red),
                    elevation = 8.dp
                ) {
                    TabItem(HomeTab)
                    TabItem(ShortsTab)
                    TabItem(SubscruptionsTab)
                    TabItem(LibraryTab)
                }
            }
        ) {
            Column(modifier = Modifier.fillMaxSize().padding( bottom = it.calculateBottomPadding())) {
                CurrentTab()
            }
        }
    }


    /* Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {
         Navigator(HomeScreen())

         Row(
             horizontalArrangement = Arrangement.Center
         ) {
             Text(
                 text = "Login",
                 style = MaterialTheme.typography.titleMedium,
                 modifier = Modifier.padding(16.dp)
             )

             Spacer(modifier = Modifier.weight(1.0f))

             var isDark by LocalThemeIsDark.current
             IconButton(
                 onClick = { isDark = !isDark }
             ) {
                 Icon(
                     modifier = Modifier.padding(8.dp).size(20.dp),
                     imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                     contentDescription = null
                 )
             }
         }
     }*/
}

@Composable
fun RowScope.TabItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    BottomNavigationItem(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            .height(58.dp)
            .clip(RoundedCornerShape(16.dp)),
        selected = tabNavigator.current == tab,
        onClick = {
            tabNavigator.current = tab
        },
        icon = {
            tab.options.icon?.let { painter ->
                Icon(painter, contentDescription = tab.options.title,
                    tint = if (tabNavigator.current == tab) Color.Red else Color.Black
                )
            }
        },
        label = {
            tab.options.title.let { title ->
                Text(title, fontSize = 12.sp, color =if (tabNavigator.current == tab) Color.Red else Color.Black )
            }
        },
        enabled = true,
        alwaysShowLabel = true,
        interactionSource = MutableInteractionSource(),
        selectedContentColor = Color.Red,
        unselectedContentColor = Color.Black
    )
}

internal expect fun openUrl(url: String?)
@Composable
internal expect fun VideoPlayer(modifier: Modifier,url: String?, thumbnail:String?)