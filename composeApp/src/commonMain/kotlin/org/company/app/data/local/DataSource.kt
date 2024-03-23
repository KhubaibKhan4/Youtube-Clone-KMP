package org.company.app.data.local

import sqldelight.db.YoutubeEntity

interface DataSource {
    suspend fun getVideoById(id: Long): YoutubeEntity?
    fun getAllVideos(): List<YoutubeEntity>
    suspend fun deleteVideoById(id: Long)
    suspend fun insertVideo(
        id: Long?,
        title: String,
        videoThumbnail: String,
        videoDesc: String,
        isVerified: Long,
        channelSubs: String,
        likes: String,
        channelName: String,
        channelImage: String,
        views: String,
        pubDate: String,
        duration: String,
    )
}