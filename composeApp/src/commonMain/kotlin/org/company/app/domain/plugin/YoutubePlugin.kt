package org.company.app.domain.plugin

import org.company.app.data.model.categories.VideoCategories
import org.company.app.data.model.channel.Channel
import org.company.app.data.model.comments.Comments
import org.company.app.data.model.search.Search
import org.company.app.data.model.videos.Youtube

interface YoutubePlugin {
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
}