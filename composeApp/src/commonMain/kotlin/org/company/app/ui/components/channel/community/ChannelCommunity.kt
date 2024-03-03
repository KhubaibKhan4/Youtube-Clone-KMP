package org.company.app.ui.components.channel.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.company.app.theme.LocalThemeIsDark
import org.company.app.ui.components.video_list.getFormattedDate
import org.company.app.ui.screens.detail.formatLikes
import org.company.app.ui.screens.detail.formatViewComments

@Composable
fun ChannelCommunity(
    youtube: org.company.app.domain.model.videos.Youtube,
    channelImage: String
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        youtube.items?.let { items ->
            items(items) { videos ->
                ChannelCommunityItems(videos, channelImage)
            }
        }

    }
}

@Composable
fun ChannelCommunityItems(videos: org.company.app.domain.model.videos.Item, channelImage: String) {
    val isDark by LocalThemeIsDark.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {
            val channelPicture: Resource<Painter> = asyncPainterResource(data = channelImage)
            KamelImage(
                resource = channelPicture,
                contentDescription = "Channel Community",
                modifier = Modifier.size(50.dp)
                    .clip(shape = CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 6.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = videos.snippet?.channelTitle.toString(),
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    color = if (isDark) Color.White else Color.Black
                )

                Text(
                    text = getFormattedDate(videos.snippet?.publishedAt.toString()),
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    color = if (isDark) Color.White else Color.Black
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {},
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = if (isDark) Color.White else Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = videos.snippet?.title.toString(),
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            color =  if (isDark) Color.White else Color.Black
        )

        val postImage: Resource<Painter> =
            asyncPainterResource(data = videos.snippet?.thumbnails?.high?.url.toString())
        KamelImage(
            resource = postImage,
            contentDescription = "Poster Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = "Likes",
                modifier = Modifier.size(20.dp),
                tint = if (isDark) Color.White else Color.Gray
            )
            Text(
                text = formatLikes(videos.statistics?.likeCount.toString()),
                color =if (isDark) Color.White else Color.Gray
            )

            Icon(
                imageVector = Icons.Default.ThumbDown,
                contentDescription = "DisLikes",
                modifier = Modifier.size(20.dp),
                tint = if (isDark) Color.White else Color.Gray

            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.IosShare,
                contentDescription = "Share",
                modifier = Modifier.size(20.dp),
                tint = if (isDark) Color.White else Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Comment,
                contentDescription = "Comments",
                modifier = Modifier.size(20.dp),
                tint = if (isDark) Color.White else Color.Gray
            )
            Text(
                text = formatViewComments(videos.statistics?.commentCount),
                color = if (isDark) Color.White else Color.Gray
            )
        }
    }
    Spacer(modifier = Modifier.width(12.dp))
}

