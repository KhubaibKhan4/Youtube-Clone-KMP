package org.company.app

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import kotlinx.browser.window
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