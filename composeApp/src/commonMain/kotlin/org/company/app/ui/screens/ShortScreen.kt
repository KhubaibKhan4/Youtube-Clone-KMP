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
import org.company.app.data.repositoryimp.Repository
import org.company.app.domain.usecases.ResultState
import org.company.app.isConnected
import org.company.app.presentation.MainViewModel
import org.company.app.ui.components.ErrorBox
import org.company.app.ui.components.NoInternet
import org.company.app.ui.components.ShimmerEffectShorts
import org.company.app.ui.components.ShortList

class ShortScreen(

) : Screen {
    @Composable
    override fun Content() {
        val repository = remember { Repository() }
        val viewModel = remember { MainViewModel(repository) }
        var shortsData by remember { mutableStateOf<org.company.app.domain.model.videos.Youtube?>(null) }
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
            is ResultState.LOADING -> {
                ShimmerEffectShorts()
            }

            is ResultState.SUCCESS -> {
                val data = (state as ResultState.SUCCESS).response
                shortsData = data
            }

            is ResultState.ERROR -> {
                val Error = (state as ResultState.ERROR).error
                if (!isConnected()) {
                    NoInternet()
                } else {
                    ErrorBox(Error)
                }
            }
        }

        shortsData?.let { shorts ->
            ShortList(shorts)
        }
    }
}