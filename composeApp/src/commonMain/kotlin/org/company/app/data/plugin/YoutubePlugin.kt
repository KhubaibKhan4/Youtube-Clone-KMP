package org.company.app.data.plugin

import org.company.app.data.model.Youtube
import org.company.app.utils.usecases.YoutubeState

interface YoutubePlugin {
    suspend fun getVideoList() : Youtube
}