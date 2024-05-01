package org.company.app.presentation.ui.screens.shorts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import org.company.app.UserRegion
import org.company.app.domain.model.videos.Youtube
import org.company.app.domain.usecases.ResultState
import org.company.app.isConnected
import org.company.app.presentation.viewmodel.MainViewModel
import org.company.app.presentation.ui.components.common.ErrorBox
import org.company.app.presentation.ui.components.common.NoInternet
import org.company.app.presentation.ui.components.shimmer.ShimmerEffectShorts
import org.company.app.presentation.ui.components.shorts.ShortList
import org.koin.compose.koinInject

class ShortScreen() : Screen {
    @Composable
    override fun Content() {
        ShortContent()
    }
}

@Composable
fun ShortContent(viewModel: MainViewModel = koinInject<MainViewModel>()) {
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
        is ResultState.LOADING -> {
            ShimmerEffectShorts()
        }

        is ResultState.SUCCESS -> {
            val data = (state as ResultState.SUCCESS).response
            shortsData = data
        }

        is ResultState.ERROR -> {
            val Error = (state as ResultState.ERROR).error
            if (!isConnected(retry = {})) {
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