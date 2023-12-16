package org.company.app.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun CustomVideoPlayerWrapper(
    modifier: Modifier = Modifier,
    videoUrl: String?,
    thumbnailResId: String,
    isPlaying: Boolean,
    onClickPlay: () -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            ComposeView(context).apply {
                setContent {
                    CustomVideoPlayer(
                        videoUrl = videoUrl,
                        thumbnailResId = thumbnailResId,
                        isPlaying = isPlaying,
                        onClickPlay = onClickPlay
                    )
                }
            }
        }
    )
}