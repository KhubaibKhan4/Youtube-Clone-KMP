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
import org.company.app.domain.repository.Repository
import org.company.app.ui.components.ErrorBox
import org.company.app.ui.components.LoadingBox
import org.company.app.ui.components.VideosList
import org.company.app.domain.usecases.YoutubeState
import org.company.app.presentation.MainViewModel

class HomeScreen() : Screen {
    @Composable
    override fun Content() {
        val repository = remember { Repository() }
        val viewModel = remember { MainViewModel(repository) }
        var state by remember { mutableStateOf<YoutubeState>(YoutubeState.LOADING) }

        LaunchedEffect(Unit) {
            viewModel.getVideosList(UserRegion())
        }
        state = viewModel.videos.collectAsState().value

        when (state) {
            is YoutubeState.LOADING -> {
                LoadingBox()
            }

            is YoutubeState.SUCCESS -> {
                val data = (state as YoutubeState.SUCCESS).youtube
                VideosList(data)
            }

            is YoutubeState.ERROR -> {
                val error = (state as YoutubeState.ERROR).error
                ErrorBox(error)
            }
        }
    }
}