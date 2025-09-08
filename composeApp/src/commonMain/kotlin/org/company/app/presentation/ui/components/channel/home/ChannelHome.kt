package org.company.app.presentation.ui.components.channel.home

import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.PlaylistAdd
import androidx.compose.material.icons.automirrored.outlined.PlaylistAddCheck
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Download
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
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.company.app.domain.model.channel.Item
import org.company.app.domain.model.videos.Youtube
import org.company.app.domain.usecases.ResultState
import org.company.app.presentation.ui.components.common.ErrorBox
import org.company.app.presentation.ui.components.shimmer.ShimmerEffectSingleVideo
import org.company.app.presentation.ui.screens.detail.DetailScreen
import org.company.app.presentation.viewmodel.MainViewModel
import org.company.app.theme.LocalThemeIsDark
import org.company.app.core.common.formatVideoDuration
import org.company.app.core.common.getFormattedDateHome
import org.koin.compose.koinInject

@Composable
fun ChannelHome(
    youtube: Youtube,
    modifier: Modifier,
    title: String,
    channel: Item,
    logo: String,
    viewModel: MainViewModel = koinInject(),
) {
    val isDark by LocalThemeIsDark.current
    var isExpanded by remember { mutableStateOf(false) }
    var videosList by remember { mutableStateOf<Youtube?>(null) }
    val videosIds = youtube.items?.map { it.id }
    val formattedIds = videosIds.toString().replace("[", "").replace("]", "")
    val newFormattedIds = formattedIds.split(",").map { it.trim() }.joinToString(",")
    LaunchedEffect(Unit) {
        viewModel.getVideosUsingIds(newFormattedIds)
    }
    val videoState by viewModel.videosUsingIds.collectAsState()
    when (videoState) {
        is ResultState.ERROR -> {
            val error = (videoState as ResultState.ERROR).error
            ErrorBox(error)
        }

        is ResultState.LOADING -> {
            ShimmerEffectSingleVideo()
        }

        is ResultState.SUCCESS -> {
            val response = (videoState as ResultState.SUCCESS).response
            videosList = response
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        modifier = modifier.fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = title,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = if (isDark) Color.White else Color.Black
            )
        }
        val visibleVideoCount = if (isExpanded) videosList?.items?.size ?: 0 else 3
        items(visibleVideoCount) { index ->
            videosList?.items?.getOrNull(index)?.let { videos ->
                ChannelHomeItems(videos, channel, logo)
            }
        }
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
                        contentDescription = "Expand/Collapse",
                        tint = if (isDark) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelHomeItems(
    videos: org.company.app.domain.model.videos.Item,
    channel: Item,
    logo: String,
) {
    var moreVertEnable by remember { mutableStateOf(false) }
    val navigator = LocalNavigator.current
    val isDark by LocalThemeIsDark.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            val image: Resource<Painter> =
                asyncPainterResource(data = videos.snippet?.thumbnails?.high?.url.toString())
            KamelImage(
                resource = image,
                contentDescription = "Thumbnail",
                modifier = Modifier.width(140.dp)
                    .height(80.dp)
                    .clickable {
                        navigator?.push(
                            DetailScreen(
                                video = videos,
                                channelData = channel,
                                logo = logo,
                                subscribersCount = channel.statistics?.subscriberCount.toString()
                            )
                        )
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
                    CircularProgressIndicator(
                        progress = { it },
                    )
                },
                animationSpec = tween()
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.8f))
                    .clip(RoundedCornerShape(4.dp))
            ) {
                androidx.compose.material3.Text(
                    text = videos.contentDetails?.duration?.let { formatVideoDuration(it) }
                        ?: "00:00",
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = videos.snippet?.title.toString(),
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = if (isDark) Color.White else Color.Black
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = formatVideoDuration(videos.contentDetails?.duration.toString()),
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = if (isDark) Color.White else Color.LightGray
                )
                Text(
                    text = getFormattedDateHome(videos.snippet?.publishedAt.toString()),
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = if (isDark) Color.White else Color.LightGray
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = {
                            moreVertEnable = !moreVertEnable
                        },
                        modifier = Modifier.align(alignment = Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Vert",
                            tint = if (isDark) Color.White else Color.Black
                        )
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
                    contentColor = if (isDark) Color.White else Color.Black,
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
                                imageVector = Icons.AutoMirrored.Outlined.PlaylistAdd,
                                contentDescription = "Time",
                                tint = if (isDark) Color.White else Color.Black
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            androidx.compose.material3.Text(
                                text = "Play in next queue",
                                modifier = Modifier.weight(1f),
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                color = if (isDark) Color.White else Color.Black
                            )
                            androidx.compose.material3.Icon(
                                imageVector = Icons.AutoMirrored.Outlined.PlaylistAddCheck,
                                contentDescription = "Time",
                                tint = if (isDark) Color.White else Color.Black
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
                                contentDescription = "Time",
                                tint = if (isDark) Color.White else Color.Black
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            androidx.compose.material3.Text(
                                text = "Save to Watch later",
                                modifier = Modifier.weight(1f),
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                color = if (isDark) Color.White else Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.AutoMirrored.Outlined.PlaylistAdd,
                                contentDescription = "Time",
                                tint = if (isDark) Color.White else Color.Black
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            androidx.compose.material3.Text(
                                text = "Save to playlist",
                                modifier = Modifier.weight(1f),
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                color = if (isDark) Color.White else Color.Black
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
                                contentDescription = "Time",
                                tint = if (isDark) Color.White else Color.Black
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            androidx.compose.material3.Text(
                                text = "Download video",
                                modifier = Modifier.weight(1f),
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                color = if (isDark) Color.White else Color.Black
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
                                contentDescription = "Time",
                                tint = if (isDark) Color.White else Color.Black
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            androidx.compose.material3.Text(
                                text = "Share",
                                modifier = Modifier.weight(1f),
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                color = if (isDark) Color.White else Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}