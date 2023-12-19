package org.company.app.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.company.app.data.model.search.Search
import org.company.app.domain.repository.Repository
import org.company.app.domain.usecases.ChannelState
import org.company.app.domain.usecases.SearchState
import org.company.app.domain.usecases.YoutubeState

class MainViewModel(private val repository: Repository) : ViewModel() {

    //Videos
    private val _videos = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
    val videos: StateFlow<YoutubeState> = _videos.asStateFlow()

    //Relevance
    private val _relevance = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
    val relevance: StateFlow<YoutubeState> = _relevance.asStateFlow()

    //Channel Details
    private val _channel = MutableStateFlow<ChannelState>(ChannelState.LOADING)
    val channelDetails: StateFlow<ChannelState> = _channel.asStateFlow()

    //Channel Details
    private val _channelBranding = MutableStateFlow<ChannelState>(ChannelState.LOADING)
    val channelBranding: StateFlow<ChannelState> = _channelBranding.asStateFlow()

    //Relevance Videos
    private val _relevance_videos = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
    val relevanceVideos: StateFlow<YoutubeState> = _relevance_videos.asStateFlow()

    //Search Videos
    private val _search = MutableStateFlow<SearchState>(SearchState.LOADING)
    val search: StateFlow<SearchState> = _search.asStateFlow()

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

    fun getRelevance() {
        viewModelScope.launch {
            _relevance.value = YoutubeState.LOADING
            try {
                val response = repository.getRelevance()
                _relevance.value = YoutubeState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _relevance.value = YoutubeState.ERROR(error)
            }

        }
    }

    fun getChannelDetails(channelId: String) {
        viewModelScope.launch {
            _channel.value = ChannelState.LOADING
            try {
                val response = repository.getChannelDetail(channelId)
                _channel.value = ChannelState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _channel.value = ChannelState.ERROR(error)
            }

        }
    }

    fun getRelevanceVideos() {
        viewModelScope.launch {
            _relevance_videos.value = YoutubeState.LOADING
            try {
                val response = repository.getRelevanceVideos()
                _relevance_videos.value = YoutubeState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _relevance_videos.value = YoutubeState.ERROR(error)
            }

        }
    }

    fun getSearch(query: String) {
        viewModelScope.launch {
            _search.value = SearchState.LOADING
            try {
                val response = repository.getSearch(query)
                _search.value = SearchState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _search.value = SearchState.ERROR(error)
            }

        }
    }

    fun getChanelBranding(channelId: String) {
        viewModelScope.launch {
            _channelBranding.value = ChannelState.LOADING
            try {
                val response = repository.getChannelBranding(channelId)
                _channelBranding.value = ChannelState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _channelBranding.value = ChannelState.ERROR(error)
            }

        }
    }

}