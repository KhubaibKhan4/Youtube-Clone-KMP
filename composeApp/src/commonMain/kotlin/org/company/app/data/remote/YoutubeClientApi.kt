package org.company.app.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.company.app.data.model.channel.Channel
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

    suspend fun getVideoList(): Youtube {
        val url =
            BASE_URL + "videos?part=contentDetails%2Csnippet%2Cstatistics&chart=mostPopular&regionCode=us&maxResults=2000&key=$API_KEY"
        return client.get(url).body()
    }

    suspend fun getRelevance(): Youtube {
        val url =
            BASE_URL + "videos?part=contentDetails%2Csnippet%2Cstatistics,statistics&id=GFo32hjd1QY,aTCq7fHpcww,-Cqe6xIe3ys&key=$API_KEY"
        return client.get(url).body()
    }

    suspend fun getChannelDetails(channelId: String): Channel {
        val url =
            BASE_URL + "channels?part=snippet,contentDetails,statistics&id=$channelId&key=$API_KEY"
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

    suspend fun getSearch(query: String): Search {
        val url = BASE_URL + "search?part=snippet&q=kotlin&type=video&maxResults=200&key=$API_KEY"
        return client.get(url).body()
    }
}