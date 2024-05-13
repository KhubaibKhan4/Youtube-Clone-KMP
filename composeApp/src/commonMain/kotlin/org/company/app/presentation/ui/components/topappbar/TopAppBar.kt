package org.company.app.presentation.ui.components.topappbar

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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.PlaylistAdd
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Cast
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
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
import org.company.app.UserRegion
import org.company.app.domain.model.search.Search
import org.company.app.domain.model.videos.Youtube
import org.company.app.domain.usecases.ResultState
import org.company.app.presentation.ui.components.channel.channel_item.SearchChannelItem
import org.company.app.presentation.ui.components.common.ErrorBox
import org.company.app.presentation.ui.screens.account.AccountScreen
import org.company.app.presentation.ui.screens.detail.DetailScreen
import org.company.app.presentation.viewmodel.MainViewModel
import org.company.app.theme.LocalThemeIsDark
import org.company.app.utils.formatVideoDuration
import org.company.app.utils.formatViewCount
import org.company.app.utils.getFormattedDateHome
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import youtube_clone.composeapp.generated.resources.Res
import youtube_clone.composeapp.generated.resources.youtube_logo_dark
import youtube_clone.composeapp.generated.resources.youtube_logo_light
import org.company.app.domain.model.channel.Item as ChannelItem
import org.company.app.domain.model.videos.Item as YouTubeItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun TopBar(
    modifier: Modifier,
    viewModel: MainViewModel = koinInject<MainViewModel>(),
) {
    var isDark by LocalThemeIsDark.current
    val navigator = LocalNavigator.current
    var isSearchEnabled by remember { mutableStateOf(false) }
    var state by remember { mutableStateOf<ResultState<Search>>(ResultState.LOADING) }
    var data by remember { mutableStateOf<Search?>(null) }
    var error by remember { mutableStateOf(false) }
    var errorData by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    if (!isSearchEnabled) {
        TopAppBar(
            title = {
                Image(
                    painterResource(if (isDark) Res.drawable.youtube_logo_dark else Res.drawable.youtube_logo_light),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                )
            },
            actions = {
                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(imageVector = Icons.Outlined.Cast, contentDescription = "Cast Screen")
                }
                IconButton(
                    onClick = { isDark = !isDark }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications"
                    )
                }
                IconButton(
                    onClick = {
                        isSearchEnabled = !isSearchEnabled
                        query = ""
                    },
                    modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand)
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(
                    onClick = { navigator?.push(AccountScreen) },
                    modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand)
                ) {
                    Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Account")
                }
            },
            modifier = modifier
        )
    } else {
        state = viewModel.search.collectAsState().value
        when (state) {
            is ResultState.LOADING -> {
                //isLoading = true
            }

            is ResultState.SUCCESS -> {
                isLoading = false
                val response = (state as ResultState.SUCCESS).response
                data = response
            }

            is ResultState.ERROR -> {
                isLoading = false
                error = true
                val error = (state as ResultState.ERROR).error
                errorData = error
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
                    query = ""
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null
                    )
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
                            viewModel.getSearch(query, UserRegion())
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
                            viewModel.getSearch(query, UserRegion())
                        }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null)
                        }
                    }
                )
                IconButton(onClick = {
                }) {
                    Icon(imageVector = Icons.Default.Mic, contentDescription = null)
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
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
fun SearchVideoItemCard(
    video: org.company.app.domain.model.search.Item,
    viewModel: MainViewModel = koinInject<MainViewModel>(),
) {
    var channelDetails by remember {
        mutableStateOf<org.company.app.domain.model.channel.Channel?>(
            null
        )
    }
    var channel by remember { mutableStateOf<ChannelItem?>(null) }
    var singleVideo by remember { mutableStateOf<YouTubeItem?>(null) }
    var videoDetail by remember { mutableStateOf<Youtube?>(null) }
    val navigator = LocalNavigator.current
    var moreVertEnable by remember { mutableStateOf(false) }
    val isDark by LocalThemeIsDark.current
    LaunchedEffect(Unit) {
        viewModel.getChannelDetails(video.snippet.channelId)
        viewModel.getSingleVideo(video.id.videoId.toString())
    }
    val state by viewModel.channelDetails.collectAsState()
    val videoState by viewModel.singleVideo.collectAsState()
    when (state) {
        is ResultState.LOADING -> {
            // CircularProgressIndicator()
        }

        is ResultState.SUCCESS -> {
            val data = (state as ResultState.SUCCESS).response
            channelDetails = data
        }

        is ResultState.ERROR -> {
            val error = (state as ResultState.ERROR).error
            ErrorBox(error)
        }
    }
    when (videoState) {
        is ResultState.LOADING -> {
            //CircularProgressIndicator()
        }

        is ResultState.SUCCESS -> {
            val response = (videoState as ResultState.SUCCESS).response
            videoDetail = response
        }

        is ResultState.ERROR -> {
            val error = (videoState as ResultState.ERROR).error
            ErrorBox(error)
        }
    }
    channelDetails?.items?.let { items ->
        if (items.isNotEmpty()) {
            channel = items[0]
        }
    }

    videoDetail?.items?.let { items ->
        if (items.isNotEmpty()) {
            singleVideo = items[0]
        }
    }
    val image: Resource<Painter> =
        asyncPainterResource(data = video.snippet.thumbnails.high.url)


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (video.snippet.title == channel?.snippet?.title) {
            channelDetails?.items?.let { items ->
                if (items.isNotEmpty()) {
                    val channelItem = items[0]
                    SearchChannelItem(channelItem)
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        navigator?.push(DetailScreen(video = singleVideo, channelData = channel))
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column {
                    Box(modifier = Modifier.fillMaxWidth()) {

                        KamelImage(
                            resource = image,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            onLoading = {
                                CircularProgressIndicator(
                                    progress = { it },
                                )
                            },
                            onFailure = {
                                Text(text = "Failed to Load Image")
                            },
                            animationSpec = tween()
                        )

                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .background(MaterialTheme.colorScheme.primary)
                                .clip(RoundedCornerShape(4.dp))
                        ) {
                            Text(
                                text = singleVideo?.contentDetails?.duration?.let {
                                    formatVideoDuration(
                                        it
                                    )
                                }
                                    ?: "00:00",
                                color = Color.White,
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
                        val image: Resource<Painter> =
                            asyncPainterResource(data = channel?.snippet?.thumbnails?.high?.url.toString())
                        KamelImage(
                            resource = image,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.FillBounds
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = singleVideo?.snippet?.title.toString(),
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                fontSize = 12.sp,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 20.sp
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = channel?.snippet?.title.toString(),
                                        fontSize = 10.sp
                                    )
                                    val isVerified = channel?.status?.isLinked == true
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
                                Text(text = "•")
                                Text(
                                    text = "${
                                        singleVideo?.statistics?.viewCount?.let {
                                            formatViewCount(
                                                it
                                            )
                                        }
                                    } views",
                                    fontSize = 10.sp
                                )
                                Text(text = "•")
                                Text(
                                    text = "${
                                        singleVideo?.snippet?.publishedAt?.let {
                                            getFormattedDateHome(
                                                it
                                            )
                                        }
                                    }",
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    modifier = Modifier
                                        .widthIn(min = 0.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))

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
                        imageVector = Icons.AutoMirrored.Outlined.PlaylistAdd,
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
                        color = if (isDark) Color.White else Color.Black
                    )
                }
            }
        }
    }
}