package org.company.app.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.company.app.data.model.channel.Channel
import org.company.app.theme.LocalThemeIsDark
import org.company.app.ui.screens.formatSubscribers

@Composable
fun FeaturedChannel(
    channel: Channel,
    featuredText: String
) {
    val isDark by LocalThemeIsDark.current
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp)
    ) {
        item {
            Text(
                text = featuredText,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                modifier = Modifier.fillMaxWidth().padding(start = 6.dp, end = 4.dp),
                color =if (isDark) Color.White else Color.Black
            )
        }
        channel.items.let { youtubeItem ->
            items(youtubeItem) { items ->
                FeatureChannelItems(items)
            }
        }
    }
}

@Composable
fun FeatureChannelItems(channel: org.company.app.data.model.channel.Item) {
    val isDark by LocalThemeIsDark.current
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        val channelImage: Resource<Painter> =
            asyncPainterResource(data = channel.snippet.thumbnails.high.url)
        KamelImage(
            resource = channelImage,
            contentDescription = "Channel Logo",
            modifier = Modifier.size(250.dp),
            contentScale = ContentScale.Crop,
            onFailure = {
                Text(text = "Failed to Load Image")
            },
            onLoading = {
                CircularProgressIndicator(progress = it, modifier = Modifier.size(200.dp))
            },
            animationSpec = tween()
        )

        Spacer(modifier = Modifier.width(20.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = channel.snippet.title.toString(),
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.SemiBold,
                color = if (isDark) Color.White else Color.Black
            )
            Text(
                text = formatSubscribers(channel.statistics.subscriberCount) + "." + channel.statistics.videoCount + "videos",
                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                fontWeight = FontWeight.Normal,
                color = if (isDark) Color.White.copy(alpha = 0.75f) else Color.DarkGray.copy(alpha = 0.75f)
            )
            Text(
                text = "SUBSCRIBE",
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold,
                color =Color.Red
            )
        }
    }
}