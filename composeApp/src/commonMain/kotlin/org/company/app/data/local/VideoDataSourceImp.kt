package org.company.app.data.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import `sql-delight`.db.YoutubeEntity
import `sql-delight`.db.YoutubeEntityQueries

class VideoDataSourceImp(
    db: YoutubeEntityQueries
) : VideoDataSource {

    val queries = db

    override suspend fun getVideoById(id: String): YoutubeEntity? {
        return withContext(Dispatchers.Main) {
            queries.getVideoById(id.toLong()).executeAsOneOrNull()
        }
    }

    override fun getAllVideos(): Flow<List<YoutubeEntity>> {

    }

    override suspend fun deleteVideoById(id: String) {
        withContext(Dispatchers.Main) {
            queries.deleteVideoById(id.toLong())
        }
    }

    override suspend fun insertVideo(
        id: String?,
        title: String,
        channelName: String,
        views: String,
        pubDate: String,
        channelImage: String,
        thumbnails: String
    ) {
        withContext(Dispatchers.Main) {
            queries.insertVideos(
                id?.toLong(),
                title,
                channelName,
                views,
                pubDate,
                channelImage,
                thumbnails
            )
        }
    }
}