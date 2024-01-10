package org.company.app.domain.repository

import org.company.app.data.model.categories.VideoCategories
import org.company.app.data.model.channel.Channel
import org.company.app.data.model.comments.Comments
import org.company.app.data.model.search.Search
import org.company.app.data.model.videos.Youtube
import org.company.app.data.remote.YoutubeClientApi
import org.company.app.domain.plugin.YoutubePlugin

class Repository : YoutubePlugin {

    override suspend fun getVideoList(userRegion: String): Youtube {
        return YoutubeClientApi.getVideoList(userRegion)
    }

    override suspend fun getRelevance(): Youtube {
        return YoutubeClientApi.getRelevance()
    }

    override suspend fun getChannelDetail(channelId: String): Channel {
        return YoutubeClientApi.getChannelDetails(channelId)
    }

    override suspend fun getRelevanceVideos(): Youtube {
        return YoutubeClientApi.getRelevanceVideos()
    }

    override suspend fun getSearch(query: String, userRegion: String): Search {
        return YoutubeClientApi.getSearch(query,userRegion)
    }

    override suspend fun getChannelBranding(channelId: String): Channel {
        return YoutubeClientApi.getChannelBranding(channelId)
    }

    override suspend fun getPlaylists(channelId: String): Youtube {
        return YoutubeClientApi.getPlaylists(channelId)
    }

    override suspend fun getChannelSections(channelId: String): Youtube {
        return YoutubeClientApi.getChannelSections(channelId)
    }

    override suspend fun getChannelLiveStreams(channelID: String): Search {
        return YoutubeClientApi.getChannelLiveStreams(channelID)
    }

    override suspend fun getChannelVideos(playlistID: String): Youtube {
        return YoutubeClientApi.getChannelVideos(playlistID)
    }

    override suspend fun getOwnChannelVideos(channelId: String): Search {
        return YoutubeClientApi.getOwnChannelVideos(channelId)
    }

    override suspend fun getChannelCommunity(channelId: String): Youtube {
        return YoutubeClientApi.getChannelCommunity(channelId)
    }

    override suspend fun getComments(videoId: String, order: String): Comments {
        return YoutubeClientApi.getComments(videoId, order)
    }

    override suspend fun getVideoCategories(): VideoCategories {
        return YoutubeClientApi.getVideoCategories()
    }

    override suspend fun getSingleVideoDetail(videoId: String): Youtube {
        return YoutubeClientApi.getSingleVideoDetail(videoId)
    }

    override suspend fun getMultipleVideos(videoId: String): Youtube {
        return YoutubeClientApi.getMultipleVideos(videoId)
    }
}