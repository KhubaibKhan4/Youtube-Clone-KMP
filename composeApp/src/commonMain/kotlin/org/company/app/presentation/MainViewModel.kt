package org.company.app.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.company.app.Res
import org.company.app.data.model.categories.VideoCategories
import org.company.app.data.model.channel.Channel
import org.company.app.data.model.comments.Comments
import org.company.app.data.model.search.Search
import org.company.app.data.model.videos.Youtube
import org.company.app.domain.repository.Repository
import org.company.app.domain.usecases.ResultState

class MainViewModel(private val repository: Repository) : ViewModel() {

    //Videos
    var _videos = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
        private set
    val videos: StateFlow<ResultState<Youtube>> = _videos.asStateFlow()

    //Relevance
    var _relevance = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
        private set
    val relevance: StateFlow<ResultState<Youtube>> = _relevance.asStateFlow()

    //Channel Details
    var _channel = MutableStateFlow<ResultState<Channel>>(ResultState.LOADING)
        private set
    val channelDetails: StateFlow<ResultState<Channel>> = _channel.asStateFlow()

    //Channel Details
    var _channelBranding = MutableStateFlow<ResultState<Channel>>(ResultState.LOADING)
        private set
    val channelBranding: StateFlow<ResultState<Channel>> = _channelBranding.asStateFlow()

    //Relevance Videos
    var _relevance_videos = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
        private set
    val relevanceVideos: StateFlow<ResultState<Youtube>> = _relevance_videos.asStateFlow()

    //Search Videos
     var _search = MutableStateFlow<ResultState<Search>>(ResultState.LOADING)
        private set
    val search: StateFlow<ResultState<Search>> = _search.asStateFlow()

    //Playlist Videos
    var _playlists = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
        private set
    val playlists: StateFlow<ResultState<Youtube>> = _playlists.asStateFlow()

    //Channel Sections
    var _channelSections = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
        private set
    val channelSections: StateFlow<ResultState<Youtube>> = _channelSections

    //Channel LiveStream
    var _channelLiveSteam = MutableStateFlow<ResultState<Search>>(ResultState.LOADING)
        private set
    val channelLiveStream: StateFlow<ResultState<Search>> = _channelLiveSteam.asStateFlow()

    //Channel Videos
    var _channelVideos = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
        private set
    val channelVideos: StateFlow<ResultState<Youtube>> = _channelVideos.asStateFlow()

    //Channel Community
     var _channelCommunity = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
        private set
    val channelCommunity: StateFlow<ResultState<Youtube>> = _channelCommunity.asStateFlow()

    //Video Comments
     var _videoComments = MutableStateFlow<ResultState<Comments>>(ResultState.LOADING)
        private set
    val videoComments: StateFlow<ResultState<Comments>> = _videoComments.asStateFlow()

    //Channel Own Videos
    var _ownChannelVideos = MutableStateFlow<ResultState<Search>>(ResultState.LOADING)
        private set
    val ownChannelVideos: StateFlow<ResultState<Search>> = _ownChannelVideos.asStateFlow()

    //Video Categories
    var _videoCategories = MutableStateFlow<ResultState<VideoCategories>>(ResultState.LOADING)
        private set
    val videoCategories: StateFlow<ResultState<VideoCategories>> = _videoCategories.asStateFlow()

    //Single Video
    var _singleVideo = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
        private set
    val singleVideo: StateFlow<ResultState<Youtube>> = _singleVideo.asStateFlow()

    //multiple Videos
    var _multipleVideos = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
        private set
    val multipleVideos: StateFlow<ResultState<Youtube>> = _multipleVideos.asStateFlow()

    fun getVideosList(userRegion: String) {
        viewModelScope.launch {
            _videos.value = ResultState.LOADING
            try {
                val response = repository.getVideoList(userRegion)
                _videos.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _videos.value = ResultState.ERROR(error)
            }

        }
    }

    fun getRelevance() {
        viewModelScope.launch {
            _relevance.value = ResultState.LOADING
            try {
                val response = repository.getRelevance()
                _relevance.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _relevance.value = ResultState.ERROR(error)
            }

        }
    }

