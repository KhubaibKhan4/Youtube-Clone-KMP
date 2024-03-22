package org.company.app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.company.app.UserRegion
import org.company.app.domain.usecases.ResultState
import org.company.app.isConnected
import org.company.app.presentation.MainViewModel
import org.company.app.theme.LocalThemeIsDark
import org.company.app.ui.components.common.ErrorBox
import org.company.app.ui.components.common.NoInternet
import org.company.app.ui.components.custom_image.NetworkImage
import org.company.app.ui.components.offline.OfflineList
import org.company.app.ui.components.shimmer.ShimmerEffectMain
import org.company.app.ui.components.video_list.VideosList
import org.company.app.ui.components.video_list.formatVideoDuration
import org.company.app.ui.components.video_list.formatViewCount
import org.company.app.ui.components.video_list.getFormattedDate
import org.koin.compose.koinInject
import sqldelight.db.YoutubeEntity

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
        viewModel.getAllVideos()
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