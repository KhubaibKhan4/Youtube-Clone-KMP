package org.company.app.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import org.company.app.VideoPlayer
import org.company.app.data.model.channel.Channel
import org.company.app.data.model.videos.Item
import org.company.app.domain.repository.Repository
import org.company.app.domain.usecases.ChannelState
import org.company.app.domain.usecases.YoutubeState
import org.company.app.presentation.MainViewModel
import org.company.app.ui.components.ErrorBox
import org.company.app.ui.components.LoadingBox
import org.company.app.ui.components.RelevanceList

class DetailScreen(
    private val video: Item? = null,
    private val search: org.company.app.data.model.search.Item? = null
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val repository = remember { Repository() }
        val viewModel = remember { MainViewModel(repository) }
        var state by remember { mutableStateOf<ChannelState>(ChannelState.LOADING) }
        var stateRelevance by remember { mutableStateOf<YoutubeState>(YoutubeState.LOADING) }
        var channelData by remember { mutableStateOf<Channel?>(null) }
        var descriptionEnabled by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            video?.snippet?.channelId?.let { viewModel.getChannelDetails(it) }
            viewModel.getRelevance()
        }
        state = viewModel.channelDetails.collectAsState().value
        stateRelevance = viewModel.relevance.collectAsState().value

        when (state) {
            is ChannelState.LOADING -> {
                LoadingBox()
            }

            is ChannelState.SUCCESS -> {
                var data = (state as ChannelState.SUCCESS).channel
                channelData = data

            }

            is ChannelState.ERROR -> {
                val error = (state as ChannelState.ERROR).error
                ErrorBox(error)
            }
        }

        //https://www.youtube.com/watch?v=${video.id}
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Thumbnail
            VideoPlayer(
                modifier = Modifier.fillMaxWidth()
                    .height(220.dp),
                url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                thumbnail = video?.snippet?.thumbnails?.high?.url
            )
            /*val image: Resource<Painter> =
                asyncPainterResource(data = video?.snippet?.thumbnails?.high?.url.toString())
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
            )*/
            // Title and Arrow Down Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                video?.snippet?.title?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .weight(0.9f)
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp).clickable {
                        descriptionEnabled = true
                    }
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
                    text = "${formatViewCount(video?.statistics?.viewCount)} views - ${
                        video?.snippet?.publishedAt?.let {
                            getFormattedDate(
                                it
                            )
                        }
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
                    .padding(horizontal = 16.dp, vertical = 8.dp),
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
                            text = formatLikes(video?.statistics?.likeCount),
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
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Channel Image
                channelData?.items?.get(0)?.snippet?.thumbnails?.default?.url?.let {
                    rememberImagePainter(
                        it
                    )
                }?.let {
                    Image(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.FillBounds
                    )
                }

                // Channel Info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    video?.snippet?.channelTitle?.let {
                        Text(
                            text = it,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    Text(
                        text = "${formatSubscribers(channelData?.items?.get(0)?.statistics?.subscriberCount)} Subscribers",
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
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User Image (Replace the imagePainter with your actual logic)
                video?.snippet?.thumbnails?.default?.url?.let {
                    rememberImagePainter(
                        it
                    )
                }?.let {
                    Image(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                }

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
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            RelevanceList(stateRelevance)

            if (descriptionEnabled) {
                ModalBottomSheet(
                    onDismissRequest = {
                        descriptionEnabled = false
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    sheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = true,
                        confirmValueChange = { true }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = Color.Black,
                    tonalElevation = 8.dp,
                    scrimColor = Color.Transparent,
                    dragHandle = null,
                    windowInsets = BottomSheetDefaults.windowInsets,
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Description",
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                modifier = Modifier.fillMaxWidth().weight(1f).padding(start = 4.dp)
                            )

                            IconButton(onClick = {
                                descriptionEnabled = false
                            }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = null)
                            }
                        }

                        Divider(
                            modifier = Modifier.fillMaxWidth().padding(2.dp),
                            thickness = 2.dp,
                            color = DividerDefaults.color
                        )

                        video?.snippet?.title?.let {
                            Text(
                                text = it,
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                modifier = Modifier.fillMaxWidth()
                                    .padding(12.dp),
                                maxLines = 2,
                                textAlign = TextAlign.Justify,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(start = 10.dp, end = 2.dp, top = 4.dp, bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // Channel Image
                            channelData?.items?.get(0)?.snippet?.thumbnails?.default?.url?.let {
                                rememberImagePainter(
                                    it
                                )
                            }?.let {
                                Image(
                                    painter = it,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(15.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            channelData?.items?.get(0)?.snippet?.title?.let {
                                Text(
                                    text = it,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                    modifier = Modifier.fillMaxWidth(),
                                    maxLines = 1,
                                    textAlign = TextAlign.Justify,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }

                        }

                        //Video Details
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = 20.dp, start = 60.dp, end = 60.dp, bottom = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = formatLikes(video?.statistics?.likeCount),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                                )

                                Text(
                                    text = "Likes",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                                )
                            }

                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = formatViewCount(video?.statistics?.viewCount),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                                )

                                Text(
                                    text = "Views",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                                )
                            }

                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val (formattedMonth, day, year) = getFormattedDateLikeMonthDay(video?.snippet?.publishedAt.toString())

                                Text(
                                    text = "$formattedMonth $day",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                                )

                                Text(
                                    text = "$year",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                                )
                            }
                        }

                        Divider(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            thickness = 2.dp,
                            color = DividerDefaults.color
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            var desc_expanded by remember { mutableStateOf(false) }
                            video?.snippet?.description?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier.fillMaxWidth().weight(1f)
                                        .padding(top = 16.dp, start = 4.dp, end = 4.dp),
                                    maxLines = if (desc_expanded) 40 else 9,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                                )
                            }
                            Text(
                                text = if (desc_expanded) "less" else "more",
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                modifier = Modifier
                                    .clickable {
                                        desc_expanded = !desc_expanded
                                    }
                                    .align(alignment = Alignment.Bottom)
                            )
                        }


                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 8.dp,
                            color = DividerDefaults.color
                        )

                        channelData?.items?.get(0)?.snippet?.title?.let {
                            Text(
                                text = "More From $it",
                                fontWeight = FontWeight.Normal,
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 8.dp
                                    ), // Adjust padding as needed
                                maxLines = 1,
                                textAlign = TextAlign.Justify,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 8.dp,
                                    vertical = 4.dp
                                ), // Adjust padding as needed
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            OutlinedCard(
                                onClick = {},
                                shape = CardDefaults.outlinedShape,
                                enabled = true,
                                border = BorderStroke(width = 1.dp, color = Color.Black),
                                modifier = Modifier.weight(1f).padding(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.VideoLibrary,
                                        contentDescription = "Videos",
                                        modifier = Modifier.padding(8.dp)
                                    )
                                    Text(
                                        "VIDEOS",
                                        textAlign = TextAlign.Center,
                                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }

                            OutlinedCard(
                                onClick = {},
                                shape = CardDefaults.outlinedShape,
                                enabled = true,
                                border = BorderStroke(width = 1.dp, color = Color.Black),
                                modifier = Modifier.weight(1f).padding(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.AccountBox,
                                        contentDescription = "About",
                                        modifier = Modifier.padding(8.dp)
                                    )
                                    Text(
                                        "ABOUT",
                                        textAlign = TextAlign.Center,
                                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }

                    }
                }
            }
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

fun getFormattedDateLikeMonthDay(videoPublishedAt: String): Triple<String, Int, Int> {
    return try {
        val instant = Instant.parse(videoPublishedAt)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val months = arrayOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )

        val formattedMonth = months[localDateTime.monthNumber - 1]
        val dayOfMonth = localDateTime.dayOfMonth
        val year = localDateTime.year

        Triple(formattedMonth, dayOfMonth, year)
    } catch (e: Throwable) {
        Triple("Unknown", 0, 0)
    }
}

