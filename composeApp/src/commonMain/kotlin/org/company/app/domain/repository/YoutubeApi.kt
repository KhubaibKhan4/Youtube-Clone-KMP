package org.company.app.domain.repository

import org.company.app.domain.model.categories.VideoCategories
import org.company.app.domain.model.channel.Channel
import org.company.app.domain.model.comments.Comments
import org.company.app.domain.model.search.Search
import org.company.app.domain.model.videos.Youtube

interface YoutubeApi {
    suspend fun getVideoList(userRegion: String) : Youtube
    suspend fun getRelevance(): Youtube
    suspend fun getChannelDetail(channelId: String): Channel
    suspend fun getRelevanceVideos(): Youtube
    suspend fun getSearch(query: String, userRegion: String): Search
    suspend fun getChannelBranding(channelId: String): Channel
    suspend fun getPlaylists(channelId: String): Youtube
    suspend fun getChannelSections(channelId: String): Youtube
    suspend fun getChannelLiveStreams(channelID: String): Search
    suspend fun getChannelVideos(playlistID: String): Youtube
    suspend fun getOwnChannelVideos(channelId: String): Search
    suspend fun getChannelCommunity(channelId: String): Youtube
    suspend fun getComments(videoId: String, order: String): Comments
    suspend fun getVideoCategories(): VideoCategories
    suspend fun getSingleVideoDetail(videoId: String): Youtube
    suspend fun getMultipleVideos(videoId: String): Youtube
    suspend fun getChannelSearch(channelID: String, query: String): Search
}