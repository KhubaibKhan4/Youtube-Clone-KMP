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
import kotlinx.coroutines.await
import org.company.app.html.LocalLayerContainer
import org.company.app.video.HTMLVideoPlayer
import org.w3c.dom.Worker

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
internal actual fun Notify(message: String) {
    window.alert(message)
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
    /*if (!isConnected){
        isConnected(retry)
    }*/
    return isConnected
}

actual class DriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver {
        val workerScriptUrl = js("import.meta.url.replace('kotlin', 'node_modules/@cashapp/sqldelight-sqljs-worker/sqljs.worker.js')")
        val driver = WebWorkerDriver(workerScriptUrl).also { YoutubeDatabase.Schema.create(it) }
        return driver
    }
}