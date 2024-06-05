package org.company.app.data.repository

import org.company.app.data.remote.YoutubeClientApi
import org.company.app.domain.model.categories.VideoCategories
import org.company.app.domain.model.channel.Channel
import org.company.app.domain.model.comments.Comments
import org.company.app.domain.model.search.Search
import org.company.app.domain.model.videos.Youtube
import org.company.app.domain.repository.YouTubeService

class YouTubeServiceImpl(
    private val youtubeClientApi: YoutubeClientApi,
) : YouTubeService {

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