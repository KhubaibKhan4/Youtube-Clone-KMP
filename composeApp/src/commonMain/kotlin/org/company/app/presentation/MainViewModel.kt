package org.company.app.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.company.app.domain.repository.Repository
import org.company.app.domain.usecases.CategoriesState
import org.company.app.domain.usecases.ChannelState
import org.company.app.domain.usecases.CommentsState
import org.company.app.domain.usecases.SearchState
import org.company.app.domain.usecases.YoutubeState

class MainViewModel(private val repository: Repository) : ViewModel() {

    //Videos
    var _videos = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
        private set
    val videos: StateFlow<YoutubeState> = _videos.asStateFlow()

    //Relevance
    var _relevance = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
        private set
    val relevance: StateFlow<YoutubeState> = _relevance.asStateFlow()

    //Channel Details
    var _channel = MutableStateFlow<ChannelState>(ChannelState.LOADING)
        private set
    val channelDetails: StateFlow<ChannelState> = _channel.asStateFlow()

    //Channel Details
    var _channelBranding = MutableStateFlow<ChannelState>(ChannelState.LOADING)
        private set
    val channelBranding: StateFlow<ChannelState> = _channelBranding.asStateFlow()

    //Relevance Videos
    var _relevance_videos = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
        private set
    val relevanceVideos: StateFlow<YoutubeState> = _relevance_videos.asStateFlow()

    //Search Videos
     var _search = MutableStateFlow<SearchState>(SearchState.LOADING)
        private set
    val search: StateFlow<SearchState> = _search.asStateFlow()

    //Playlist Videos
    var _playlists = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
        private set
    val playlists: StateFlow<YoutubeState> = _playlists.asStateFlow()

    //Channel Sections
    var _channelSections = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
        private set
    val channelSections: StateFlow<YoutubeState> = _channelSections

    //Channel LiveStream
    var _channelLiveSteam = MutableStateFlow<SearchState>(SearchState.LOADING)
        private set
    val channelLiveStream: StateFlow<SearchState> = _channelLiveSteam.asStateFlow()

    //Channel Videos
    var _channelVideos = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
        private set
    val channelVideos: StateFlow<YoutubeState> = _channelVideos.asStateFlow()

    //Channel Community
     var _channelCommunity = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
        private set
    val channelCommunity: StateFlow<YoutubeState> = _channelCommunity.asStateFlow()

    //Video Comments
     var _videoComments = MutableStateFlow<CommentsState>(CommentsState.LOADING)
        private set
    val videoComments: StateFlow<CommentsState> = _videoComments.asStateFlow()

    //Channel Own Videos
    var _ownChannelVideos = MutableStateFlow<SearchState>(SearchState.LOADING)
        private set
    val ownChannelVideos: StateFlow<SearchState> = _ownChannelVideos.asStateFlow()

    //Video Categories
    var _videoCategories = MutableStateFlow<CategoriesState>(CategoriesState.LOADING)
        private set
    val videoCategories: StateFlow<CategoriesState> = _videoCategories.asStateFlow()

    //Single Video
    var _singleVideo = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
        private set
    val singleVideo: StateFlow<YoutubeState> = _singleVideo.asStateFlow()

    //multiple Videos
    var _multipleVideos = MutableStateFlow<YoutubeState>(YoutubeState.LOADING)
        private set
    val multipleVideos: StateFlow<YoutubeState> = _multipleVideos.asStateFlow()

    fun getVideosList(userRegion: String) {
        viewModelScope.launch {
            _videos.value = YoutubeState.LOADING
            try {
                val response = repository.getVideoList(userRegion)
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

    fun getSearch(query: String, userRegion: String) {
        viewModelScope.launch {
            _search.value = SearchState.LOADING
            try {
                val response = repository.getSearch(query, userRegion)
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

    fun getPlaylists(channelId: String) {
        viewModelScope.launch {
            _playlists.value = YoutubeState.LOADING
            try {
                val response = repository.getPlaylists(channelId)
                _playlists.value = YoutubeState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _playlists.value = YoutubeState.ERROR(error)
            }
        }
    }

    fun getChannelSections(channelId: String) {
        viewModelScope.launch {
            _channelSections.value = YoutubeState.LOADING
            try {
                val response = repository.getChannelSections(channelId)
                _channelSections.value = YoutubeState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _channelSections.value = YoutubeState.ERROR(error)
            }
        }
    }

    fun getChannelLiveStreams(channelId: String) {
        viewModelScope.launch {
            _channelLiveSteam.value = SearchState.LOADING
            try {
                val response = repository.getChannelLiveStreams(channelId)
                _channelLiveSteam.value = SearchState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _channelLiveSteam.value = SearchState.ERROR(error)
            }
        }
    }

    fun getChannelVideos(playlistId: String) {
        viewModelScope.launch {
            _channelVideos.value = YoutubeState.LOADING
            try {
                val response = repository.getChannelVideos(playlistId)
                _channelVideos.value = YoutubeState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _channelVideos.value = YoutubeState.ERROR(error)
            }
        }
    }

    fun getChannelCommunity(channelId: String) {
        viewModelScope.launch {
            _channelCommunity.value = YoutubeState.LOADING
            try {
                val response = repository.getChannelCommunity(channelId)
                _channelCommunity.value = YoutubeState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _channelCommunity.value = YoutubeState.ERROR(error)
            }
        }
    }

    fun getVideoComments(videoId: String, order: String) {
        viewModelScope.launch {
            _videoComments.value = CommentsState.LOADING
            try {
                val response = repository.getComments(videoId, order)
                _videoComments.value = CommentsState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _videoComments.value = CommentsState.ERROR(error)
            }
        }
    }

    fun getOwnChannelVideos(channelId: String) {
        viewModelScope.launch {
            _ownChannelVideos.value = SearchState.LOADING
            try {
                val response = repository.getOwnChannelVideos(channelId)
                _ownChannelVideos.value = SearchState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _ownChannelVideos.value = SearchState.ERROR(error)
            }
        }
    }

    fun getVideoCategories() {
        viewModelScope.launch {
            _videoCategories.value = CategoriesState.LOADING
            try {
                val response = repository.getVideoCategories()
                _videoCategories.value = CategoriesState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _videoCategories.value = CategoriesState.ERROR(error)
            }
        }
    }

    fun getSingleVideo(videoId: String) {
        viewModelScope.launch {
            _singleVideo.value = YoutubeState.LOADING
            try {
                val response = repository.getSingleVideoDetail(videoId)
                _singleVideo.value = YoutubeState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _singleVideo.value = YoutubeState.ERROR(error)
            }
        }
    }
    fun getMultipleVideo(videoId: String) {
        viewModelScope.launch {
            _multipleVideos.value = YoutubeState.LOADING
            try {
                val response = repository.getMultipleVideos(videoId)
                _multipleVideos.value = YoutubeState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _multipleVideos.value = YoutubeState.ERROR(error)
            }
        }
    }
}