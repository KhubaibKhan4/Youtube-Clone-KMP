package org.company.app.ui.components

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
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.PlaylistAdd
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.company.app.data.model.videos.Item
import org.company.app.data.model.videos.Youtube
import org.company.app.theme.LocalThemeIsDark
import org.company.app.ui.screens.DetailScreen

@Composable
fun PlaylistsVideosList(youtube: Youtube) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            LazyVerticalGrid(columns = GridCells.Adaptive(300.dp)) {
                youtube.items?.let { items ->
                    items(items) { videos ->
                        PlaylistsVideoItemCard(videos)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistsVideoItemCard(video: Item) {
    val navigator = LocalNavigator.current
    val isDark by LocalThemeIsDark.current
    var moreVertEnable by remember { mutableStateOf(false) }
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
                val image: Resource<Painter> =
                    asyncPainterResource(data = video.snippet?.thumbnails?.high?.url!!)

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
                    animationSpec = tween(),
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
                        color = if (isDark) Color.White else Color.Black,
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
                val image: Resource<Painter> =
                    asyncPainterResource(data = video.snippet!!.thumbnails?.high?.url.toString())
                KamelImage(
                    resource = image,
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
                        text = video.snippet.title.toString(),
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        color = if (isDark) Color.White else Color.Black,
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
                        Text(
                            text = video.snippet.channelTitle.toString(),
                            fontSize = 10.sp,
                            color = if (isDark) Color.White else Color.Black
                        )
                        Text(
                            text = "•",
                            color = if (isDark) Color.White else Color.Black
                        )
                        Text(
                            text = "${video.statistics?.viewCount?.let { formatViewCount(it) }} views",
                            fontSize = 10.sp,
                            color = if (isDark) Color.White else Color.Black
                        )
                        Text(
                            text = "•",
                            color = if (isDark) Color.White else Color.Black
                        )
                        Text(
                            text = getFormattedDate(video.snippet.publishedAt.toString()),
                            fontSize = 10.sp,
                            maxLines = 1,
                            modifier = Modifier
                                .widthIn(min = 0.dp),
                            color = if (isDark) Color.White else Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))

                // Vertical Three Dots Icon
                IconButton(onClick = {
                    moreVertEnable = !moreVertEnable
                }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (isDark) Color.White else Color.Black
                    )
                }
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
            contentColor =if (isDark) Color.White else Color.Black,  // Adjust color as needed
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
                    Icon(
                        imageVector = Icons.Outlined.WatchLater,
                        contentDescription = "Time",
                        tint = if (isDark) Color.White else Color.Black
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
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
                    Icon(
                        imageVector = Icons.Outlined.PlaylistAdd,
                        contentDescription = "Time",
                        tint = if (isDark) Color.White else Color.Black
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
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
                    Icon(
                        imageVector = Icons.Outlined.Download,
                        contentDescription = "Time",
                        tint = if (isDark) Color.White else Color.Black
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
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
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Time",
                        tint = if (isDark) Color.White else Color.Black
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "Share",
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
                    Icon(
                        imageVector = Icons.Outlined.Block,
                        contentDescription = "Time",
                        tint = if (isDark) Color.White else Color.Black
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "Not interested",
                        modifier = Modifier.weight(1f),
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color= if (isDark) Color.White else Color.Black
                    )
                }
            }
        }
    }
}