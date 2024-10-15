package org.company.app.data.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.messaging.messaging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.company.app.data.remote.YoutubeClientApi
import org.company.app.domain.model.categories.VideoCategories
import org.company.app.domain.model.channel.Channel
import org.company.app.domain.model.comments.Comments
import org.company.app.domain.model.search.Search
import org.company.app.domain.model.videos.Youtube
import org.company.app.domain.repository.YouTubeService
import org.company.app.utils.Layout
import org.company.app.utils.LayoutInformation
import org.company.app.utils.LayoutMeta
import org.company.app.utils.LayoutType
import org.company.app.utils.Meta
import org.company.app.utils.UiData

class YouTubeServiceImpl(
    private val youtubeClientApi: YoutubeClientApi,
    private val database: FirebaseDatabase
) : YouTubeService {


    fun fetchUiData(): Flow<UiData?> = flow {
        try {
            database.reference().child("ui").valueEvents.collect { dataSnapshot ->
                val layoutSnapshot = dataSnapshot.child("layout")
                val metaSnapshot = dataSnapshot.child("meta")
                val layout = layoutSnapshot.children.associate { child ->
                    val key = child.key ?: ""
                    val type = child.child("type").value<String>() ?: ""
                    val columns = child.child("columns").value<Int?>()
                    key to Layout(type, columns)
                }
                val canFavourite = metaSnapshot.child("canFavourite").value<Boolean>() ?: false
                val mode = metaSnapshot.child("mode").value<String>() ?: ""
                val meta = Meta(canFavourite, mode)
                val uiData = UiData(layout, meta)
                emit(uiData)
            }
        } catch (e: Exception) {
            emit(null)
            println("FetchUiData Error fetching UI data: ${e.message}")
        }
    }
    suspend fun registerMessagingToken(): String{
        return Firebase.messaging.getToken()
    }

    fun subscribeToTopic(topic: String) {
        return Firebase.messaging.subscribeToTopic(topic)
    }
    fun unSubscribeToTopic(topic: String){
        return Firebase.messaging.unsubscribeFromTopic(topic)
    }

    fun fetchLayoutInformation(): Flow<LayoutInformation?> = flow {
        fetchUiData().collect { uiData ->
            uiData?.let {
                val layoutMode = it.meta.mode
                val selectedLayout = it.layout[layoutMode] ?: it.layout["layout_1"]

                val layoutType = when (selectedLayout?.type) {
                    "list" -> LayoutType.List
                    "grid" -> LayoutType.Grid(selectedLayout.columns ?: 1)
                    else -> LayoutType.List
                }

                val layoutMeta = LayoutMeta(
                    layoutType = layoutType,
                    favouriteEnabled = it.meta.canFavourite
                )

                emit(LayoutInformation(layoutMeta))
            } ?: emit(null)
        }
    }

    fun fetchCanFavourite(): Flow<Boolean?> = flow {
        try {
            database.reference().child("ui").child("meta")
                .child("canFavourite").valueEvents.collect { dataSnapshot ->
                    val canFavourite = dataSnapshot.value<Boolean?>()
                    println("canFavourite Value: $canFavourite")
                    emit(canFavourite)
                }
        } catch (e: Exception) {
            emit(null)
            println("FetchCanFavourite Error fetching canFavourite: ${e.message}")
        }
    }

    fun fetchSeverUi(): Flow<UiData?> = flow {
        try {
            database.reference().child("ui").valueEvents.collect { dataSnapshot ->
                val layoutUi = dataSnapshot.value<UiData?>()
                println("canFavourite Value: $layoutUi")
                emit(layoutUi)
            }
        } catch (e: Exception) {
            emit(null)
            println("Layout UI Error fetching canFavourite: ${e.message}")
        }
    }

    override suspend fun getVideoList(userRegion: String): Youtube {
        return youtubeClientApi.getVideoList(userRegion)
    }

    override suspend fun getRelevance(): Youtube {
        return youtubeClientApi.getRelevance()
    }

    override suspend fun getChannelDetail(channelId: String): Channel {
        return youtubeClientApi.getChannelDetails(channelId)
    }

    override suspend fun getRelevanceVideos(): Youtube {
        return youtubeClientApi.getRelevanceVideos()
    }

    override suspend fun getSearch(
        query: String,
        userRegion: String,
    ): Search {
        return youtubeClientApi.getSearch(query, userRegion)
    }

    override suspend fun getChannelBranding(channelId: String): Channel {
        return youtubeClientApi.getChannelBranding(channelId)
    }

    override suspend fun getPlaylists(channelId: String): Youtube {
        return youtubeClientApi.getPlaylists(channelId)
    }

    override suspend fun getChannelSections(channelId: String): Youtube {
        return youtubeClientApi.getChannelSections(channelId)
    }

    override suspend fun getChannelLiveStreams(channelID: String): Search {
        return youtubeClientApi.getChannelLiveStreams(channelID)
    }

    override suspend fun getChannelVideos(playlistID: String): Youtube {
        return youtubeClientApi.getChannelVideos(playlistID)
    }

    override suspend fun getOwnChannelVideos(channelId: String): Search {
        return youtubeClientApi.getOwnChannelVideos(channelId)
    }

    override suspend fun getChannelCommunity(channelId: String): Youtube {
        return youtubeClientApi.getChannelCommunity(channelId)
    }

    override suspend fun getComments(
        videoId: String,
        order: String,
    ): Comments {
        return youtubeClientApi.getComments(videoId, order)
    }

    override suspend fun getVideoCategories(): VideoCategories {
        return youtubeClientApi.getVideoCategories()
    }

    override suspend fun getSingleVideoDetail(videoId: String): Youtube {
        return youtubeClientApi.getSingleVideoDetail(videoId)
    }

    override suspend fun getMultipleVideos(videoId: String): Youtube {
        return youtubeClientApi.getMultipleVideos(videoId)
    }

    override suspend fun getChannelSearch(channelID: String, query: String): Search {
        return youtubeClientApi.getChannelSearch(channelID, query)
    }

    override suspend fun getVideosUsingIds(ids: String): Youtube {
        return youtubeClientApi.getVideosUsingIds(ids)
    }
}