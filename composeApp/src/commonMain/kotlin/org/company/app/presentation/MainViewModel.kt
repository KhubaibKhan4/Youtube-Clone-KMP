package org.company.app.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.company.app.domain.repository.Repository
import org.company.app.domain.usecases.YoutubeState

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _videos = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
    val videos: StateFlow<YoutubeState> = _videos.asStateFlow()

    fun getVideosList() {
        viewModelScope.launch {
            _videos.value = YoutubeState.LOADING
            try {
                val response = repository.getVideoList()
                _videos.value = YoutubeState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _videos.value = YoutubeState.ERROR(error)
            }

        }
    }

}