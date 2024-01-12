package org.company.app.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import cafe.adriel.voyager.navigator.LocalNavigator
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.company.app.ShareManager
import org.company.app.data.model.channel.Item
import org.company.app.theme.LocalThemeIsDark
import org.company.app.ui.screens.ChannelScreen
import org.company.app.ui.screens.formatSubscribers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchChannelItem(
    channel: Item
) {
    val navigator = LocalNavigator.current
    val isDark by LocalThemeIsDark.current
    var isMoreVert by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(10.dp)
            .clickable {
                navigator?.push(ChannelScreen(channel))
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(
            modifier = Modifier.weight(1f)
        )
        val image: Resource<Painter> =
            asyncPainterResource(data = channel.snippet.thumbnails.high.url)
        KamelImage(
            resource = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(75.dp)
                .clip(shape = CircleShape),
            onLoading = {
                CircularProgressIndicator(it)
            },
            onFailure = {
                Text(text = "Failed to Load Image")
            },
            animationSpec = tween()
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = channel.snippet.title,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val isVerified = channel.status?.isLinked == true
                if (isVerified) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = if (isDark) Color.White else Color.Black,
                        modifier = Modifier.size(15.dp)
                            .padding(start = 4.dp, top = 4.dp)
                    )
                }
            }
            Text(
                text = channel.snippet.customUrl,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = formatSubscribers(channel.statistics.subscriberCount),
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                fontWeight = FontWeight.SemiBold
            )
            TextButton(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("Subscribe")
            }
        }
        Spacer(
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = {
            isMoreVert = !isMoreVert
        }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        }
    }
    Divider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = Color.LightGray
    )
    if (isMoreVert) {
        var isShareEnabled by remember { mutableStateOf(false) }
        ModalBottomSheet(
            onDismissRequest = {
                isMoreVert = false
            },
            modifier = Modifier.fillMaxWidth()
                .padding(12.dp),
            sheetState = rememberModalBottomSheetState(),
            shape = RoundedCornerShape(4.dp),
            contentColor =if (isDark) Color.White else Color.Black,  // Adjust color as needed
            scrimColor = Color.Transparent,
            tonalElevation = 4.dp,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 12.dp)
                    .clickable {
                        isShareEnabled = !isShareEnabled
                    },
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Share",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    color = if (isDark) Color.White else Color.Black
                )
                if (isShareEnabled) {
                    ShareManager(
                        title = channel.snippet.title,
                        videoUrl = "https://youtube.com/${channel.snippet.customUrl}"
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}