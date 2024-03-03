package org.company.app.ui.navigation.tabs.subscriptions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.company.app.ui.screens.subscriptions.SubscriptionScreen

object SubscriptionsTab : Tab {
    @Composable
    override fun Content() {
        Navigator(SubscriptionScreen())
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Subscriptions)
            val title = "Subscription"
            val index: UShort = 2u

            return TabOptions(
                index, title, icon
            )
        }
}