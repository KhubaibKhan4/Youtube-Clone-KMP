package org.company.app.ui

import android.graphics.RenderEffect
import android.os.Build
import androidx.annotation.IntRange
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(UnstableApi::class)
@Composable
fun CustomVideoPlayer(
    modifier: Modifier = Modifier,
    videoUrl: String?,
    thumbnailResId: String,
    isPlaying: Boolean,
    onClickPlay: () -> Unit
) {
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    var isVideoLoading by remember { mutableStateOf(true) }
    var isFullScreen by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        // Set media source when the component is first composed
        if (videoUrl != null) {
            val mediaItem = MediaItem.fromUri(videoUrl)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }

        // Dispose the ExoPlayer when the component is no longer in use
        onDispose {
            exoPlayer.release()
        }
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            // Start playback when isPlaying is true
            exoPlayer.play()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable {
                onClickPlay()
            }
    ) {
        // Display the thumbnail and play icon
        if (!isPlaying) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black)
            ) {
                val image: Resource<Painter> = asyncPainterResource(data = thumbnailResId)
                KamelImage(
                    resource = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Display play icon in the center
                IconButton(
                    onClick = {
                        onClickPlay()
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        } else {
            // Display video player
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black),
                factory = { context ->
                    PlayerView(context).apply {
                        player = exoPlayer
                        // Add any additional settings here if needed
                        hideController() // Manually hide the controller
                        setShowNextButton(true)
                        setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
                        setControllerHideDuringAds(true)
                        setShowFastForwardButton(true)
                        setShowPreviousButton(true)
                        setBackgroundColor(0XFF000000.toInt())
                        exoPlayer.addListener(object : Player.Listener {
                            override fun onIsLoadingChanged(isLoading: Boolean) {
                                isVideoLoading = isLoading
                            }

                            override fun onIsPlayingChanged(isPlaying: Boolean) {
                                isVideoLoading = !isPlaying
                            }

                            override fun onPlayerError(error: PlaybackException) {
                                Log.e("ExoPlayer", "Error during playback. Video URL: $videoUrl", error)
                            }
                        })
                    }
                },
                update = {
                    // You can add any additional update logic if needed
                }
            )
        }
        IconButton(
            onClick = {
                // Handle click on full-screen icon

            },
            modifier = Modifier
                .padding(end = 8.dp)
                .align(alignment = Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White
            )
        }

        IconButton(
            onClick = {
                // Handle click on full-screen icon
                isFullScreen = !isFullScreen
            },
            modifier = Modifier
                .padding(end = 8.dp)
                .align(alignment = Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = if (isFullScreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                contentDescription = null,
                tint = Color.White
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Switch(
                checked = isPlaying,
                onCheckedChange = { isChecked ->
                    // Handle autoplay switch change
                    // You can add additional logic here if needed
                },
                modifier = Modifier.padding(end = 8.dp),
            )
            Icon(
                imageVector = Icons.Default.Cast,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp),
                tint = Color.White
            )
            Icon(
                imageVector = Icons.Default.ClosedCaption,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp),
                tint = Color.White
            )
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = Color.White
            )
        }

        // Display circular progress bar while loading video
        if (isVideoLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}




