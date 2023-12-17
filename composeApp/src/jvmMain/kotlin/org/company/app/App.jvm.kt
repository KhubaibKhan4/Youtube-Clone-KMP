package org.company.app

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import java.awt.Component
import java.awt.Desktop
import java.net.URI
import java.util.Locale

internal actual fun openUrl(url: String?) {
    val uri = url?.let { URI.create(it) } ?: return
    Desktop.getDesktop().browse(uri)
}

/*@Composable
internal actual fun VideoPlayer(modifier: Modifier, url: String?, thumbnail:String?) {
    val mediaPlayerComponent = remember { initializeMediaPlayerComponent() }
    val mediaPlayer = remember { mediaPlayerComponent.mediaPlayer() }

    val factory = remember { { mediaPlayerComponent } }

    LaunchedEffect(url) { mediaPlayer.media().play*//*OR .start*//*(url) }
    DisposableEffect(Unit) { onDispose(mediaPlayer::release) }
    SwingPanel(
        factory = factory,
        background = Color.Transparent,
        modifier = modifier,
        update = {

        }
    )
}
private fun initializeMediaPlayerComponent(): Component {
    NativeDiscovery().discover()
    return if (isMacOS()) {
        CallbackMediaPlayerComponent()
    } else {
        EmbeddedMediaPlayerComponent()
    }
}*/

@Composable
internal actual fun VideoPlayer(modifier: Modifier, url: String?, thumbnail:String?) {
    VideoPlayerFFMpeg(modifier = modifier, file = url.toString())
    /*val videoPlayerState = remember { VlcjVideoPlayerState() }
    Box(Modifier.fillMaxHeight()
        .aspectRatio(videoPlayerState.aspectRatio)
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { videoPlayerState.togglePause() }
            )
        }
    ) {
        VideoPlayerDirect(
            modifier = Modifier.fillMaxSize(),
            state = videoPlayerState,
            url = url.toString()
        )
        var thumbPosition by remember { mutableStateOf(0f) }
        LaunchedEffect(videoPlayerState) {
            snapshotFlow { videoPlayerState.progress }.collect {
                thumbPosition = it
            }
        }
        Slider(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
            value = thumbPosition,
            onValueChange = {
                thumbPosition = it
                videoPlayerState.seek(it)
            },
            valueRange = 0f..1f
        )
    }*/


}
private fun initializeMediaPlayerComponent(): Component {
    NativeDiscovery().discover()
    return if (isMacOS()) {
        CallbackMediaPlayerComponent()
    } else {
        EmbeddedMediaPlayerComponent()
    }
}


private fun Component.mediaPlayer() = when (this) {
    is CallbackMediaPlayerComponent -> mediaPlayer()
    is EmbeddedMediaPlayerComponent -> mediaPlayer()
    else -> error("mediaPlayer() can only be called on vlcj player components")
}

private fun isMacOS(): Boolean {
    val os = System
        .getProperty("os.name", "generic")
        .lowercase(Locale.ENGLISH)
    return "mac" in os || "darwin" in os
}