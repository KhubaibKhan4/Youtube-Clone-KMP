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
import org.company.app.utils.LayoutInformation
import org.company.app.utils.UiData
import sqldelight.db.YoutubeEntity


typealias YouTube = ResultState<Youtube>
typealias Channels = ResultState<Channel>
typealias Searches = ResultState<Search>
typealias CommentState = ResultState<Comments>
typealias VideoCategoriesState = ResultState<VideoCategories>
typealias LocalVideos = ResultState<List<YoutubeEntity>>


class MainViewModel(
    private val repository: YouTubeServiceImpl,
    private val database: YoutubeDatabase,
) : ViewModel() {
    
    private val _videos = MutableStateFlow<YouTube>(ResultState.LOADING)
    val videos: StateFlow<YouTube> = _videos.asStateFlow()

    private val _videosUsingIds = MutableStateFlow<YouTube>(ResultState.LOADING)
    val videosUsingIds: StateFlow<YouTube> = _videosUsingIds.asStateFlow()

    private val _relevance = MutableStateFlow<YouTube>(ResultState.LOADING)
    val relevance: StateFlow<YouTube> = _relevance.asStateFlow()


    private val _channel = MutableStateFlow<Channels>(ResultState.LOADING)
    val channelDetails: StateFlow<Channels> = _channel.asStateFlow()

    private val _channelBranding = MutableStateFlow<Channels>(ResultState.LOADING)
    val channelBranding: StateFlow<Channels> = _channelBranding.asStateFlow()

    private val _relevance_videos = MutableStateFlow<YouTube>(ResultState.LOADING)
    val relevanceVideos: StateFlow<YouTube> = _relevance_videos.asStateFlow()

    private val _search = MutableStateFlow<Searches>(ResultState.LOADING)
    val search: StateFlow<Searches> = _search.asStateFlow()

    private val _channelSearch = MutableStateFlow<Searches>(ResultState.LOADING)
    val channelSearch: StateFlow<Searches> = _channelSearch.asStateFlow()

    private val _playlists = MutableStateFlow<YouTube>(ResultState.LOADING)
    val playlists: StateFlow<YouTube> = _playlists.asStateFlow()

    private val _channelSections = MutableStateFlow<YouTube>(ResultState.LOADING)
    val channelSections: StateFlow<YouTube> = _channelSections

    private val _channelLiveSteam = MutableStateFlow<Searches>(ResultState.LOADING)
    val channelLiveStream: StateFlow<Searches> = _channelLiveSteam.asStateFlow()

    private val _channelVideos = MutableStateFlow<YouTube>(ResultState.LOADING)
    val channelVideos: StateFlow<YouTube> = _channelVideos.asStateFlow()

    private val _channelCommunity = MutableStateFlow<YouTube>(ResultState.LOADING)
    val channelCommunity: StateFlow<YouTube> = _channelCommunity.asStateFlow()

    private val _videoComments = MutableStateFlow<CommentState>(ResultState.LOADING)
    val videoComments: StateFlow<CommentState> = _videoComments.asStateFlow()

    private val _ownChannelVideos = MutableStateFlow<Searches>(ResultState.LOADING)
    val ownChannelVideos: StateFlow<Searches> = _ownChannelVideos.asStateFlow()

    private val _videoCategories =
        MutableStateFlow<VideoCategoriesState>(ResultState.LOADING)
    val videoCategories: StateFlow<VideoCategoriesState> = _videoCategories.asStateFlow()


    private val _singleVideo = MutableStateFlow<YouTube>(ResultState.LOADING)
    val singleVideo: StateFlow<YouTube> = _singleVideo.asStateFlow()

    private val _multipleVideos = MutableStateFlow<YouTube>(ResultState.LOADING)
    val multipleVideos: StateFlow<YouTube> = _multipleVideos.asStateFlow()

    private val _localVideos =
        MutableStateFlow<LocalVideos>(ResultState.LOADING)
    val localVideos: StateFlow<LocalVideos> = _localVideos.asStateFlow()


    init {
        fetchLayoutInformation()
        fetchCanFavourite()
    }
    private val _layoutInformation = MutableStateFlow<LayoutInformation?>(null)
    val layoutInformation: StateFlow<LayoutInformation?> get() = _layoutInformation

    private fun fetchLayoutInformation() {
        viewModelScope.launch {
            repository.fetchLayoutInformation().collect { layoutInfo ->
                _layoutInformation.value = layoutInfo
            }
        }
    }
    private val _canFavourite = MutableStateFlow<Boolean?>(null)
    val canFavourite: StateFlow<Boolean?> get() = _canFavourite

    private val _serverUi = MutableStateFlow<UiData?>(null)
    val serverUi: StateFlow<UiData?>  = _serverUi

    private fun fetchCanFavourite() {
        viewModelScope.launch {
            repository.fetchCanFavourite().collect { value ->
                _canFavourite.value = value
            }
        }
    }
    private fun fetchServerUi(){
        viewModelScope.launch {
            repository.fetchSeverUi().collect{serverui->
                _serverUi.value = serverui
            }
        }
    }

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
