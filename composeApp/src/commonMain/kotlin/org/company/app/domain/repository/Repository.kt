package org.company.app.domain.repository

import org.company.app.data.model.channel.Channel
import org.company.app.data.model.videos.Item
import org.company.app.data.model.videos.Youtube
import org.company.app.domain.plugin.YoutubePlugin
import org.company.app.data.remote.YoutubeClientApi
import org.company.app.domain.usecases.YoutubeState

class Repository : YoutubePlugin {

    override suspend fun getVideoList(): Youtube {
       return YoutubeClientApi.getVideoList()
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
}