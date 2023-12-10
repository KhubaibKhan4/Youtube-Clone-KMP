package org.company.app.repository

import org.company.app.data.model.Youtube
import org.company.app.data.plugin.YoutubePlugin
import org.company.app.data.remote.YoutubeClientApi

class Repository : YoutubePlugin {

    override suspend fun getVideoList(): Youtube {
       return YoutubeClientApi.getVideoList()
    }
}