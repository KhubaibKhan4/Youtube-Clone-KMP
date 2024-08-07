package org.company.app.presentation.ui.navigation.tabs.library

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.company.app.presentation.ui.screens.library.LibraryScreen

object LibraryTab : Tab {
    @Composable
    override fun Content() {
        Navigator(LibraryScreen())
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.LocalLibrary)
            val title = "Library"
            val index: UShort = 3u

            return TabOptions(
                index, title, icon
            )
        }
}