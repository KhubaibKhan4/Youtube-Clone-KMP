package org.company.app.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.company.app.data.model.videos.Item
import org.company.app.data.model.videos.Youtube

@Composable
fun ChannelHome(
    youtube: Youtube,
    modifier: Modifier,
    title: String,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        modifier = modifier.fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = title,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
        }
        items(youtube.items!!) { videos ->
            ChannelHomeItems(videos)
        }
    }
}

@Composable
fun ChannelHomeItems(videos: Item) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        val image: Resource<Painter> =
            asyncPainterResource(data = videos.snippet?.thumbnails?.high?.url.toString())
        KamelImage(
            resource = image,
            contentDescription = "Thumbnail",
            modifier = Modifier.width(140.dp)
                .height(80.dp)
                .clip(
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            onFailure = {
                Text("Failed to Load Image")
            },
            onLoading = {
                CircularProgressIndicator(progress = it)
            },
            animationSpec = tween()
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            //Video Title
            Text(
                text = videos.snippet?.title.toString(),
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //Date
                Text(
                    text = getFormattedDateHome(videos.snippet?.publishedAt.toString()),
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = Color.LightGray
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = {},
                        modifier = Modifier.align(alignment = Alignment.TopEnd)
                    ) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More Vert")
                    }
                }

            }
        }
    }
}

fun getFormattedDateHome(publishedAt: String): String {
    return try {
        val instant = Instant.parse(publishedAt)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val currentInstant = Clock.System.now()

        val seconds = (currentInstant - instant).inWholeSeconds
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        when {
            seconds < 60 -> "$seconds seconds ago"
            minutes < 60 -> "$minutes minutes ago"
            hours < 24 -> "$hours hours ago"
            else -> "$days days ago"
        }
    } catch (e: Throwable) {
        "Unknown date"
    }
}