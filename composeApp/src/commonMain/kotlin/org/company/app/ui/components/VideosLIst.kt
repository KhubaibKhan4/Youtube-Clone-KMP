package org.company.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.seiko.imageloader.rememberImagePainter
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.company.app.data.model.Item
import org.company.app.data.model.Youtube
import org.company.app.ui.screens.DetailScreen

@Composable
fun VideosList(youtube: Youtube) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            TopBar()
            LazyVerticalGrid(columns = GridCells.Adaptive(300.dp)) {
                items(youtube.items) { videos ->
                    VideoItemCard(videos)
                }
            }
        }
    }
}

@Composable
fun VideoItemCard(video: Item) {
    val navigator = LocalNavigator.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navigator?.push(DetailScreen(video))
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = rememberImagePainter(video.snippet.thumbnails.medium.url),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                // Video Total Time
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.primary)
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    Text(
                        text = video.contentDetails?.duration?.let { formatVideoDuration(it) }
                            ?: "00:00",
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Channel Image
                Image(
                    painter = rememberImagePainter(video.snippet.thumbnails.default.url),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )

                Spacer(modifier = Modifier.width(8.dp))// Video Title and Time in a Box
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = video.snippet.title,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        fontSize = 12.sp,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp // Adjust the lineHeight value as needed
                    )
                    //Spacer(modifier = Modifier.height(4.dp))

                    // Channel Name, Views, Time
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = video.snippet.channelTitle, fontSize = 10.sp)
                        Text(text = "•")
                        Text(
                            text = "${video.statistics?.viewCount?.let { formatViewCount(it) }} views",
                            fontSize = 10.sp
                        )
                        Text(text = "•")
                        Text(
                            text = getFormattedDate(video.snippet.publishedAt),
                            fontSize = 10.sp,
                            maxLines = 1,
                            modifier = Modifier
                                .widthIn(min = 0.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))

                // Vertical Three Dots Icon
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

fun formatVideoDuration(duration: String?): String {
    if (duration == null) return "00:00"

    val regex = Regex("PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?")
    val matchResult = regex.find(duration)

    val hours = matchResult?.groups?.get(1)?.value?.toIntOrNull() ?: 0
    val minutes = matchResult?.groups?.get(2)?.value?.toIntOrNull() ?: 0
    val seconds = matchResult?.groups?.get(3)?.value?.toIntOrNull() ?: 0

    return buildString {
        if (hours > 0) {
            append("${if (hours < 10) "0" else ""}$hours:")
        }
        append("${if (minutes < 10) "0" else ""}$minutes:")
        append("${if (seconds < 10) "0" else ""}$seconds")
    }
}


fun getFormattedDate(publishedAt: String): String {
    return try {
        val instant = Instant.parse(publishedAt)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val currentInstant = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())

        val seconds = currentInstant.epochSeconds - instant.epochSeconds
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

@Composable
fun formatViewCount(viewCount: String): String {
    val count = viewCount.toLong()

    return when {
        count < 1_000 -> "$count"
        count < 1_000_000 -> "${count / 1_000}k"
        count < 1_000_000_000 -> "${count / 1_000_000}M"
        else -> "${count / 1_000_000_000}B"
    }
}



