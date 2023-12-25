package org.company.app.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.company.app.domain.repository.Repository
import org.company.app.domain.usecases.ChannelState
import org.company.app.domain.usecases.CommentsState
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

    //Playlist Videos
    private val _playlists = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
    val playlists: StateFlow<YoutubeState> = _playlists.asStateFlow()

    //Channel Sections
    private val _channelSections  = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
    val channelSections: StateFlow<YoutubeState> = _channelSections

    //Channel LiveStream
    private val _channelLiveSteam = MutableStateFlow<SearchState>(SearchState.LOADING)
    val channelLiveStream : StateFlow<SearchState> = _channelLiveSteam.asStateFlow()

    //Channel Videos
    private val _channelVideos = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
    val channelVideos : StateFlow<YoutubeState> = _channelVideos.asStateFlow()

    //Channel Community
    private val _channelCommunity = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
    val channelCommunity : StateFlow<YoutubeState> = _channelCommunity.asStateFlow()

    //Video Comments
    private val _videoComments = MutableStateFlow<CommentsState>(CommentsState.LOADING)
    val videoComments : StateFlow<CommentsState> = _videoComments.asStateFlow()

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

    fun getPlaylists(channelId: String){
        viewModelScope.launch {
            _playlists.value = YoutubeState.LOADING
            try {
                val response = repository.getPlaylists(channelId)
                _playlists.value = YoutubeState.SUCCESS(response)
            }catch (e: Exception){
                val error = e.message.toString()
                _playlists.value = YoutubeState.ERROR(error)
            }
        }
    }
    fun getChannelSections(channelId: String){
        viewModelScope.launch {
            _channelSections.value = YoutubeState.LOADING
            try {
                val response = repository.getChannelSections(channelId)
                _channelSections.value = YoutubeState.SUCCESS(response)
            }catch (e: Exception){
                val error = e.message.toString()
                _channelSections.value = YoutubeState.ERROR(error)
            }
        }
    }

    fun getChannelLiveStreams(channelId: String){
        viewModelScope.launch {
            _channelLiveSteam.value = SearchState.LOADING
            try {
                val response = repository.getChannelLiveStreams(channelId)
                _channelLiveSteam.value = SearchState.SUCCESS(response)
            }catch (e: Exception){
                val error = e.message.toString()
                _channelLiveSteam.value = SearchState.ERROR(error)
            }
        }
    }

    fun getChannelVideos(playlistId: String){
        viewModelScope.launch {
            _channelVideos.value = YoutubeState.LOADING
            try {
                val response = repository.getChannelVideos(playlistId)
                _channelVideos.value = YoutubeState.SUCCESS(response)
            }catch (e: Exception){
                val error = e.message.toString()
                _channelVideos.value = YoutubeState.ERROR(error)
            }
        }
    }

    fun getChannelCommunity(channelId: String){
        viewModelScope.launch {
            _channelCommunity.value = YoutubeState.LOADING
            try {
                val response = repository.getChannelCommunity(channelId)
                _channelCommunity.value = YoutubeState.SUCCESS(response)
            }catch (e: Exception){
                val error = e.message.toString()
                _channelCommunity.value = YoutubeState.ERROR(error)
            }
        }
    }

    fun getVideoComments(videoId: String){
        viewModelScope.launch {
            _videoComments.value = CommentsState.LOADING
            try {
                val response = repository.getVideoComments(videoId)
                _videoComments.value = CommentsState.SUCCESS(response)
            }catch (e: Exception){
                val error = e.message.toString()
                _videoComments.value = CommentsState.ERROR(error)
            }
        }
    }
}