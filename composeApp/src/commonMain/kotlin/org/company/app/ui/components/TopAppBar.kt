package org.company.app.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PlaylistAdd
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.company.app.data.model.search.Search
import org.company.app.domain.repository.Repository
import org.company.app.domain.usecases.SearchState
import org.company.app.presentation.MainViewModel
import org.company.app.theme.LocalThemeIsDark
import org.company.app.ui.screens.AccountScreen
import org.company.app.ui.screens.DetailScreen
import org.company.app.ui.screens.HomeScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class, InternalComposeApi::class)
@Composable
fun TopBar(modifier: Modifier) {
    var isDark by LocalThemeIsDark.current
    val navigator = LocalNavigator.current
    var isSearchEnabled by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var state by remember { mutableStateOf<SearchState>(SearchState.LOADING) }
    var data by remember { mutableStateOf<Search?>(null) }
    var error by remember { mutableStateOf(false) }
    var errorData by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    val repository = remember { Repository() }
    var viewModel = remember { MainViewModel(repository) }
    if (!isSearchEnabled) {
        TopAppBar(
            title = {
                Image(
                    org.jetbrains.compose.resources.painterResource(if (isDark) "youtube_logo_dark.webp" else "youtube_logo_light.webp"),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                )
            },
            actions = {
                IconButton(
                    onClick = { isDark = !isDark }
                ) {
                    Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "Notifications")
                }
                IconButton(
                    onClick = { isSearchEnabled = !isSearchEnabled }
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(
                    onClick = { navigator?.push(AccountScreen) }
                ) {
                    Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Account")
                }
            },
            modifier = modifier
        )
    } else {
        state = viewModel.search.collectAsState().value
        when (state) {
            is SearchState.LOADING -> {
                isLoading = true
            }

            is SearchState.SUCCESS -> {
                isLoading = false
                val response = (state as SearchState.SUCCESS).search
                data = response
            }

            is SearchState.ERROR -> {
                isLoading = false
                error = true
                val Error = (state as SearchState.ERROR).error
                errorData = Error
            }
        }
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(top = 49.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    isSearchEnabled = false
                }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
                }

                TextField(
                    value = query,
                    onValueChange = {
                        query = it
                    },
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            // Handle search or done action
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    enabled = true,
                    placeholder = {
                        Text("Search YouTube....")
                    }, trailingIcon = {
                        IconButton(onClick = {
                            viewModel.getSearch(query)

                        }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null)
                        }
                    }
                )

                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Mic, contentDescription = null)
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp, color = Color.LightGray
            )
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (error) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    SelectionContainer {
                        Text(errorData)
                    }
                }
            } else {
                data?.let { SearchVideosList(it) }
            }

        }
    }
}

@Composable
fun SearchVideosList(youtube: Search) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            LazyVerticalGrid(columns = GridCells.Adaptive(300.dp)) {
                youtube.items?.let { items ->
                    items(items) { videos ->
                        SearchVideoItemCard(videos)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchVideoItemCard(video: org.company.app.data.model.search.Item) {
    val navigator = LocalNavigator.current
    var moreVertEnable by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navigator?.push(DetailScreen(search = video))
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()) {
                val image: Resource<Painter> =
                    asyncPainterResource(data = video.snippet.thumbnails.high.url)
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
                    animationSpec = tween()
                )

                // Video Total Time
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.primary)
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    /*Text(
                        text = video.contentDetails?.duration?.let { formatVideoDuration(it) }
                            ?: "00:00",
                        color = Color.White,
                        fontSize = 10.sp
                    )*/
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
                    asyncPainterResource(data = video.snippet.thumbnails.high.url)
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
                        text = video.snippet.title,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
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
                        Text(text = video.snippet.channelTitle, fontSize = 10.sp)
                        Text(text = "•")
                        /* Text(
                             text = "${video.statistics?.viewCount?.let { formatViewCount(it) }} views",
                             fontSize = 10.sp
                         )*/
                        Text(text = "•")
                        Text(
                            text = getFormattedDate(video.snippet.publishedAt),
                            fontSize = 10.sp,
                            maxLines = 1,
                            modifier = Modifier
                                .widthIn(min = 0.dp)
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
                        modifier = Modifier.size(24.dp)
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
                    Icon(imageVector = Icons.Outlined.WatchLater, contentDescription = "Time")
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
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
                    Icon(imageVector = Icons.Outlined.PlaylistAdd, contentDescription = "Time")
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
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
                    Icon(imageVector = Icons.Outlined.Download, contentDescription = "Time")
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
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
                    Icon(imageVector = Icons.Outlined.Share, contentDescription = "Time")
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "Share",
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
                    Icon(imageVector = Icons.Outlined.Block, contentDescription = "Time")
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "Not interested",
                        modifier = Modifier.weight(1f),
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                }
            }
        }
    }
}