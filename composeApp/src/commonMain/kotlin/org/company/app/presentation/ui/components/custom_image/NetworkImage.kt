package org.company.app.presentation.ui.components.custom_image

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.company.app.presentation.ui.components.common.LoadingBox

@Composable
fun NetworkImage(
    modifier: Modifier,
    url: String,
    contentDescription: String?,
    contentScale: ContentScale
) {
    val image: Resource<Painter> = asyncPainterResource(data = url)
    KamelImage(
        resource = image,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        onFailure = {
            Text("Failed to Load Image")
        },
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 100,
            easing = EaseInOutSine
        ),
    )
}