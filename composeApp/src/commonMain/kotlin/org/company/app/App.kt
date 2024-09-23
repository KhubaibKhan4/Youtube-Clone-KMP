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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
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
import io.ktor.client.HttpClientConfig
import kotlinx.coroutines.flow.Flow
import org.company.app.di.appModule
import org.company.app.presentation.ui.navigation.rails.items.NavigationItem
import org.company.app.presentation.ui.navigation.rails.navbar.NavigationSideBar
import org.company.app.presentation.ui.navigation.tabs.home.HomeTab
import org.company.app.presentation.ui.navigation.tabs.library.LibraryTab
import org.company.app.presentation.ui.navigation.tabs.shorts.ShortsTab
import org.company.app.presentation.ui.navigation.tabs.subscriptions.SubscriptionsTab
import org.company.app.theme.AppTheme
import org.company.app.theme.LocalThemeIsDark
import org.koin.compose.KoinApplication
import org.koin.compose.module.rememberKoinModules


@Composable
internal fun App() = AppTheme {
    KoinApplication(
        application = {
            modules(appModule)
        }
    ) {
        AppContent()
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AppContent() {
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
                NavigationBar(
                    modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.ime),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = contentColorFor(Color.Red),
                    tonalElevation = 8.dp
                ) {
                    TabItem(HomeTab)
                    TabItem(ShortsTab)
                    TabItem(SubscriptionsTab)
                    TabItem(LibraryTab)
                }
            }
        }) {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
                    .padding(
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
    val isDark by LocalThemeIsDark.current
    val tabNavigator = LocalTabNavigator.current
    NavigationBarItem(
        modifier = Modifier
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
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Red,
            unselectedIconColor = Color.Black,
            indicatorColor = Color.LightGray
        ),
    )
}

internal expect fun openUrl(url: String?)
@Composable
internal expect fun showDialog(title: String,message: String)
@Composable
internal expect fun provideShortCuts()

@Composable
internal expect fun VideoPlayer(modifier: Modifier, url: String?, thumbnail: String?)

@Composable
internal expect fun ShortsVideoPlayer(url: String?, modifier: Modifier)
internal expect fun UserRegion(): String

@Composable
internal expect fun ShareManager(title: String, videoUrl: String)

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class DriverFactory() {
    fun createDriver(): SqlDriver
}

expect class VideoDownloader() {
    suspend fun downloadVideo(url: String, onProgress: (Float, String) -> Unit): String
}
expect fun HttpClientConfig<*>.setupHttpCache()