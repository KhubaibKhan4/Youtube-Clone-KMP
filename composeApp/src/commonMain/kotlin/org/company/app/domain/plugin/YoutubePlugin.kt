package org.company.app.domain.plugin

import org.company.app.data.model.Youtube

interface YoutubePlugin {
    suspend fun getVideoList() : Youtube
}