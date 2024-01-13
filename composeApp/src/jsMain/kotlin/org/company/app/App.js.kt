package org.company.app

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.company.app.ui.components.NetworkImage
import org.jetbrains.compose.web.dom.Div

internal actual fun openUrl(url: String?) {
    url?.let { window.open(it) }
}

@Composable
internal actual fun VideoPlayer(modifier: Modifier, url: String?, thumbnail: String?) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        NetworkImage(
            modifier = modifier,
            url = thumbnail.toString(),
            contentDescription = "Image",
            contentScale = ContentScale.FillBounds
        )
        CircularProgressIndicator()
    }
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
internal actual fun ShortsVideoPlayer(url: String?) {

    // Use rememberUpdatedState to capture the latest value of url
    val videoId = extractVideoId(url.toString())
    console.log("Before YouTube API is ready $videoId")
    val videoUrl by rememberUpdatedState(newValue = videoId)
        document.body?.innerHTML =
            "<iframe width='640' height='360' src='https://www.youtube.com/embed/$videoUrl?autoplay=1&mute=1&control=1'></iframe>"

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