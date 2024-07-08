package org.company.app.data.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.company.app.data.remote.YoutubeClientApi
import org.company.app.domain.model.categories.VideoCategories
import org.company.app.domain.model.channel.Channel
import org.company.app.domain.model.comments.Comments
import org.company.app.domain.model.search.Search
import org.company.app.domain.model.videos.Youtube
import org.company.app.domain.repository.YouTubeService
import org.company.app.utils.LayoutInformation
import org.company.app.utils.LayoutMeta
import org.company.app.utils.LayoutType

class YouTubeServiceImpl(
    private val youtubeClientApi: YoutubeClientApi,
) : YouTubeService {

    private val database = Firebase.database.reference()

    fun fetchLayout(): Flow<LayoutInformation?> = flow {
        try {
            database.child("ui").child("layout").valueEvents.collect { dataSnapshot ->
                val layoutType = when (dataSnapshot.child("type").value<String?>()) {
                    "list" -> LayoutType.List
                    "grid" -> LayoutType.Grid(dataSnapshot.child("columns").value<Int?>() ?: 1) // Provide default value
                    else -> LayoutType.List
                }
                val canFavourite = dataSnapshot.child("meta").child("canFavourite").value<Boolean?>()
                if (canFavourite != null) {
                    val layoutMeta = LayoutMeta(
                        layoutType = layoutType,
                        favouriteEnabled = canFavourite
                    )
                    emit(LayoutInformation(layoutMeta))
                } else {
                    emit(null)
                }
            }
        } catch (e: Exception) {
            emit(null)
            println("FetchLayout Error fetching layout: ${e.message}")
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