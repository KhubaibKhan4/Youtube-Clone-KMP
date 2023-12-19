package org.company.app.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.seiko.imageloader.rememberImagePainter
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.company.app.data.model.channel.Item
import org.company.app.theme.LocalThemeIsDark

class ChannelScreen(
    private val channel: Item
) : Screen {
    @Composable
    override fun Content() {
        val isDark by LocalThemeIsDark.current
        Column(
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val navigator = LocalNavigator.current
            // Custom Top App Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Back Icon
                IconButton(onClick = {
                    navigator?.pop()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, contentDescription = "Arrow Back",
                        tint = if (isDark) Color.White else Color.Black
                    )
                }

                // Spacer to create space between back icon and title
                Spacer(modifier = Modifier.width(16.dp))

                // Title
                Text(
                    text = channel.snippet.title,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    color = if (isDark) Color.White else Color.Black
                )

                // Spacer to create space between title and search/more vert icons
                Spacer(modifier = Modifier.weight(1f))

                // Search Icon
                IconButton(onClick = { /* Handle search button click */ }) {
                    Icon(
                        imageVector = Icons.Default.Search, contentDescription = "Search Arrow",
                        tint = if (isDark) Color.White else Color.Black
                    )
                }

                // More Vert Icon
                IconButton(onClick = { /* Handle more vert button click */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert, contentDescription = "More Vert",
                        tint = if (isDark) Color.White else Color.Black
                    )
                }
            }

            // Channel Poster Image
            val poster: Resource<Painter> =
                asyncPainterResource(channel.snippet.thumbnails.high.url)
            KamelImage(
                resource = poster,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
                    .height(170.dp),
                contentScale = ContentScale.Crop,
                onLoading = {
                    CircularProgressIndicator(it)
                },
                onFailure = {
                    Text(text = "Failed to Load Image")
                },
                animationSpec = tween()
            )

            // Channel Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Channel Image
                channel.snippet.thumbnails.default.url.let {
                    rememberImagePainter(it)
                }.let {
                    Image(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.FillBounds
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Channel Title
                Text(
                    text = channel.snippet.title,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = if (isDark) Color.White else Color.Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Channel Subscribers and Videos
                Text(
                    text = "@GarandeThumb • 3.55M Subscribers • 398 videos",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center,
                    color = if (isDark) Color.White else Color.Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Channel Details Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Channel Description
                    Text(
                        text = "Retired military guy who enjoys firearms, fitness, and humor. Enjoy!",
                        color = if (isDark) Color.White else Color.Black
                    )

                    // Arrow Icon
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint =if (isDark) Color.White else Color.Black
                        )
                    }
                }
            }


            // Channel Links
            Text(text = "facebook.com/grandThumb?ref=book...",
                color = if (isDark) Color.White else Color.Black)

            // Subscribe Button
            TextButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) Color.Red else Color.Black
                )
            ) {
                Text(
                    text = "Subscribe",
                    color = Color.White
                )
            }
        }

        // TabRow
        TabRow(
            selectedTabIndex = 0,
            containerColor = if (isDark) Color.Black else Color.White,
            contentColor = if (isDark) Color.White else Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Tab(
                selected = true,
                onClick = {},
                modifier = Modifier
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.primary),
            ) {
                Text(text = "Home")
            }
            Tab(
                selected = false,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            ) {
                Text(text = "Videos")
            }
            Tab(
                selected = false,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            ) {
                Text(text = "Live")
            }
            Tab(
                selected = false,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            ) {
                Text(text = "Playlists")
            }
            Tab(
                selected = false,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            ) {
                Text(text = "Community")
            }
        }
    }
}




