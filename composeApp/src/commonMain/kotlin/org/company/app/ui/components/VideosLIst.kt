package org.company.app.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalDrawer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Audiotrack
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Games
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.PlaylistAdd
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Sports
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.company.app.data.model.categories.Snippet
import org.company.app.data.model.categories.VideoCategories
import org.company.app.data.model.search.Search
import org.company.app.data.model.videos.Item
import org.company.app.data.model.videos.Youtube
import org.company.app.domain.repository.Repository
import org.company.app.domain.usecases.CategoriesState
import org.company.app.domain.usecases.SearchState
import org.company.app.presentation.MainViewModel
import org.company.app.theme.LocalThemeIsDark
import org.company.app.ui.screens.DetailScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun VideosList(youtube: Youtube) {
    val repository = remember { Repository() }
    val viewModel = remember { MainViewModel(repository) }
    var videoCategories by remember { mutableStateOf<VideoCategories?>(null) }
    var videosByCategories by remember { mutableStateOf<Search?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var isAnyCategorySelected by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getVideoCategories()
    }
    if (isAnyCategorySelected) {
        LaunchedEffect(selectedCategory) {
            selectedCategory?.let { category ->
                viewModel.getSearch(category)
            }
        }
    }

    // Get Video Categories
    val state by viewModel.videoCategories.collectAsState()
    val categoriesVideos by viewModel.search.collectAsState()
    when (state) {
        is CategoriesState.LOADING -> {
            LoadingBox()
        }

        is CategoriesState.SUCCESS -> {
            val data = (state as CategoriesState.SUCCESS).categories
            videoCategories = data
        }

        is CategoriesState.ERROR -> {
            val error = (state as CategoriesState.ERROR).error
            ErrorBox(error)
        }
    }


    when (categoriesVideos) {
        is SearchState.LOADING -> {
            if (isAnyCategorySelected) {
                LoadingBox()
            }
        }

        is SearchState.SUCCESS -> {
            val data = (categoriesVideos as SearchState.SUCCESS).search
            videosByCategories = data
        }

        is SearchState.ERROR -> {
            val error = (categoriesVideos as SearchState.ERROR).error
            ErrorBox(error)
        }
    }

    val windowSizeClass = calculateWindowSizeClass()
    val showRails = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    val drawerState =
        androidx.compose.material.rememberDrawerState(initialValue = androidx.compose.material.DrawerValue.Closed)

    val isDark = LocalThemeIsDark.current.value
    if (showRails) {
        coroutineScope.launch {
            drawerState.close()
        }
    }


    ModalDrawer(
        drawerBackgroundColor = MaterialTheme.colorScheme.surface,
        drawerContentColor = if (isDark) Color.White else Color.Black,
        drawerState = drawerState,
        gesturesEnabled = true,
        modifier = Modifier.fillMaxHeight().wrapContentWidth(),
        drawerContent = {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 6.dp, top = 8.dp, end = 6.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    painterResource(if (isDark) "youtube_logo_dark.webp" else "youtube_logo_light.webp"),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                )

                Spacer(modifier = Modifier.height(12.dp))
                NavigationDrawerItem(
                    label = {
                        Text(text = "Trending")
                    },
                    onClick = {
                        coroutineScope.launch {
                            selectedCategory = "Trending"
                            isAnyCategorySelected = true
                            drawerState.close()
                        }
                    },
                    selected = false,
                    icon = {
                        Icon(
                            painterResource("trending.png"),
                            contentDescription = "Trending",
                            modifier = Modifier.size(25.dp),
                            tint = if (isDark) Color.White else Color.Black
                        )
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))
                NavigationDrawerItem(
                    label = {
                        Text(text = "Music")
                    },
                    onClick = {
                        coroutineScope.launch {
                            selectedCategory = "Music"
                            isAnyCategorySelected = true
                            drawerState.close()
                        }
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Audiotrack,
                            contentDescription = "Music"
                        )
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))
                NavigationDrawerItem(
                    label = {
                        Text(text = "Live")
                    },
                    onClick = {
                        coroutineScope.launch {
                            selectedCategory = "Live"
                            isAnyCategorySelected = true
                            drawerState.close()
                        }
                    },
                    selected = false,
                    icon = {
                        Icon(
                            painterResource("livestream_icon.png"),
                            contentDescription = "Live",
                            modifier = Modifier.size(25.dp),
                            tint = if (isDark) Color.White else Color.Black
                        )
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))
                NavigationDrawerItem(
                    label = {
                        Text(text = "Gaming")
                    },
                    onClick = {
                        coroutineScope.launch {
                            selectedCategory = "Gaming"
                            isAnyCategorySelected = true
                            drawerState.close()
                        }
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Games,
                            contentDescription = "Gaming"
                        )
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))
                NavigationDrawerItem(
                    label = {
                        Text(text = "News")
                    },
                    onClick = {
                        coroutineScope.launch {
                            selectedCategory = "News"
                            isAnyCategorySelected = true
                            drawerState.close()
                        }
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Newspaper,
                            contentDescription = "News"
                        )
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))
                NavigationDrawerItem(
                    label = {
                        Text(text = "Sports")
                    },
                    onClick = {
                        coroutineScope.launch {
                            selectedCategory = "Sports"
                            isAnyCategorySelected = true
                            drawerState.close()
                        }
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Sports,
                            contentDescription = "Sports"
                        )
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))
                NavigationDrawerItem(
                    label = {
                        Text(text = "Learning")
                    },
                    onClick = {
                        coroutineScope.launch {
                            selectedCategory = "Learning"
                            isAnyCategorySelected = true
                            drawerState.close()
                        }
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Lightbulb,
                            contentDescription = "Learning"
                        )
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))
                NavigationDrawerItem(
                    label = {
                        Text(text = "Fashion & Beauty")
                    },
                    onClick = {
                        coroutineScope.launch {
                            selectedCategory = "Fashion & Beauty"
                            isAnyCategorySelected = true
                            drawerState.close()
                        }
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingBag,
                            contentDescription = "Fashion & Beauty"
                        )
                    }
                )
            }
        }) {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            Column {
                TopBar(modifier = Modifier.fillMaxWidth())

                // Buttons section (Compass icon and "All" button)
                Row(
                    modifier = Modifier
                        .horizontalScroll(state = rememberScrollState())
                        .width(1500.dp)
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Compass icon button
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        },
                        modifier = Modifier.size(48.dp).clip(shape = RoundedCornerShape(6.dp)),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.LightGray.copy(alpha = 0.55f)
                        ),
                    ) {
                        Icon(
                            painter = painterResource("compass_icon.xml"),
                            contentDescription = "Compass Icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // "All" button
                    CategoryButton(
                        category = org.company.app.data.model.categories.Item(
                            etag = "",
                            id = "all",
                            kind = "",
                            Snippet(assignable = true, title = "All", channelId = "")
                        ),
                        isSelected = selectedCategory == "all",
                        onCategorySelected = {
                            selectedCategory = "all"
                            isAnyCategorySelected = true
                        }
                    )
                    // Scrollable row of category buttons
                    videoCategories?.let { categories ->
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(categories.items ?: emptyList()) { category ->
                                CategoryButton(
                                    category = category,
                                    isSelected = category.id == selectedCategory,
                                    onCategorySelected = {
                                        selectedCategory = category.id
                                        isAnyCategorySelected = true
                                    }
                                )
                            }
                        }
                    }

                }

                if (!isAnyCategorySelected) {
                    // LazyVerticalGrid of videos
                    LazyVerticalGrid(columns = GridCells.Adaptive(300.dp)) {
                        youtube.items?.let { items ->
                            items(items) { videos ->
                                VideoItemCard(videos)
                            }
                        }
                    }

                } else {
                    // LazyVerticalGrid of videos
                    LazyVerticalGrid(columns = GridCells.Adaptive(300.dp)) {
                        videosByCategories?.items?.let { items ->
                            items(items) { videos ->
                                SearchVideoItemCard(videos)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CategoryButton(
    category: org.company.app.data.model.categories.Item,
    isSelected: Boolean,
    onCategorySelected: () -> Unit
) {
    Button(
        onClick = onCategorySelected,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.Gray else Color.LightGray.copy(alpha = 0.55f),
            contentColor = Color.Black,
        ),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = category.snippet.title.orEmpty(),
            fontSize = MaterialTheme.typography.bodySmall.fontSize
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoItemCard(video: Item) {
    val navigator = LocalNavigator.current
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
                        Text(text = video.snippet.channelTitle.toString(), fontSize = 10.sp)
                        Text(text = "•")
                        Text(
                            text = "${video.statistics?.viewCount?.let { formatViewCount(it) }} views",
                            fontSize = 10.sp
                        )
                        Text(text = "•")
                        Text(
                            text = getFormattedDate(video.snippet.publishedAt.toString()),
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
            sheetState = androidx.compose.material3.rememberModalBottomSheetState(),
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

fun formatVideoDuration(duration: String?): String {
    if (duration == null) return "00:00"

    val regex = Regex("PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?")
    val matchResult = regex.find(duration)

    val hours = matchResult?.groups?.get(1)?.value?.toIntOrNull() ?: 0
    val minutes = matchResult?.groups?.get(2)?.value?.toIntOrNull() ?: 0
    val seconds = matchResult?.groups?.get(3)?.value?.toIntOrNull() ?: 0

    return buildString {
        if (hours > 0) {
            append("${if (hours < 10) "0" else ""}$hours:")
        }
        append("${if (minutes < 10) "0" else ""}$minutes:")
        append("${if (seconds < 10) "0" else ""}$seconds")
    }
}


fun getFormattedDate(publishedAt: String): String {
    return try {
        val instant = Instant.parse(publishedAt)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val currentInstant = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())

        val seconds = currentInstant.epochSeconds - instant.epochSeconds
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        when {
            seconds < 60 -> "$seconds seconds ago"
            minutes < 60 -> "$minutes minutes ago"
            hours < 24 -> "$hours hours ago"
            else -> "$days days ago"
        }
    } catch (e: Throwable) {
        "Unknown date"
    }
}

@Composable
fun formatViewCount(viewCount: String): String {
    val count = viewCount.toLong()

    return when {
        count < 1_000 -> "$count"
        count < 1_000_000 -> "${count / 1_000}k"
        count < 1_000_000_000 -> "${count / 1_000_000}M"
        else -> "${count / 1_000_000_000}B"
    }
}



