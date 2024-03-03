package org.company.app.ui.components.custom_image

import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.company.app.ui.components.common.LoadingBox

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
        onLoading = {
            LoadingBox()
        },
        onFailure = {
            Text("Failed to Load Image")
        },
        animationSpec = tween(),
    )
}