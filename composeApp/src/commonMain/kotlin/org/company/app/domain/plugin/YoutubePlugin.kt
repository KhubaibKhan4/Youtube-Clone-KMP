package org.company.app.domain.plugin

import org.company.app.data.model.channel.Channel
import org.company.app.data.model.videos.Item
import org.company.app.data.model.videos.Youtube

interface YoutubePlugin {
    suspend fun getVideoList() : Youtube
    suspend fun getRelevance(): Youtube
    suspend fun getChannelDetail(channelId: String): Channel
    suspend fun getRelevanceVideos(): Youtube
}