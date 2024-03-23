package org.company.app.ui.components.offline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import org.company.app.theme.LocalThemeIsDark
import org.company.app.ui.components.custom_image.NetworkImage
import org.company.app.ui.components.video_list.formatVideoDuration
import org.company.app.ui.components.video_list.getFormattedDate
import sqldelight.db.YoutubeEntity

@Composable
fun OfflineList(youtubeEntity: List<YoutubeEntity>) {
    LazyVerticalGrid(columns = GridCells.Adaptive(300.dp)) {
        items(youtubeEntity) {
            OfflineVideoCard(it)
        }
    }
}

@Composable
fun OfflineVideoCard(
    youtubeEntity: YoutubeEntity,
) {
    val navigator = LocalNavigator.current
    val isDark by LocalThemeIsDark.current
    var moreVertEnable by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .pointerHoverIcon(icon = PointerIcon.Hand),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()) {

                NetworkImage(
                    modifier = Modifier.fillMaxWidth()
                        .height(200.dp)
                        .pointerHoverIcon(icon = PointerIcon.Hand),
                    url = youtubeEntity.videoThumbnail,
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop
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
                        text = youtubeEntity.duration,
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
                val channelImage = youtubeEntity.channelImage
                NetworkImage(
                    url = channelImage,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.size(40.dp)
                        .clip(CircleShape)
                        .pointerHoverIcon(icon = PointerIcon.Hand)
                )

                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = youtubeEntity.title,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        color = if (isDark) Color.White else Color.Black,
                        fontSize = 12.sp,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp
                    )

                    // Channel Name, Views, Time
                    Row(
                        modifier = Modifier.width(IntrinsicSize.Max),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = youtubeEntity.channelName,
                                fontSize = 10.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.width(IntrinsicSize.Min),
                                color = if (isDark) Color.White else Color.Black,
                            )
                            if (youtubeEntity.isVerified.toInt() == 1){
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = if (isDark) Color.White else Color.Black,
                                    modifier = Modifier.size(15.dp)
                                        .padding(start = 4.dp, top = 4.dp)
                                )
                            }
                        }
                        Text(text = "•", color = if (isDark) Color.White else Color.Black)
                        Text(
                            text = youtubeEntity.views,
                            fontSize = 10.sp,
                            color = if (isDark) Color.White else Color.Black
                        )
                        Text(text = "•", color = if (isDark) Color.White else Color.Black)
                        Text(
                            text = youtubeEntity.pubDate,
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
}