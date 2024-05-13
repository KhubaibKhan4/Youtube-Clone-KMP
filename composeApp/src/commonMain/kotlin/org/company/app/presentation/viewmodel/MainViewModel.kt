package org.company.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youtube.clone.db.YoutubeDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.company.app.data.repository.YouTubeServiceImpl
import org.company.app.domain.model.categories.VideoCategories
import org.company.app.domain.model.channel.Channel
import org.company.app.domain.model.comments.Comments
import org.company.app.domain.model.search.Search
import org.company.app.domain.model.videos.Youtube
import org.company.app.domain.usecases.ResultState
import org.koin.android.annotation.KoinViewModel
import sqldelight.db.YoutubeEntity

@KoinViewModel
class MainViewModel(
    private val repository: YouTubeServiceImpl,
    private val database: YoutubeDatabase,
) : ViewModel() {
    private val _videos = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
    val videos: StateFlow<ResultState<Youtube>> = _videos.asStateFlow()

    private val _videosUsingIds = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
    val videosUsingIds: StateFlow<ResultState<Youtube>> = _videosUsingIds.asStateFlow()

    private val _relevance = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
    val relevance: StateFlow<ResultState<Youtube>> = _relevance.asStateFlow()


    private val _channel = MutableStateFlow<ResultState<Channel>>(ResultState.LOADING)
    val channelDetails: StateFlow<ResultState<Channel>> = _channel.asStateFlow()

    private val _channelBranding = MutableStateFlow<ResultState<Channel>>(ResultState.LOADING)
    val channelBranding: StateFlow<ResultState<Channel>> = _channelBranding.asStateFlow()

    private val _relevance_videos = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
    val relevanceVideos: StateFlow<ResultState<Youtube>> = _relevance_videos.asStateFlow()

    private val _search = MutableStateFlow<ResultState<Search>>(ResultState.LOADING)
    val search: StateFlow<ResultState<Search>> = _search.asStateFlow()
    private val _channelSearch = MutableStateFlow<ResultState<Search>>(ResultState.LOADING)
    val channelSearch: StateFlow<ResultState<Search>> = _channelSearch.asStateFlow()

    private val _playlists = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
    val playlists: StateFlow<ResultState<Youtube>> = _playlists.asStateFlow()

    private val _channelSections = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
    val channelSections: StateFlow<ResultState<Youtube>> = _channelSections

    private val _channelLiveSteam = MutableStateFlow<ResultState<Search>>(ResultState.LOADING)
    val channelLiveStream: StateFlow<ResultState<Search>> = _channelLiveSteam.asStateFlow()

    private val _channelVideos = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
    val channelVideos: StateFlow<ResultState<Youtube>> = _channelVideos.asStateFlow()

    private val _channelCommunity = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
    val channelCommunity: StateFlow<ResultState<Youtube>> = _channelCommunity.asStateFlow()

    private val _videoComments = MutableStateFlow<ResultState<Comments>>(ResultState.LOADING)
    val videoComments: StateFlow<ResultState<Comments>> = _videoComments.asStateFlow()

    private val _ownChannelVideos = MutableStateFlow<ResultState<Search>>(ResultState.LOADING)
    val ownChannelVideos: StateFlow<ResultState<Search>> = _ownChannelVideos.asStateFlow()

    private val _videoCategories =
        MutableStateFlow<ResultState<VideoCategories>>(ResultState.LOADING)
    val videoCategories: StateFlow<ResultState<VideoCategories>> = _videoCategories.asStateFlow()


    private val _singleVideo = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
    val singleVideo: StateFlow<ResultState<Youtube>> = _singleVideo.asStateFlow()

    private val _multipleVideos = MutableStateFlow<ResultState<Youtube>>(ResultState.LOADING)
    val multipleVideos: StateFlow<ResultState<Youtube>> = _multipleVideos.asStateFlow()

    private val _localVideos =
        MutableStateFlow<ResultState<List<YoutubeEntity>>>(ResultState.LOADING)
    val localVideos: StateFlow<ResultState<List<YoutubeEntity>>> = _localVideos.asStateFlow()

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
    fun getVideosUsingIds(ids: String) {
        viewModelScope.launch {
            _videosUsingIds.value = ResultState.LOADING
            try {
                val response = repository.getVideosUsingIds(ids)
                _videosUsingIds.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                val error = e.message.toString()
                _videosUsingIds.value = ResultState.ERROR(error)
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

    fun getChannelSearch(channelID: String, query: String) {
        viewModelScope.launch {
            _channelSearch.value = ResultState.LOADING
            try {
                val response = repository.getChannelSearch(channelID, query)
                _channelSearch.value = ResultState.SUCCESS(response)
                println("Channel Search $response")
            } catch (e: Exception) {
                val error = e.message.toString()
                _channelSearch.value = ResultState.ERROR(error)
            }
        }
    }


    fun getAllVideos() {
        viewModelScope.launch {
            _localVideos.value = ResultState.LOADING
            try {
                val response = database.youtubeEntityQueries.getAllVideos().executeAsList()
                _localVideos.value = ResultState.SUCCESS(response)
            } catch (e: Exception) {
                _localVideos.value = ResultState.ERROR(e.toString())
            }
        }
    }

    fun insertVideos(
        id: Long?,
        title: String,
        videoThumbnail: String,
        videoDesc: String,
        isVerified: Long,
        channelSubs: String,
        likes: String,
        channelName: String,
        channelImage: String,
        views: String,
        pubDate: String,
        duration: String,
    ) {
        viewModelScope.launch {
            try {
                val exitingVideo =
                    database.youtubeEntityQueries.getVideoByTitle(title).executeAsOneOrNull()
                if (exitingVideo == null || exitingVideo.id == null && !exitingVideo.title.contains(
                        title
                    )
                ) {
                    database.youtubeEntityQueries.insertVideos(
                        id,
                        title,
                        videoThumbnail,
                        videoDesc,
                        isVerified,
                        channelSubs,
                        likes,
                        channelName,
                        channelImage,
                        views,
                        pubDate,
                        duration
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteVideoById(id: Long) {
        viewModelScope.launch {
            try {
                database.youtubeEntityQueries.deleteVideoById(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}