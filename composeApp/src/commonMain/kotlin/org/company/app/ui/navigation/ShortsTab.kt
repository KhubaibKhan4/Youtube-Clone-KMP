package org.company.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicVideo
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.company.app.ui.components.ErrorBox
import org.company.app.ui.screens.HomeScreen

object ShortsTab : Tab {
    @Composable
    override fun Content() {
        ErrorBox("Shorts YouTube")
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.MusicVideo)
            val title = "Shorts"
            val index: UShort = 1u

            return TabOptions(
                index, title, icon
            )
        }
}