    fun getChannelDetails(channelId: String) {
        viewModelScope.launch {
            _channel.value = ResultState.LOADING
            try {
                val response = repository.getChannelDetail(channelId)
                _channel.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _channel.value = ResultState.ERROR(error)
            }

        }
    }

    fun getRelevanceVideos() {
        viewModelScope.launch {
            _relevance_videos.value = ResultState.LOADING
            try {
                val response = repository.getRelevanceVideos()
                _relevance_videos.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _relevance_videos.value = ResultState.ERROR(error)
            }

        }
    }

    fun getSearch(query: String, userRegion: String) {
        viewModelScope.launch {
            _search.value = ResultState.LOADING
            try {
                val response = repository.getSearch(query, userRegion)
                _search.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _search.value = ResultState.ERROR(error)
            }

        }
    }

    fun getChanelBranding(channelId: String) {
        viewModelScope.launch {
            _channelBranding.value = ResultState.LOADING
            try {
                val response = repository.getChannelBranding(channelId)
                _channelBranding.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _channelBranding.value = ResultState.ERROR(error)
            }

        }
    }

    fun getPlaylists(channelId: String) {
        viewModelScope.launch {
            _playlists.value = ResultState.LOADING
            try {
                val response = repository.getPlaylists(channelId)
                _playlists.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _playlists.value = ResultState.ERROR(error)
            }
        }
    }

    fun getChannelSections(channelId: String) {
        viewModelScope.launch {
            _channelSections.value = ResultState.LOADING
            try {
                val response = repository.getChannelSections(channelId)
                _channelSections.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _channelSections.value = ResultState.ERROR(error)
            }
        }
    }

    fun getChannelLiveStreams(channelId: String) {
        viewModelScope.launch {
            _channelLiveSteam.value = ResultState.LOADING
            try {
                val response = repository.getChannelLiveStreams(channelId)
                _channelLiveSteam.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _channelLiveSteam.value = ResultState.ERROR(error)
            }
        }
    }

    fun getChannelVideos(playlistId: String) {
        viewModelScope.launch {
            _channelVideos.value = ResultState.LOADING
            try {
                val response = repository.getChannelVideos(playlistId)
                _channelVideos.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _channelVideos.value = ResultState.ERROR(error)
            }
        }
    }

    fun getChannelCommunity(channelId: String) {
        viewModelScope.launch {
            _channelCommunity.value = ResultState.LOADING
            try {
                val response = repository.getChannelCommunity(channelId)
                _channelCommunity.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _channelCommunity.value = ResultState.ERROR(error)
            }
        }
    }

    fun getVideoComments(videoId: String, order: String) {
        viewModelScope.launch {
            _videoComments.value = ResultState.LOADING
            try {
                val response = repository.getComments(videoId, order)
                _videoComments.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _videoComments.value = ResultState.ERROR(error)
            }
        }
    }

    fun getOwnChannelVideos(channelId: String) {
        viewModelScope.launch {
            _ownChannelVideos.value = ResultState.LOADING
            try {
                val response = repository.getOwnChannelVideos(channelId)
                _ownChannelVideos.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _ownChannelVideos.value = ResultState.ERROR(error)
            }
        }
    }

    fun getVideoCategories() {
        viewModelScope.launch {
            _videoCategories.value = ResultState.LOADING
            try {
                val response = repository.getVideoCategories()
                _videoCategories.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _videoCategories.value = ResultState.ERROR(error)
            }
        }
    }

    fun getSingleVideo(videoId: String) {
        viewModelScope.launch {
            _singleVideo.value = ResultState.LOADING
            try {
                val response = repository.getSingleVideoDetail(videoId)
                _singleVideo.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _singleVideo.value = ResultState.ERROR(error)
            }
        }
    }
    fun getMultipleVideo(videoId: String) {
        viewModelScope.launch {
            _multipleVideos.value = ResultState.LOADING
            try {
                val response = repository.getMultipleVideos(videoId)
                _multipleVideos.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _multipleVideos.value = ResultState.ERROR(error)
            }
        }
    }
}