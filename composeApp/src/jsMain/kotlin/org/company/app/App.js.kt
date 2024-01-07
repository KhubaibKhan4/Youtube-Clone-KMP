package org.company.app

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.company.app.ui.components.NetworkImage

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