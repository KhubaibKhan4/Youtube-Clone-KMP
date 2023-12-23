package org.company.app.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.company.app.data.model.channel.Item
import org.company.app.data.model.videos.Youtube
import org.company.app.domain.repository.Repository
import org.company.app.domain.usecases.YoutubeState
import org.company.app.presentation.MainViewModel
import org.company.app.theme.LocalThemeIsDark
import org.company.app.ui.components.ErrorBox
import org.company.app.ui.components.LoadingBox
import org.company.app.ui.components.PlaylistsVideosList

class ChannelScreen(
    private val channel: Item
) : Screen {
    @Composable
    override fun Content() {
        val isDark by LocalThemeIsDark.current
        val repository = remember { Repository() }
        val viewModel = remember { MainViewModel(repository) }
        var playlists by remember { mutableStateOf<Youtube?>(null) }

        LaunchedEffect(Unit) {
            viewModel.getPlaylists(channel.id)
        }
        val state by viewModel.playlists.collectAsState()
        when (state) {
            is YoutubeState.LOADING -> {
                LoadingBox()
            }

            is YoutubeState.SUCCESS -> {
                val response = (state as YoutubeState.SUCCESS).youtube
                playlists = response
            }

            is YoutubeState.ERROR -> {
                val error = (state as YoutubeState.ERROR).error
                ErrorBox(error = error)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 49.dp)
                .verticalScroll(rememberScrollState()),
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
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow Back",
                        tint = if (isDark) Color.White else Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Title
                Text(
                    text = channel.snippet?.title.toString(),
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    color = if (isDark) Color.White else Color.Black
                )

                Spacer(modifier = Modifier.weight(1f))

                // Search Icon
                IconButton(onClick = { /* Handle search button click */ }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Arrow",
                        tint = if (isDark) Color.White else Color.Black
                    )
                }

                // More Vert Icon
                IconButton(onClick = { /* Handle more vert button click */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Vert",
                        tint = if (isDark) Color.White else Color.Black
                    )
                }
            }

            // Channel Poster Image
            val poster: Resource<Painter> =
                asyncPainterResource(channel.brandingSettings?.image?.bannerExternalUrl.toString())
            KamelImage(
                resource = poster,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(170.dp),
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
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Channel Image
                val image: Resource<Painter> =
                    asyncPainterResource(data = channel.snippet?.thumbnails?.default?.url.toString())
                KamelImage(
                    resource = image,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp).clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Channel Title
                Text(
                    text = channel.snippet?.title.toString(),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = if (isDark) Color.White else Color.Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Channel Subscribers and Videos
                Text(
                    text = "${channel.snippet?.customUrl} • ${formatSubscribers(channel.statistics?.subscriberCount)} Subscribers • ${
                        formatLikes(
                            channel.statistics?.videoCount
                        )
                    } videos",
                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
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
                    channel.snippet?.localized?.description?.let {
                        Text(
                            text = it,
                            color = if (isDark) Color.White else Color.Black,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        )
                    }

                    // Arrow Icon
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = if (isDark) Color.White else Color.Black
                        )
                    }
                }
            }


            // Channel Links
            Text(
                text = "facebook.com/grandThumb?ref=book...",
                color = if (isDark) Color.White else Color.Black
            )

            // Subscribe Button
            TextButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) Color.Red else Color.Black
                )
            ) {
                Text(
                    text = "Subscribe", color = Color.White
                )
            }
            var selectedTabIndex by remember { mutableStateOf(0) }


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    edgePadding = 8.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                                .height(2.dp).background(MaterialTheme.colorScheme.primary)
                        )
                    },
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    contentColor = if (isDark) Color.White else Color.Black,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = {
                            selectedTabIndex = 0
                        },
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        Text(text = "Home")
                    }
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = {
                            selectedTabIndex = 1
                        },
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        Text(text = "Videos")
                    }
                    Tab(
                        selected = selectedTabIndex == 2,
                        onClick = {
                            selectedTabIndex = 2
                        },
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        Text(text = "Live")
                    }
                    Tab(
                        selected = selectedTabIndex == 3,
                        onClick = {
                            selectedTabIndex = 3
                        },
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        Text(text = "Playlists")
                    }
                    Tab(
                        selected = selectedTabIndex == 4,
                        onClick = {
                            selectedTabIndex = 4
                        },
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        Text(text = "Community")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                //Data of Row Tabs
                Column(
                    modifier = Modifier.height(500.dp)
                        .scrollable(
                            state = rememberScrollState(),
                            orientation = Orientation.Vertical
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    when (selectedTabIndex) {
                        0 -> {
                            playlists.let {
                                it?.let { it1 -> PlaylistsVideosList(it1) }
                            }
                            Text(text = "Hey Welcome to Home $playlists")
                        }

                        1 -> {
                            Text(text = "Hey Videos")
                        }

                        2 -> {
                            Text(text = "No Live Stream")
                        }

                        3 -> {
                            Text(text = "No Playlists")
                        }

                        4 -> {
                            Text(text = "Community Text")
                        }
                    }
                }
            }
        }

    }
}




