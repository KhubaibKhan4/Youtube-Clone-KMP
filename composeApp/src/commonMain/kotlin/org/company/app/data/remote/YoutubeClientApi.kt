package org.company.app.data.remote

import com.eygraber.uri.Uri
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.company.app.data.model.categories.VideoCategories
import org.company.app.data.model.channel.Channel
import org.company.app.data.model.comments.Comments
import org.company.app.data.model.search.Search
import org.company.app.data.model.videos.Youtube
import org.company.app.utils.Constant.API_KEY
import org.company.app.utils.Constant.BASE_URL
import org.company.app.utils.Constant.TIMEOUT

object YoutubeClientApi {
    @OptIn(ExperimentalSerializationApi::class)
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                explicitNulls = false
                ignoreUnknownKeys = true
            })
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        install(HttpTimeout) {
            connectTimeoutMillis = TIMEOUT
            requestTimeoutMillis = TIMEOUT
            socketTimeoutMillis = TIMEOUT
        }
    }

    private fun getRandomVideoIds(): String? {
        val videoIds = listOf(
            "Ra9KOsF4BjY,XTtTtlVhh5g,NIM_drzGMJk,-yMaznEn1LM,NiMWG8KyBY4,bQpGIcRphCo,urXQVXswcgc,8OVCL0lMV1I,8qcqpwJ1LBw,C_sEA0c94zs,wxBtwCZtDAg,IX0QVURTqb8,74pnrYPozcU,MDZlKThUOSw,At4T3Ujv4xk,AIt_6ReFzEw,D_rY6oJlCoM,pXtmG1b1-Pw,Fq23VNXRV40,gzlwegTk1AM,Y9HKtiq0q9A,wjY_1Z27_sE,GFo32hjd1QY,aTCq7fHpcww,-Cqe6xIe3ys"
        )
        return videoIds.shuffled().firstOrNull()
    }

    suspend fun getVideoList(userRegion: String): Youtube {
        val url =
            BASE_URL + "videos?part=snippet,contentDetails,statistics,liveStreamingDetails,player,recordingDetails,id&chart=mostPopular&regionCode=${userRegion}&maxResults=2000&key=$API_KEY"
        return client.get(url).body()
    }

    suspend fun getRelevance(): Youtube {
        val url =
            BASE_URL + "videos?part=contentDetails%2Csnippet%2Cstatistics,statistics&id=${getRandomVideoIds()}&key=$API_KEY"
        return client.get(url).body()
    }

    suspend fun getChannelDetails(channelId: String): Channel {
        val url =
            BASE_URL + "channels?part=contentDetails,brandingSettings,contentDetails,contentOwnerDetails,id,localizations,snippet,statistics,status,topicDetails&id=${channelId}&key=${API_KEY}"
        return client.get(url).body()
    }

    suspend fun getChannelBranding(channelId: String): Channel {
        val url = BASE_URL + "channels?part=brandingSettings&id=$channelId&key=$API_KEY"
        return client.get(url).body()
    }

    suspend fun getRelevanceVideos(): Youtube {
        val url =
            BASE_URL + "search?part=snippet&order=date&type=video&videoEmbeddable=true&maxResults=200&key=$API_KEY&regionCode=us"
        return client.get(url).body()
    }

    suspend fun getSearch(query: String, userRegion: String): Search {
        val url =
            BASE_URL + "search?part=snippet&q=${query}&type=any&maxResults=200&key=$API_KEY&regionCode=${userRegion}"
        return client.get(url).body()
    }

    suspend fun getPlaylists(channelId: String): Youtube {
        val url =
            BASE_URL + "playlists?part=snippet,contentDetails,localizations,player,status,id&channelId=${channelId}&maxResults=50&key=${API_KEY}"
        return client.get(url).body()
    }

    suspend fun getChannelSections(channelId: String): Youtube {
        val url =
            BASE_URL + "channelSections?part=snippet,contentDetails&channelId=${channelId}&key=${API_KEY}"
        return client.get(url).body()
    }

    suspend fun getChannelLiveStreams(channelID: String): Search {
        val url =
            BASE_URL + "search?part=snippet&eventType=live&type=video&id=${channelID}&maxResults=500&regionCode=us&key=${API_KEY}"
        return client.get(url).body()
    }

    suspend fun getChannelVideos(playlistID: String): Youtube {
        val url =BASE_URL + "playlistItems?part=snippet,contentDetails,id,status&&maxResults=50&playlistId=${playlistID}&key=$API_KEY"
        return client.get(url).body()
    }

    suspend fun getOwnChannelVideos(channelId: String): Search {
        val url =
            BASE_URL + "search?key=${API_KEY}&part=snippet&channelId=${channelId}&type=video&maxResults=500"
        return client.get(url).body()
    }

    suspend fun getChannelCommunity(channelId: String): Youtube {
        val url =
            BASE_URL + "activities?part=snippet,contentDetails&channelId=${channelId}&maxResults=500&key=${API_KEY}"
        return client.get(url).body()
    }

    suspend fun getComments(videoId: String, order: String): Comments {
        val url =
            BASE_URL + "commentThreads?part=snippet,replies&videoId=${videoId}&order=${order}&maxResults=2000&key=${API_KEY}"
        return client.get(url).body()
    }

    suspend fun getVideoCategories(): VideoCategories {
        val url =
            BASE_URL + "videoCategories?key=${API_KEY}&part=snippet&maxResults=250&regionCode=us"
        return client.get(url).body()
    }

    suspend fun getSingleVideoDetail(videoId: String): Youtube {
        val url = BASE_URL + "videos?part=snippet,contentDetails,statistics&id=${videoId}&key=$API_KEY"
        return client.get(url).body()
    }
    suspend fun getMultipleVideos(videoId: String): Youtube{
        val url = BASE_URL+"videos?part=snippet,contentDetails,statistics&id=${videoId}&key=$API_KEY"
        return client.get(url).body()
    }
}