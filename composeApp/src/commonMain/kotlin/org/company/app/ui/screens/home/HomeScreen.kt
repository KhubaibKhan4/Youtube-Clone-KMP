package org.company.app.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import org.company.app.UserRegion
import org.company.app.data.repository.Repository
import org.company.app.domain.usecases.ResultState
import org.company.app.isConnected
import org.company.app.presentation.MainViewModel
import org.company.app.ui.components.common.ErrorBox
import org.company.app.ui.components.common.NoInternet
import org.company.app.ui.components.shimmer.ShimmerEffectMain
import org.company.app.ui.components.video_list.VideosList

class HomeScreen() : Screen {
    @Composable
    override fun Content() {
        val repository = remember { Repository() }
        val viewModel = remember { MainViewModel(repository) }
        var state by remember { mutableStateOf<ResultState<org.company.app.domain.model.videos.Youtube>>(ResultState.LOADING) }

        LaunchedEffect(Unit) {
            viewModel.getVideosList(UserRegion())
        }
        state = viewModel.videos.collectAsState().value
        when (state) {
            is ResultState.LOADING -> {
                /*LoadingBox()*/
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
    }
}