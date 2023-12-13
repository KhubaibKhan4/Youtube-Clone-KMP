package org.company.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.company.app.ui.components.ErrorBox

object SubscruptionsTab : Tab {
    @Composable
    override fun Content() {
        ErrorBox("Subscription YouTube")
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