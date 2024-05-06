package org.company.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.cash.sqldelight.db.SqlDriver
import org.company.app.presentation.ui.navigation.host.ScreenItems
import org.company.app.presentation.ui.navigation.host.SetupNavHost
import org.company.app.presentation.ui.navigation.rails.items.NavigationItem
import org.company.app.presentation.ui.navigation.rails.navbar.NavigationSideBar
import org.company.app.presentation.ui.screens.home.HomeScreen
import org.company.app.presentation.ui.screens.library.LibraryScreen
import org.company.app.presentation.ui.screens.shorts.ShortScreen
import org.company.app.presentation.ui.screens.subscriptions.SubscriptionScreen
import org.company.app.theme.AppTheme


@Composable
internal fun App() = AppTheme {
    AppContent()
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
    val navController = rememberNavController()


    Scaffold(bottomBar = {
        if (!showNavigationRail) {
            BottomBar(navController = navController)
        }
    }) {
        Column(
            modifier = Modifier.fillMaxSize().padding(
                bottom = it.calculateBottomPadding(),
                start = if (showNavigationRail) 80.dp else 0.dp
            )
        ) {
            SetupNavHost(navController)
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
                    HomeScreen(navController)
                }

                1 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        ShortScreen()
                    }
                }

                2 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        SubscriptionScreen(navController)
                    }
                }

                3 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        LibraryScreen(navController)
                    }
                }
            }
        }

    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val bottomItems = listOf(
        ScreenItems.Home,
        ScreenItems.Shorts,
        ScreenItems.Subscription,
        ScreenItems.Library,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar {
        bottomItems.forEach { screenItems ->
            if (currentDestination != null) {
                AddItem(
                    screen = screenItems,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: ScreenItems,
    currentDestination: NavDestination,
    navController: NavHostController,
) {
    val isSelected = currentDestination.hierarchy?.any { it.route == screen.title } == true
    NavigationBarItem(
        selected = isSelected,
        onClick = {
            navController.navigate(screen.title) {
                popUpTo(navController.graph.route.toString())
                launchSingleTop = true
            }
        },
        icon = {
            Icon(
                imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                contentDescription = ""
            )
        },
        label = { Text(text = screen.title) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Red,
            selectedTextColor = Color.Red,
            indicatorColor = Color.Transparent
        )
    )
}

internal expect fun openUrl(url: String?)

@Composable
internal expect fun provideShortCuts()

@Composable
internal expect fun VideoPlayer(modifier: Modifier, url: String?, thumbnail: String?)

@Composable
internal expect fun ShortsVideoPlayer(url: String?, modifier: Modifier)
internal expect fun UserRegion(): String

@Composable
internal expect fun Notify(message: String)

@Composable
internal expect fun ShareManager(title: String, videoUrl: String)

@Composable
internal expect fun isConnected(retry: () -> Unit): Boolean

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class DriverFactory() {
    fun createDriver(): SqlDriver
}