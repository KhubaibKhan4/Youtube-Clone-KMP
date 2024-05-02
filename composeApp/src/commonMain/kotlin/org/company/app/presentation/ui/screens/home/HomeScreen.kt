package org.company.app.presentation.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import org.company.app.UserRegion
import org.company.app.domain.usecases.ResultState
import org.company.app.isConnected
import org.company.app.presentation.ui.components.common.ErrorBox
import org.company.app.presentation.ui.components.common.NoInternet
import org.company.app.presentation.ui.components.offline.OfflineList
import org.company.app.presentation.ui.components.shimmer.ShimmerEffectMain
import org.company.app.presentation.ui.components.video_list.VideosList
import org.company.app.presentation.viewmodel.MainViewModel
import org.koin.compose.koinInject

class HomeScreen() : Screen {
    @Composable
    override fun Content() {
        HomeContent()
    }
}

@Composable
fun HomeContent(
    viewModel: MainViewModel = koinInject<MainViewModel>(),
) {
    LaunchedEffect(Unit) {
        viewModel.getVideosList(UserRegion())
        //viewModel.getAllVideos()
    }
    if (isConnected(retry = {})) {
        val state by viewModel.videos.collectAsState()
        when (state) {
            is ResultState.LOADING -> {
                ShimmerEffectMain()
            }

            is ResultState.SUCCESS -> {
                val data = (state as ResultState.SUCCESS).response
                VideosList(data)
            }

            is ResultState.ERROR -> {
                val error = (state as ResultState.ERROR).error
                if (!isConnected(retry = {})) {
                    NoInternet()
                } else {
                    ErrorBox(error)
                }
            }
        }
    } else {
        val localState by viewModel.localVideos.collectAsState()
        when (localState) {
            is ResultState.ERROR -> {
                val error = (localState as ResultState.ERROR).error
                ErrorBox(error)
            }

            ResultState.LOADING -> {
                ShimmerEffectMain()
            }

            is ResultState.SUCCESS -> {
                val response = (localState as ResultState.SUCCESS).response
                OfflineList(response)
            }
        }
    }
}