package org.company.app.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.PlaylistAdd
import androidx.compose.material.icons.outlined.PlaylistAddCheck
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
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
import org.company.app.ui.screens.DetailScreen

@Composable
fun ChannelHome(
    youtube: Youtube,
    modifier: Modifier,
    title: String,
) {
    var isExpanded by remember { mutableStateOf(false) }
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
        val visibleVideoCount = if (isExpanded) youtube.items?.size ?: 0 else 3
        items(visibleVideoCount) { index ->
            youtube.items?.getOrNull(index)?.let { videos ->
                ChannelHomeItems(videos)
            }
        }

        // Display the expand/collapse button
        if (youtube.items?.size ?: 0 > visibleVideoCount) {
            item {
                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand/Collapse"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelHomeItems(videos: Item) {
    var moreVertEnable by remember { mutableStateOf(false) }
    val navigator = LocalNavigator.current
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
                .clickable {
                    navigator?.push(DetailScreen(video = videos))
                }
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
                        onClick = {
                            moreVertEnable = !moreVertEnable
                        },
                        modifier = Modifier.align(alignment = Alignment.TopEnd)
                    ) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More Vert")
                    }
                }

            }
            if (moreVertEnable) {
                ModalBottomSheet(
                    onDismissRequest = {
                        moreVertEnable = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    sheetState = rememberModalBottomSheetState(),
                    shape = RoundedCornerShape(4.dp),
                    contentColor = Color.Black,  // Adjust color as needed
                    scrimColor = Color.Transparent,
                    tonalElevation = 4.dp,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Outlined.PlaylistAdd,
                                contentDescription = "Time"
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            androidx.compose.material3.Text(
                                text = "Play in next queue",
                                modifier = Modifier.weight(1f),
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize
                            )
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Outlined.PlaylistAddCheck,
                                contentDescription = "Time"
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Outlined.WatchLater,
                                contentDescription = "Time"
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            androidx.compose.material3.Text(
                                text = "Save to Watch later",
                                modifier = Modifier.weight(1f),
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Outlined.PlaylistAdd,
                                contentDescription = "Time"
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            androidx.compose.material3.Text(
                                text = "Save to playlist",
                                modifier = Modifier.weight(1f),
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Outlined.Download,
                                contentDescription = "Time"
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            androidx.compose.material3.Text(
                                text = "Download video",
                                modifier = Modifier.weight(1f),
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = "Time"
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            androidx.compose.material3.Text(
                                text = "Share",
                                modifier = Modifier.weight(1f),
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
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