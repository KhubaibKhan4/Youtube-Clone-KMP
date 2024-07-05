package org.company.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.CanvasBasedWindow
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import com.youtube.clone.db.YoutubeDatabase
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.company.app.html.LocalLayerContainer
import org.company.app.video.HTMLVideoPlayer
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.Worker
import org.w3c.dom.url.URL
import org.w3c.notifications.DEFAULT
import org.w3c.notifications.DENIED
import org.w3c.notifications.GRANTED
import org.w3c.notifications.Notification
import org.w3c.notifications.NotificationOptions
import org.w3c.notifications.NotificationPermission

internal actual fun openUrl(url: String?) {
    url?.let { window.open(it) }
}

@Composable
internal actual fun VideoPlayer(modifier: Modifier, url: String?, thumbnail: String?) {
    val videoId = extractVideoId(url.toString())
    CompositionLocalProvider(LocalLayerContainer provides document.body!!) {
        HTMLVideoPlayer(videoId)
    }

}

@Composable
internal actual fun provideShortCuts() {
    return
}
@Composable
internal actual fun ShareManager(title: String, videoUrl: String) {
    window.open(url = videoUrl, "_blank")
}

@Composable
internal actual fun ShortsVideoPlayer(url: String?, modifier: Modifier) {
    val videoId = extractVideoId(url.toString())
    console.log("Before YouTube API is ready $videoId")

    CompositionLocalProvider(LocalLayerContainer provides document.body!!) {
        HTMLVideoPlayer(videoId)
    }
}

private fun extractVideoId(url: String): String {
    val videoIdRegex =
        Regex("""(?:youtube\.com\/(?:[^\/]+\/.+\/|(?:v|e(?:mbed)?)\/|.*[?&]v=)|youtu\.be\/)([^"&?\/\s]{11})""")
    val matchResult = videoIdRegex.find(url)
    return matchResult?.groupValues?.get(1) ?: "default_video_id"
}

internal actual fun UserRegion(): String {
    return js("window.navigator.language.slice(-2)")
}

@Composable
actual fun isConnected(): Flow<Boolean> {
    return flow {
        while (true) {
            val isConnected = try {
                val response = window.fetch("https://www.google.com").await()
                response.ok
            } catch (e: dynamic) {
                false
            }
            emit(isConnected)
            delay(5000)
        }
    }
}

actual class DriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver {
        val workerScriptUrl = js("import.meta.url.replace('kotlin', 'node_modules/@cashapp/sqldelight-sqljs-worker/sqljs.worker.js')")
        val driver = WebWorkerDriver(workerScriptUrl).also { YoutubeDatabase.Schema.create(it) }
        return driver
    }
}

actual class VideoDownloader {
    actual suspend fun downloadVideo(url: String, onProgress: (Float, String) -> Unit): String {
        return withContext(Dispatchers.Main) {
            try {
                console.log("Downloading video from: $url")
                val response = window.fetch(url).await()

                if (response.ok) {
                    val blob = response.blob().await()
                    console.log("Blob received:", blob)

                    val downloadUrl = URL.createObjectURL(blob)
                    onProgress(1.0f, "Download complete")

                    downloadUrl
                } else {
                    throw Exception("Failed to download video: ${response.statusText}")
                }
            } catch (e: Exception) {
                console.error("Error downloading video:", e.message)
                e.printStackTrace()
                "Error: ${e.message}"
            }
        }
    }
}