package org.company.app.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FeaturedPlayList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Sort
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
import org.company.app.data.model.channel.Channel
import org.company.app.data.model.videos.Item
import org.company.app.data.model.videos.Youtube
import kotlin.random.Random

@Composable
fun ChannelVideos(
    channel: Channel,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        modifier = Modifier.fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(channel.items) { videos ->
            ChannelVideosItems(videos)
        }
    }
}

@Composable
fun ChannelVideosItems(videos: org.company.app.data.model.channel.Item) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // Thumbnail with playlist count overlay
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(80.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            val image: Resource<Painter> =
                asyncPainterResource(data = videos.snippet?.thumbnails?.high?.url.toString())
            KamelImage(
                resource = image,
                contentDescription = "Thumbnail",
                modifier = Modifier.fillMaxSize()
                    .clip(
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
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
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Video Title
            Text(
                text = videos.snippet?.title.toString(),
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(0.75f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Playlist Title
                Text(
                    text = videos.snippet?.title.toString(),
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = Color.DarkGray
                )

                // More options icon
               Box(contentAlignment = Alignment.TopEnd){
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
private fun getRandomColors(): Color{
    val random = Random
    return Color(
        red = random.nextFloat(),
        green = random.nextFloat(),
        blue = random.nextFloat(),
        alpha = 0.75f
    )
}


