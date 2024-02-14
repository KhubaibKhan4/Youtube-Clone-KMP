package org.company.app

import YouTubeDatabase.db.YoutubeDatabase
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
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
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.dom.Worker

internal actual fun openUrl(url: String?) {
    url?.let { window.open(it) }
}

@Composable
internal actual fun VideoPlayer(modifier: Modifier, url: String?, thumbnail: String?) {
    val videoId = extractVideoId(url.toString())
    console.log("Before YouTube API is ready $videoId")

    val iframeId = "youtube-iframe-${videoId.hashCode()}"
    val body = document.body
    val videoContainer = document.getElementById("video-container") ?: run {
        val newContainer = document.createElement("div")
        newContainer.id = "video-container"
        document.body?.appendChild(newContainer)
        newContainer
    }
    videoContainer.innerHTML = """
    <iframe 
        width="100%" 
        height="150" 
        style="background-color: black;" 
        src="https://www.youtube.com/embed/$videoId?autoplay=1&mute=1&showinfo=0" 
        title="YouTube video player" 
        frameborder="0" 
        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" 
        allowfullscreen
        modestbranding>
    </iframe>
"""

}

@Composable
internal actual fun provideShortCuts() {
    return
}

@Composable
internal actual fun Notify(message: String) {
    window.alert(message)
}

@Composable
internal actual fun ShareManager(title: String, videoUrl: String) {
    window.open(url = videoUrl, "_blank")
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal actual fun ShortsVideoPlayer(url: String?) {
    val videoId = extractVideoId(url.toString())
    console.log("Before YouTube API is ready $videoId")

    val iframeId = "youtube-iframe-${videoId.hashCode()}"
    val body = document.body

    CanvasBasedWindow {
        Column(
            modifier = Modifier.background(color = Color.Black)
        ) {
            val videoContainer = document.getElementById("video-container") ?: run {
                val newContainer = document.createElement("div")
                newContainer.id = "video-container"
                document.body?.appendChild(newContainer)
                newContainer
            }
            videoContainer.innerHTML =
                "<iframe width=\"230\" height=\"430\" style=\"background-color: transparent;\" src=\"https://www.youtube.com/embed/$videoId?autoplay=1&mute=1&showinfo=0\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture;fullscreen\" modestbranding></iframe>"
        }
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
internal actual fun isConnected(retry : () -> Unit): Boolean {
    var isConnected by remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        isConnected = try {
            val response = window.fetch("https://youtube.com").await()
            response.status == 200.toShort()
        } catch (e: dynamic) {
            false
        }
    }
    if (!isConnected){
        isConnected(retry)
    }
    return isConnected
}

actual class DriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver {
        val workerScriptUrl = js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
        val driver = WebWorkerDriver(workerScriptUrl).also { YoutubeDatabase.Schema.create(it) }
        return driver
    }
}