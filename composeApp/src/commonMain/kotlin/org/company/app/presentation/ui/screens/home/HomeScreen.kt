package org.company.app.presentation.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeContent(
    viewModel: MainViewModel = koinInject<MainViewModel>(),
) {
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        viewModel.getVideosList(UserRegion())
        delay(1500)
        refreshing = false
    }

    val pullRefreshState = rememberPullRefreshState(refreshing, ::refresh)

    Box(
        Modifier
            .fillMaxWidth()
            .pullRefresh(pullRefreshState),
        contentAlignment = Alignment.Center
    ) {
        if (!refreshing) {
            if (isConnected(retry = {})) {
                LaunchedEffect(Unit) {
                    viewModel.getVideosList(UserRegion())
                }
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
                            if (error.contains("Unable to resolve host \"www.googleapis.com\": No address associated with hostname")) {
                                NoInternet()
                            } else {
                                ErrorBox(error)
                            }
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
        } else {
            ShimmerEffectMain()
        }
        PullRefreshIndicator(
            refreshing, pullRefreshState,
            Modifier.wrapContentSize().align(Alignment.TopCenter)
        )
    }
}