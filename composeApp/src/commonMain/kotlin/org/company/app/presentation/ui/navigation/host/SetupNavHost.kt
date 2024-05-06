package org.company.app.presentation.ui.navigation.host

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.company.app.presentation.ui.screens.home.HomeScreen
import org.company.app.presentation.ui.screens.library.LibraryScreen
import org.company.app.presentation.ui.screens.shorts.ShortContent
import org.company.app.presentation.ui.screens.subscriptions.SubscriptionScreen

@Composable
fun SetupNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = ScreenItems.Home.title) {
        composable(ScreenItems.Home.title) {
            HomeScreen()
        }
        composable(ScreenItems.Shorts.title) {
            ShortContent()
        }
        composable(ScreenItems.Subscription.title) {
            SubscriptionScreen()
        }
        composable(ScreenItems.Library.title) {
            LibraryScreen()
        }
    }
}