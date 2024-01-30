package org.company.app

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.MusicVideo
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.MusicVideo
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.sqldelight.db.SqlDriver
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.company.app.theme.AppTheme
import org.company.app.theme.LocalThemeIsDark
import org.company.app.ui.navigation.HomeTab
import org.company.app.ui.navigation.LibraryTab
import org.company.app.ui.navigation.ShortsTab
import org.company.app.ui.navigation.SubscriptionsTab
import org.company.app.ui.navigation.rails.NavigationItem
import org.company.app.ui.navigation.rails.NavigationSideBar
import `sql-delight`.db.YoutubeDatabase


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun App() = AppTheme {


    val items = listOf(
        NavigationItem(
            title = "Home",
            selectedIcon = Icons.Default.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews = false,
        ),
        NavigationItem(
            title = "Shorts",
            selectedIcon = Icons.Filled.MusicVideo,
            unselectedIcon = Icons.Outlined.MusicVideo,
            hasNews = true,
        ),
        NavigationItem(
            title = "Subscriptions",
            selectedIcon = Icons.Filled.Subscriptions,
            unselectedIcon = Icons.Outlined.Subscriptions,
            hasNews = false,
        ),
        NavigationItem(
            title = "Library",
            selectedIcon = Icons.Filled.LocalLibrary,
            unselectedIcon = Icons.Outlined.LocalLibrary,
            hasNews = false,
        ),
    )
    val windowClass = calculateWindowSizeClass()
    val showNavigationRail = windowClass.widthSizeClass != WindowWidthSizeClass.Compact
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    TabNavigator(HomeTab) { tabNavigator ->
        Scaffold(bottomBar = {
            if (!showNavigationRail) {
                BottomNavigation(
                    modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.ime),
                    backgroundColor = MaterialTheme.colorScheme.background,
                    contentColor = contentColorFor(Color.Red),
                    elevation = 8.dp
                ) {
                    TabItem(HomeTab)
                    TabItem(ShortsTab)
                    TabItem(SubscriptionsTab)
                    TabItem(LibraryTab)
                }
            }
        }) {
            Column(
                modifier = Modifier.fillMaxSize().padding(
                    bottom = it.calculateBottomPadding(),
                    start = if (showNavigationRail) 80.dp else 0.dp
                )
            ) {
                CurrentTab()
            }
        }
    }
    if (showNavigationRail) {
        NavigationSideBar(
            items = items,
            selectedItemIndex = selectedItemIndex,
            onNavigate = {
                selectedItemIndex = it
            }
        )

        Box(
            modifier = Modifier.fillMaxSize()
                .padding(start = 80.dp)
        ) {
            when (selectedItemIndex) {
                0 -> {

                }

                1 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        TabNavigator(ShortsTab)
                    }
                }

                2 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        TabNavigator(SubscriptionsTab)
                    }
                }

                3 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        TabNavigator(LibraryTab)
                    }
                }
            }
        }

    }

}

@Composable
fun RowScope.TabItem(tab: Tab) {
    var isDark by LocalThemeIsDark.current
    val tabNavigator = LocalTabNavigator.current
    BottomNavigationItem(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            .height(58.dp).clip(RoundedCornerShape(16.dp)),
        selected = tabNavigator.current == tab,
        onClick = {
            tabNavigator.current = tab
        },
        icon = {
            tab.options.icon?.let { painter ->
                Icon(
                    painter,
                    contentDescription = tab.options.title,
                    tint = if (tabNavigator.current == tab) Color.Red else if (isDark) Color.White else Color.Black
                )
            }
        },
        label = {
            tab.options.title.let { title ->
                Text(
                    title,
                    fontSize = 12.sp,
                    color = if (tabNavigator.current == tab) Color.Red else if (isDark) Color.White else Color.Black
                )
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
internal expect fun provideShortCuts()

@Composable
internal expect fun VideoPlayer(modifier: Modifier, url: String?, thumbnail: String?)

@Composable
internal expect fun ShortsVideoPlayer(url: String?)
internal expect fun UserRegion(): String

@Composable
internal expect fun Notify(message: String)

@Composable
internal expect fun ShareManager(title: String, videoUrl: String)

@Composable
internal expect fun isConnected(): Boolean

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class DriverFactory() {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): YoutubeDatabase {
    val driver = driverFactory.createDriver()
    val database = YoutubeDatabase(driver)
    return database
}