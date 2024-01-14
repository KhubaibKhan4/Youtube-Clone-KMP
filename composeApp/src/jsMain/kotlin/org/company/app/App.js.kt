package org.company.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.window.CanvasBasedWindow
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Canvas
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Iframe
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposableInBody
import org.w3c.dom.HTMLIFrameElement
import org.w3c.dom.Node

internal actual fun openUrl(url: String?) {
    url?.let { window.open(it) }
}
@Composable
internal actual fun VideoPlayer(modifier: Modifier, url: String?, thumbnail: String?) {
    // Use rememberUpdatedState to capture the latest value of url
    val videoId = extractVideoId(url.toString())
    console.log("Before YouTube API is ready $videoId")

    val iframeId = "youtube-iframe-${videoId.hashCode()}"
    val body = document.body
            // Assuming you have a div with the id "video-container"
            val videoContainer = document.getElementById("video-container") ?: run {
                val newContainer = document.createElement("div")
                newContainer.id = "video-container"
                document.body?.appendChild(newContainer)
                newContainer
            }
           // videoContainer.innerHTML = "<iframe width=\"100%\" height=\"350\" src=\"https://www.youtube.com/embed/$videoId?autoplay=1&mute=1\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture;fullscreen\"></iframe>"
           // videoContainer.innerHTML = "<iframe width=\"100%\" height=\"350\" style=\"background-color: black;\" src=\"https://www.youtube.com/embed/$videoId?autoplay=1&mute=1&showinfo=0\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture;fullscreen\" modestbranding></iframe>"
    videoContainer.innerHTML = """
    <iframe 
        width="100%" 
        height="350" 
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
    // Use rememberUpdatedState to capture the latest value of url
    val videoId = extractVideoId(url.toString())
    console.log("Before YouTube API is ready $videoId")

    val iframeId = "youtube-iframe-${videoId.hashCode()}"
    val body = document.body

    CanvasBasedWindow {
        Column(
            modifier = Modifier.background(color = Color.Black)
        ) {
            // Assuming you have a div with the id "video-container"
            val videoContainer = document.getElementById("video-container") ?: run {
                val newContainer = document.createElement("div")
                newContainer.id = "video-container"
                document.body?.appendChild(newContainer)
                newContainer
            }
            videoContainer.innerHTML = "<iframe width=\"230\" height=\"430\" style=\"background-color: transparent;\" src=\"https://www.youtube.com/embed/$videoId?autoplay=1&mute=1&showinfo=0\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture;fullscreen\" modestbranding></iframe>"
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
internal actual fun isConnected(): Boolean {
    var isConnected by remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        try {
            val response = window.fetch("https://youtube.com").await()
            isConnected = response.status == 200.toShort()
        } catch (e: dynamic) {
            isConnected = false
        }
    }
    return isConnected
}