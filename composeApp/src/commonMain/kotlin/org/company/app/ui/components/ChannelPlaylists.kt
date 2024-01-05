package org.company.app.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import org.company.app.data.model.videos.Item
import org.company.app.data.model.videos.Youtube
import org.company.app.ui.screens.DetailScreen
import kotlin.random.Random

@Composable
fun ChannelPlaylists(
    youtube: Youtube,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        modifier = Modifier.fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row {
                Icon(imageVector = Icons.Default.Sort, contentDescription = null)
                Text(
                    text = "Sort by",
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
            }
        }
        youtube.items?.let { items ->
            items(items) { videos ->
                ChannelPlaylistsItems(videos)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelPlaylistsItems(videos: Item) {
    var moreVertEnable by remember { mutableStateOf(false) }
    val navigator = LocalNavigator.current
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
                    .clickable {
                        navigator?.push(DetailScreen(videos))
                    }
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

            // Playlist count overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(
                        color = getRandomColors()
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FeaturedPlayList,
                        contentDescription = "Playlists",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = videos.contentDetails?.itemCount.toString(), // You can replace this with the actual count
                        color = Color.White
                    )
                }
            }
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
                    text = videos.snippet?.channelTitle.toString(),
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = Color.DarkGray
                )

                // More options icon
               Box(contentAlignment = Alignment.TopEnd){
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
private fun getRandomColors(): Color{
    val random = Random
    return Color(
        red = random.nextFloat(),
        green = random.nextFloat(),
        blue = random.nextFloat(),
        alpha = 0.75f
    )
}


