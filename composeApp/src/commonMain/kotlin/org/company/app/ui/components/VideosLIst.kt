package org.company.app.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
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
import com.seiko.imageloader.rememberImagePainter
import org.company.app.data.model.Item
import org.company.app.data.model.Youtube

@Composable
fun VideosList(youtube: Youtube) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            TopBar()
            LazyVerticalGrid(columns = GridCells.Adaptive(300.dp)) {
               items(youtube.items){videos ->
                   VideoItemCard(videos)
               }
            }
        }
    }
}

@Composable
fun VideoItemCard(video: Item) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column {
            Image(
                painter = rememberImagePainter(video.snippet.thumbnails.medium.url),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

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

                Spacer(modifier = Modifier.width(8.dp))

                // Video Title and Time in a Box
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = video.snippet.title,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp // Adjust the lineHeight value as needed
                    )
                    //Spacer(modifier = Modifier.height(4.dp))

                    // Channel Name, Views, Time
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = video.snippet.channelTitle, fontSize = 12.sp)
                        Text(text = "•")
                        Text(text = "248 views", fontSize = 12.sp)
                        Text(text = "•")
                        Text(
                            text = "2 days ago",
                            fontSize = 12.sp,
                            maxLines = 1,
                            modifier = Modifier
                                .widthIn(min = 0.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Vertical Three Dots Icon
                IconButton(onClick = {}){
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





