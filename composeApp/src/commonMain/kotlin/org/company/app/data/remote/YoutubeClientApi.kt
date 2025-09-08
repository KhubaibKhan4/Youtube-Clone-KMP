package org.company.app.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.company.app.domain.model.categories.VideoCategories
import org.company.app.domain.model.channel.Channel
import org.company.app.domain.model.comments.Comments
import org.company.app.domain.model.search.Search
import org.company.app.domain.model.videos.Youtube
import org.company.app.core.common.Constant.API_KEY

class YoutubeClientApi(
    private val client: HttpClient
) {
    private fun getRandomVideoIds(): String? {
        val videoIds = listOf(
            "Ra9KOsF4BjY,XTtTtlVhh5g,NIM_drzGMJk,-yMaznEn1LM,NiMWG8KyBY4,bQpGIcRphCo,urXQVXswcgc,8OVCL0lMV1I,8qcqpwJ1LBw,C_sEA0c94zs,wxBtwCZtDAg,IX0QVURTqb8,74pnrYPozcU,MDZlKThUOSw,At4T3Ujv4xk,AIt_6ReFzEw,D_rY6oJlCoM,pXtmG1b1-Pw,Fq23VNXRV40,gzlwegTk1AM,Y9HKtiq0q9A,wjY_1Z27_sE,GFo32hjd1QY,aTCq7fHpcww,-Cqe6xIe3ys"
        )
        return videoIds.shuffled().firstOrNull()
    }

    suspend fun getVideoList(userRegion: String): Youtube {
        val url = "videos?part=snippet,contentDetails,statistics,liveStreamingDetails,player,recordingDetails,id&chart=mostPopular&regionCode=${userRegion}&maxResults=2000&key=${API_KEY}"
        return client.get(url).body()
    }

    suspend fun getRelevance(): Youtube {
        val url ="videos?part=contentDetails%2Csnippet%2Cstatistics,statistics&id=${getRandomVideoIds()}&key=${API_KEY}"
        return client.get(url).body()
    }
    suspend fun getVideosUsingIds(ids: String): Youtube {
        val url = "videos?id=$ids&key=$API_KEY&part=snippet,contentDetails,statistics&maxResults=20"
        return client.get(url).body()
    }

    suspend fun getChannelDetails(channelId: String): Channel {
        val url ="channels?part=contentDetails,brandingSettings,contentDetails,contentOwnerDetails,id,localizations,snippet,statistics,status,topicDetails&id=$channelId&key=${API_KEY}"
        return client.get(url).body()
    }

    suspend fun getChannelBranding(channelId: String): Channel {
        val url ="channels?part=brandingSettings&id=$channelId&key=${API_KEY}"
        return client.get(url).body()
    }

    suspend fun getRelevanceVideos(): Youtube {
        val url ="search?part=snippet&order=date&type=video&videoEmbeddable=true&maxResults=200&key=$API_KEY&regionCode=us"
        return client.get(url).body()
    }

    suspend fun getSearch(query: String, userRegion: String): Search {
        val url ="search?part=snippet&q=${query}&type=any&maxResults=200&key=$API_KEY&regionCode=${userRegion}"
        return client.get(url).body()
    }

    suspend fun getPlaylists(channelId: String): Youtube {
        val url ="playlists?part=snippet,contentDetails,localizations,player,status,id&channelId=${channelId}&maxResults=50&key=${API_KEY}"
        return client.get(url).body()
    }

    suspend fun getChannelSections(channelId: String): Youtube {
        val url ="channelSections?part=snippet,contentDetails&channelId=${channelId}&key=${API_KEY}"
        return client.get(url).body()
    }

    suspend fun getChannelLiveStreams(channelID: String): Search {
        val url ="search?part=snippet&eventType=live&type=video&id=${channelID}&maxResults=500&regionCode=us&key=${API_KEY}"
        return client.get(url).body()
    }

    suspend fun getChannelVideos(playlistID: String): Youtube {
        val url ="playlistItems?part=snippet,contentDetails,id,status&&maxResults=50&playlistId=${playlistID}&key=$API_KEY"
        return client.get(url).body()
    }

    suspend fun getOwnChannelVideos(channelId: String): Search {
        val url ="search?key=${API_KEY}&part=snippet&channelId=${channelId}&type=video&maxResults=500"
        return client.get(url).body()
    }

    suspend fun getChannelCommunity(channelId: String): Youtube {
        val url ="activities?part=snippet,contentDetails&channelId=${channelId}&maxResults=500&key=${API_KEY}"
        return client.get(url).body()
    }

    suspend fun getComments(videoId: String, order: String): Comments {
        val url ="commentThreads?part=snippet,replies&videoId=${videoId}&order=${order}&maxResults=2000&key=${API_KEY}"
        return client.get(url).body()
    }

    suspend fun getVideoCategories(): VideoCategories {
        val url ="videoCategories?key=${API_KEY}&part=snippet&maxResults=250&regionCode=us"
        return client.get(url).body()
    }

    suspend fun getSingleVideoDetail(videoId: String): Youtube {
        val url ="videos?part=snippet,contentDetails,statistics&id=${videoId}&key=$API_KEY"
        return client.get(url).body()
    }

    suspend fun getMultipleVideos(videoId: String): Youtube {
        val url ="videos?part=snippet,contentDetails,statistics&id=${videoId}&key=$API_KEY"
        return client.get(url).body()
    }

    suspend fun getChannelSearch(channelID: String, query: String): Search {
        val url ="search?part=snippet&channelId=${channelID}&q=${query}&key=$API_KEY"
        return client.get(url).body()
    }
}