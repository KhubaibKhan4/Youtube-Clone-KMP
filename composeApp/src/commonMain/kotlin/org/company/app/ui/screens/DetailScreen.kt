package org.company.app.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.seiko.imageloader.rememberImagePainter
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.company.app.data.model.videos.Item
import org.company.app.data.model.videos.Youtube
import org.company.app.domain.repository.Repository
import org.company.app.domain.usecases.YoutubeState
import org.company.app.presentation.MainViewModel
import org.company.app.ui.components.ErrorBox
import org.company.app.ui.components.LoadingBox
import org.company.app.ui.components.RelevanceList

class DetailScreen(
    private val video: Item,
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val repository = remember { Repository() }
        val viewModel = remember { MainViewModel(repository) }
        var state by remember { mutableStateOf<YoutubeState>(YoutubeState.LOADING) }
        var relevanceData by remember { mutableStateOf<Youtube?>(null) }

        LaunchedEffect(Unit) {
            viewModel.getRelevance(video.id)
        }
        state = viewModel.relevance.collectAsState().value

        when (state) {
            is YoutubeState.LOADING -> {
                LoadingBox()
            }

            is YoutubeState.SUCCESS -> {
                var data = (state as YoutubeState.SUCCESS).youtube
                relevanceData = data

            }

            is YoutubeState.ERROR -> {
                val error = (state as YoutubeState.ERROR).error
                ErrorBox(error)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Thumbnail
            val image: Resource<Painter> = asyncPainterResource(data = video.snippet.thumbnails.high.url)
            KamelImage(
                resource = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                onLoading = {
                    CircularProgressIndicator(it)
                },
                onFailure = {
                    Text(text = "Failed to Load Image")
                },
                animationSpec = tween()
            )
            // Title and Arrow Down Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = video.snippet.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(0.9f)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Views, Date, Likes, Dislikes, Share, Download, Save
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${formatViewCount(video.statistics?.viewCount)} views - ${
                        getFormattedDate(
                            video.snippet.publishedAt
                        )
                    }",
                    fontSize = 14.sp
                )
            }
            // Horizontal Divider
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(vertical = 8.dp),
                thickness = 4.dp,
                color = Color.DarkGray
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp,vertical = 8.dp),
            ) {
                // Thumbs Up
                Card(
                    modifier = Modifier
                        .height(40.dp)
                        .padding(4.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp).clickable { },
                            tint = Color.Black
                        )

                        Text(
                            text = formatLikes(video.statistics?.likeCount),
                            fontSize = 14.sp,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        // Vertical line
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                                .background(Color.White)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        // Thumbs Down
                        Icon(
                            imageVector = Icons.Default.ThumbDown,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp).clickable { },
                            tint = Color.Black
                        )
                    }
                }

                // Share
                Card(
                    modifier = Modifier
                        .height(40.dp)
                        .padding(4.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    onClick = { /* Handle Share click */ },
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Black
                        )

                        Text(
                            text = "Share",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }

                // Download
                Card(
                    modifier = Modifier
                        .height(40.dp)
                        .padding(4.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    onClick = { /* Handle Download click */ },
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Black
                        )

                        Text(
                            text = "Download",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }

                // Save
                Card(
                    modifier = Modifier
                        .height(40.dp)
                        .padding(4.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    onClick = { /* Handle Save click */ },
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Black
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(6.dp))

            // Horizontal Divider
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                thickness = 1.dp, // Adjust the thickness as needed
                color = Color.LightGray // Use a different color for better visibility
            )


            // Channel Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp,vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Channel Image
                Image(
                    painter = rememberImagePainter(video.snippet.thumbnails.default.url),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )

                // Channel Info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = video.snippet.channelTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${formatSubscribers(video.statistics?.commentCount)} Subscribers",
                        fontSize = 14.sp
                    )

                }
                Text(
                    text = "SUBSCRIBE",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { /* Handle subscribe click */ }
                )
            }

            // Horizontal Divider
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                thickness = 1.dp, // Adjust the thickness as needed
                color = Color.LightGray // Use a different color for better visibility
            )


            // Sample Comment
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp,vertical = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User Image (Replace the imagePainter with your actual logic)
                Image(
                    painter = rememberImagePainter(video.snippet.thumbnails.default.url),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                // Comment Text
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "amazing video... ", modifier = Modifier.padding(start = 3.dp))
                }

                // Arrow Right Icon
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp).align(alignment = Alignment.CenterEnd)
                    )
                }
            }


            // Horizontal Divider
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                thickness = 1.dp, // Adjust the thickness as needed
                color = Color.LightGray // Use a different color for better visibility
            )
            // More Videos Section (Lazy Column)
            Text(
                text = "More Videos",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp,vertical = 8.dp),
            )
            relevanceData?.let { RelevanceList(it) }
        }
    }
}


// Utility functions for formatting
fun formatViewCount(count: String?): String {
    return count?.toDoubleOrNull()?.let {
        when {
            it >= 1_000_000_000 -> "${(it / 1_000_000_000).toInt()}B"
            it >= 1_000_000 -> "${(it / 1_000_000).toInt()}M"
            it >= 1_000 -> "${(it / 1_000).toInt()}K"
            else -> "$it"
        }
    } ?: "0"
}

fun formatViewComments(count: String?): String {
    return count?.toDoubleOrNull()?.let {
        when {
            it >= 1_000_000_000 -> "${(it / 1_000_000_000).toInt()}B"
            it >= 1_000_000 -> "${(it / 1_000_000).toInt()}M"
            it >= 1_000 -> "${(it / 1_000).toInt()}K"
            else -> "$it"
        }
    } ?: "0"
}

fun formatLikes(count: String?): String {
    return count?.toDoubleOrNull()?.let {
        when {
            it >= 1_000_000 -> "${(it / 1_000_000).toInt()}M"
            it >= 1_000 -> "${(it / 1_000).toInt()}K"
            else -> "$it"
        }
    } ?: "0"
}

fun formatSubscribers(count: String?): String {
    return count?.toDoubleOrNull()?.let {
        when {
            it >= 1_000_000_000 -> "${(it / 1_000_000_000).toInt()}B"
            it >= 1_000_000 -> "${(it / 1_000_000).toInt()}M"
            it >= 1_000 -> "${(it / 1_000).toInt()}K"
            else -> "$it"
        }
    } ?: "0"
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
