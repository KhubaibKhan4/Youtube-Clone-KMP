package org.company.app.ui.components.shorts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.outlined.PlaylistAddCheck
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.ClosedCaption
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.PlaylistAddCheck
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.seiko.imageloader.rememberImagePainter
import io.github.aakira.napier.Napier
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.company.app.Notify
import org.company.app.ShareManager
import org.company.app.ShortsVideoPlayer
import org.company.app.domain.model.channel.Channel
import org.company.app.domain.model.comments.Comments
import org.company.app.domain.model.videos.Item
import org.company.app.domain.model.videos.Youtube
import org.company.app.domain.usecases.ResultState
import org.company.app.presentation.MainViewModel
import org.company.app.theme.LocalThemeIsDark
import org.company.app.ui.components.comments.CommentsList
import org.company.app.ui.components.common.ErrorBox
import org.company.app.ui.screens.channel_detail.ChannelDetail
import org.company.app.ui.screens.channel_screen.ChannelScreen
import org.company.app.ui.screens.detail.formatLikes
import org.company.app.ui.screens.detail.formatSubscribers
import org.company.app.ui.screens.detail.formatViewComments
import org.company.app.ui.screens.detail.getFormattedDateLikeMonthDay
import org.koin.compose.koinInject

@Composable
fun ShortList(youtube: Youtube) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        youtube.items?.let { videos ->
            items(videos) { items ->
                ShortItem(items)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ShortItem(
    video: Item,
    viewModel: MainViewModel = koinInject<MainViewModel>(),
) {
    var commentData by remember { mutableStateOf<Comments?>(null) }
    var channelDetail by remember { mutableStateOf<Channel?>(null) }
    var descriptionEnabled by remember { mutableStateOf(false) }
    val navigator = LocalNavigator.current
    val shortsUrl = video.id.toString()
    val channelImage = channelDetail?.items?.getOrNull(0)?.snippet?.thumbnails?.high?.url.toString()
    val image: Resource<Painter> = asyncPainterResource(data = channelImage)
    val customName = channelDetail?.items?.getOrNull(0)?.snippet?.customUrl
    var expanded by remember { mutableStateOf(false) }
    var isShareEnabled by remember { mutableStateOf(false) }
    var isCommentEnabled by remember { mutableStateOf(false) }
    var isMoreVertEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        //Channel Details
        viewModel.getVideoComments(video.id.toString(), order = "relevance")
        viewModel.getChannelDetails(video.snippet?.channelId.toString())
    }
    val commentsState by viewModel.videoComments.collectAsState()
    val state by viewModel.channelDetails.collectAsState()
    when (commentsState) {
        is ResultState.LOADING -> {
            //LoadingBox()
        }

        is ResultState.SUCCESS -> {
            val data = (commentsState as ResultState.SUCCESS).response
            commentData = data
        }

        is ResultState.ERROR -> {
            val error = (commentsState as ResultState.ERROR).error
            ErrorBox(error)
        }
    }
    when (state) {
        is ResultState.LOADING -> {
            //LoadingBox()
        }

        is ResultState.SUCCESS -> {
            val data = (state as ResultState.SUCCESS).response
            channelDetail = data

        }

        is ResultState.ERROR -> {
            val error = (state as ResultState.ERROR).error
            ErrorBox(error)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.Black)
            .padding(top = 10.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        ShortsVideoPlayer(url = shortsUrl, modifier = Modifier)
        Napier.d(message = "Video ID: ${video.id}", tag = "SHORTS")
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                /* IconButton(
                     onClick = {

                     },
                 ) {
                     Icon(
                         imageVector = Icons.Default.ArrowBack,
                         contentDescription = "Arrow Back",
                         tint = Color.White
                     )
                 }*/

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = {
                        isMoreVertEnabled = !isMoreVertEnabled
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Vert",
                        tint = Color.White
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxHeight()
                    .padding(end = 6.dp, top = 335.dp, bottom = 49.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                //Like Button
                Column(
                    modifier = Modifier.wrapContentWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.ThumbUp,
                            contentDescription = "Liked",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = formatLikes(video.statistics?.likeCount),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                //DisLike Button
                Column(
                    modifier = Modifier.wrapContentWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.ThumbDown,
                            contentDescription = "DisLiked",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Dislike",
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                //Comments Button
                Column(
                    modifier = Modifier.wrapContentWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = {
                        isCommentEnabled = !isCommentEnabled
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Comment, contentDescription = "Comments",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = formatViewComments(video.statistics?.commentCount),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                //Share Button
                Column(
                    modifier = Modifier.wrapContentWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = {
                        isShareEnabled = !isShareEnabled
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Share, contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Share",
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (isShareEnabled) {
                        ShareManager(
                            title = video.snippet?.title.toString(),
                            videoUrl = shortsUrl
                        )
                    }
                }

                //Remix Button
                Column(
                    modifier = Modifier.wrapContentWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Shuffle, contentDescription = "Remix",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Remix",
                        color = Color.White,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier.wrapContentWidth()
                        .padding(top = 12.dp, end = 4.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    KamelImage(
                        resource = image,
                        contentDescription = "Music Image",
                        modifier = Modifier.size(35.dp)
                            .clip(shape = RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 8.dp)
                    .offset(y = (-85).dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    KamelImage(
                        resource = image,
                        contentDescription = "Channel image",
                        modifier = Modifier.size(40.dp)
                            .clip(shape = CircleShape)
                            .clickable {
                                channelDetail?.items?.get(0)?.let { item ->
                                    navigator?.push(ChannelScreen(channel = item))
                                }
                            }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Row(
                        modifier = Modifier.clickable {
                            channelDetail?.items?.get(0)?.let { item ->
                                navigator?.push(ChannelScreen(channel = item))
                            }
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = customName.toString(),
                            color = Color.White
                        )
                        val isVerified = channelDetail?.items?.get(0)?.status?.isLinked
                        if (isVerified == true) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(25.dp).padding(start = 4.dp)
                            )
                        } else {
                            //  Nothing here..
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Text(
                            text = "Subscribe",
                            color = Color.White
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp)
                    .offset(y = (-85).dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    FlowRow {
                        Text(
                            modifier = Modifier.fillMaxWidth(0.65f),
                            text = "${video.snippet?.description}",
                            color = Color.White,
                            maxLines = if (expanded) Int.MAX_VALUE else 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    AnimatedVisibility((video.snippet?.description?.length ?: 0) > 100) {
                        // Show more button
                        TextButton(
                            onClick = { expanded = !expanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (expanded) "Show Less" else "Show More",
                                color = Color.White
                            )
                        }
                    }
                }
            }

        }
    }
    if (isCommentEnabled) {
        val isDark by LocalThemeIsDark.current
        if (video.statistics?.commentCount.isNullOrBlank()) {
            Notify(message = "No Comments Found...")
        }
        var commentInput by remember { mutableStateOf("") }

        ModalBottomSheet(
            onDismissRequest = {
                isCommentEnabled = false
            },
            sheetState = rememberModalBottomSheetState(),
            tonalElevation = ModalBottomSheetDefaults.Elevation,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                // Comment Title Section
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    androidx.compose.material3.Text(
                        text = "Comments",
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    androidx.compose.material3.IconButton(onClick = {
                        isCommentEnabled = false
                    }) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon"
                        )
                    }
                }
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = DividerDefaults.color)

                Spacer(modifier = Modifier.height(14.dp))

                // Top and Newest Icon Button
                val buttons = listOf(
                    "Top",
                    "Newest"
                )
                var selectedButton by remember { mutableStateOf(buttons.first()) }

                // Function to fetch comments based on the selected order
                fun fetchComments(order: String) {
                    viewModel.getVideoComments(video.id.toString(), order)
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    buttons.forEach { title ->
                        TextButton(
                            onClick = {
                                selectedButton = title
                                fetchComments(if (title == "Top") "relevance" else "time")
                            },
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(12.dp))
                                .background(
                                    color = if (selectedButton == title) Color.Black else Color.Transparent
                                )
                        ) {
                            androidx.compose.material3.Text(
                                text = title,
                                color = if (selectedButton == title) if (isDark) Color.White else Color.White else if (isDark) Color.White else Color.Black
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                    }
                }


                // Terms and conditions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .background(color = Color.LightGray),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    androidx.compose.material3.Text(
                        text = "Remember to keep comments respectful and to follow our Community Guidelines",
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth().padding(12.dp)
                    )
                }

                HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = DividerDefaults.color)

                // Comments Lists
                commentData?.let { comments ->
                    CommentsList(comments, modifier = Modifier.weight(1f))
                }

                // Own Channel Image and TextField
                Row(
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    // Own Channel Image
                    val ownChannelImage: Resource<Painter> =
                        asyncPainterResource(data = channelDetail?.items?.get(0)?.brandingSettings?.image?.bannerExternalUrl.toString())
                    KamelImage(
                        resource = ownChannelImage,
                        contentDescription = "Channel Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(25.dp)
                            .clip(CircleShape)
                            .align(alignment = Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    // TextField
                    TextField(
                        value = commentInput,
                        onValueChange = {
                            commentInput = it
                        },
                        enabled = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(
                                start = 8.dp,
                                bottom = 8.dp
                            ),
                        placeholder = {
                            androidx.compose.material3.Text(text = "Add a comment...")
                        },
                        singleLine = true,
                        maxLines = 1,
                        shape = RoundedCornerShape(12.dp),
                    )
                }

            }
        }
    }
    if (isMoreVertEnabled) {
        ModalBottomSheet(
            onDismissRequest = {
                isMoreVertEnabled = false
            },
            sheetState = rememberModalBottomSheetState(),
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.White
        ) {
            //Description
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 12.dp, end = 8.dp)
                    .clickable {
                        descriptionEnabled = !descriptionEnabled
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(imageVector = Icons.Outlined.Description, contentDescription = "Description")
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Description",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            //Save to Playlist
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 12.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.PlaylistAddCheck, contentDescription = "Save")
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Save to playlist",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            //Captions
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 12.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(imageVector = Icons.Outlined.ClosedCaption, contentDescription = "Captions")
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Captions",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            //Captions
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 12.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(imageVector = Icons.Outlined.Report, contentDescription = "Report")
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Report",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            //Send Feedback
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 12.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(imageVector = Icons.Outlined.Feedback, contentDescription = "Send Feedback")
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Send Feedback",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            }
        }
    }
    if (descriptionEnabled) {
        ModalBottomSheet(
            onDismissRequest = {
                descriptionEnabled = false
            },
            modifier = Modifier.fillMaxWidth(),
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true,
                confirmValueChange = { true }),
            shape = RoundedCornerShape(12.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Color.Black,
            tonalElevation = 8.dp,
            scrimColor = Color.Transparent,
            dragHandle = null,
            windowInsets = BottomSheetDefaults.windowInsets,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .verticalScroll(state = rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    androidx.compose.material3.Text(
                        text = "Description",
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        modifier = Modifier.fillMaxWidth().weight(1f).padding(start = 4.dp)
                    )

                    androidx.compose.material3.IconButton(onClick = {
                        descriptionEnabled = false
                    }) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(2.dp),
                    thickness = 2.dp,
                    color = DividerDefaults.color
                )

                val videoTitle = video.snippet?.title.toString()
                androidx.compose.material3.Text(
                    text = videoTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    maxLines = 2,
                    textAlign = TextAlign.Justify,
                    overflow = TextOverflow.Ellipsis,
                )


                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 10.dp, end = 2.dp, top = 4.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    // Channel Image
                    channelDetail?.items?.get(0)?.snippet?.thumbnails?.default?.url?.let {
                        rememberImagePainter(
                            it
                        )
                    }?.let {
                        Image(
                            painter = it,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp).clip(CircleShape).clickable {
                                navigator?.push(ChannelScreen(channelDetail?.items!![0]))
                            },
                            contentScale = ContentScale.FillBounds
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    channelDetail?.items?.get(0)?.snippet?.title?.let {
                        androidx.compose.material3.Text(
                            text = it,
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 1,
                            textAlign = TextAlign.Justify,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                }

                //Video Details
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 20.dp, start = 60.dp, end = 60.dp, bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        androidx.compose.material3.Text(
                            text = formatLikes(video.statistics?.likeCount),
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )

                        androidx.compose.material3.Text(
                            text = "Likes",
                            fontWeight = FontWeight.Normal,
                            fontSize = MaterialTheme.typography.labelSmall.fontSize
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        androidx.compose.material3.Text(
                            text = org.company.app.ui.screens.detail.formatViewCount(video.statistics?.viewCount),
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )

                        androidx.compose.material3.Text(
                            text = "Views",
                            fontWeight = FontWeight.Normal,
                            fontSize = MaterialTheme.typography.labelSmall.fontSize
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val (formattedMonth, day, year) = getFormattedDateLikeMonthDay(video.snippet?.publishedAt.toString())

                        androidx.compose.material3.Text(
                            text = "$formattedMonth $day",
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )

                        androidx.compose.material3.Text(
                            text = "$year",
                            fontWeight = FontWeight.Normal,
                            fontSize = MaterialTheme.typography.labelSmall.fontSize
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    thickness = 2.dp,
                    color = DividerDefaults.color
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    var desc_expanded by remember { mutableStateOf(false) }
                    val videoDescription = video.snippet?.description.toString()
                    androidx.compose.material3.Text(
                        text = videoDescription,
                        modifier = Modifier.fillMaxWidth().weight(1f)
                            .padding(top = 16.dp, start = 4.dp, end = 4.dp),
                        maxLines = if (desc_expanded) 40 else 9,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )

                    androidx.compose.material3.Text(
                        text = if (desc_expanded) "less" else "more",
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        modifier = Modifier.clickable {
                            desc_expanded = !desc_expanded
                        }.align(alignment = Alignment.Bottom)
                    )
                }


                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 8.dp,
                    color = DividerDefaults.color
                )

                channelDetail?.items?.get(0)?.snippet?.title?.let {
                    androidx.compose.material3.Text(
                        text = "More From $it",
                        fontWeight = FontWeight.Normal,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        modifier = Modifier.fillMaxWidth().padding(
                            horizontal = 16.dp, vertical = 8.dp
                        ), // Adjust padding as needed
                        maxLines = 1,
                        textAlign = TextAlign.Justify,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                // Channel Section
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Channel Image
                    channelDetail?.items?.get(0)?.snippet?.thumbnails?.default?.url?.let {
                        rememberImagePainter(
                            it
                        )
                    }?.let {
                        Image(
                            painter = it,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp).clip(CircleShape).clickable {
                                navigator?.push(ChannelScreen(channelDetail?.items!![0]))
                            },
                            contentScale = ContentScale.FillBounds
                        )
                    }

                    // Channel Info
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val channelTitle = video.snippet?.channelTitle.toString()

                        androidx.compose.material3.Text(
                            text = channelTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        androidx.compose.material3.Text(
                            text = "${formatSubscribers(channelDetail?.items?.get(0)?.statistics?.subscriberCount)} Subscribers",
                            fontSize = 14.sp
                        )

                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(
                        horizontal = 8.dp, vertical = 4.dp
                    ), // Adjust padding as needed
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedCard(
                        onClick = {},
                        shape = CardDefaults.outlinedShape,
                        enabled = true,
                        border = BorderStroke(width = 1.dp, color = Color.Black),
                        modifier = Modifier.weight(1f).padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Outlined.VideoLibrary,
                                contentDescription = "Videos",
                                modifier = Modifier.padding(8.dp)
                            )
                            androidx.compose.material3.Text(
                                "VIDEOS",
                                textAlign = TextAlign.Center,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    OutlinedCard(
                        onClick = {
                            channelDetail?.let { channel ->
                                navigator?.push(ChannelDetail(channel.items!![0]))
                            }
                        },
                        shape = CardDefaults.outlinedShape,
                        enabled = true,
                        border = BorderStroke(width = 1.dp, color = Color.Black),
                        modifier = Modifier.weight(1f).padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Outlined.AccountBox,
                                contentDescription = "About",
                                modifier = Modifier.padding(8.dp)
                            )
                            androidx.compose.material3.Text(
                                "ABOUT",
                                textAlign = TextAlign.Center,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

            }
        }
    }
}