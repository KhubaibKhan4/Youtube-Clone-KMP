package org.company.app.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import org.company.app.UserRegion
import org.company.app.data.model.videos.Youtube
import org.company.app.domain.repository.Repository
import org.company.app.domain.usecases.YoutubeState
import org.company.app.presentation.MainViewModel
import org.company.app.ui.components.ErrorBox
import org.company.app.ui.components.ShimmerEffectShorts
import org.company.app.ui.components.ShortList

class ShortScreen(

) : Screen {
    @Composable
    override fun Content() {
        val repository = remember { Repository() }
        val viewModel = remember { MainViewModel(repository) }
        var shortsData by remember { mutableStateOf<Youtube?>(null) }
        /* UnComment this, If you want to use the Dark Theme
        When User enter the Shorts Screen
        var isDark by LocalThemeIsDark.current
        isDark = isSystemInDarkTheme()
        isDark = true
        DisposableEffect(isDark){
            onDispose {
                isDark = false
            }
        }*/
        LaunchedEffect(Unit) {
            viewModel.getVideosList(UserRegion())
        }
        val state by viewModel.videos.collectAsState()
        when (state) {
            is YoutubeState.LOADING -> {
                ShimmerEffectShorts()
            }

            is YoutubeState.SUCCESS -> {
                val data = (state as YoutubeState.SUCCESS).youtube
                shortsData = data
            }

            is YoutubeState.ERROR -> {
                val Error = (state as YoutubeState.ERROR).error
                ErrorBox(Error)
            }
        }

        shortsData?.let { shorts ->
            ShortList(shorts)
        }
    }
}