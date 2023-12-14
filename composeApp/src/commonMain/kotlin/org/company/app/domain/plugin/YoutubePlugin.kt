package org.company.app.domain.plugin

import org.company.app.data.model.videos.Youtube

interface YoutubePlugin {
    suspend fun getVideoList() : Youtube
    suspend fun getRelevance(): Youtube
}