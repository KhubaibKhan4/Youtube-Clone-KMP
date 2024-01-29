package org.company.app.data.local

import kotlinx.coroutines.flow.Flow
import `sql-delight`.db.YoutubeEntity

interface VideoDataSource {
    suspend fun getVideoById(id: String): YoutubeEntity?
    fun getAllVideos(): Flow<List<YoutubeEntity>>
    suspend fun deleteVideoById(id: String)
    suspend fun insertVideo(
        id: String? = null,
        title: String,
        channelName: String,
        views: String,
        pubDate: String,
        channelImage: String,
        thumbnails: String
    )
}