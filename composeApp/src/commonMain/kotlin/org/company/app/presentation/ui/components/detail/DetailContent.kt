package org.company.app.presentation.ui.components.detail

import Notify
import VideoPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.launch
import org.company.app.ShareManager
import org.company.app.VideoDownloader
import org.company.app.domain.model.channel.Channel
import org.company.app.domain.model.comments.Comments
import org.company.app.domain.model.videos.Item
import org.company.app.domain.model.videos.Youtube
import org.company.app.domain.usecases.ResultState
import org.company.app.presentation.ui.components.comments.CommentsList
import org.company.app.presentation.ui.components.common.ErrorBox
import org.company.app.presentation.ui.components.custom_image.NetworkImage
import org.company.app.presentation.ui.components.relevance.RelevanceList
import org.company.app.presentation.ui.screens.channel_detail.ChannelDetail
import org.company.app.presentation.ui.screens.channel_screen.ChannelScreen
import org.company.app.presentation.ui.screens.detail.formatLikes
import org.company.app.presentation.ui.screens.detail.formatSubscribers
import org.company.app.presentation.ui.screens.detail.formatViewComments
import org.company.app.presentation.ui.screens.detail.formatViewCount
import org.company.app.presentation.ui.screens.detail.getFormattedDateLikeMonthDay
import org.company.app.presentation.viewmodel.MainViewModel
import org.company.app.theme.LocalThemeIsDark
import org.company.app.utils.Constant
import org.company.app.utils.formatVideoDuration
import org.company.app.utils.getFormattedDate
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContent(
    video: Item?,
    search: org.company.app.domain.model.search.Item?,
    channelData: org.company.app.domain.model.channel.Item?,
    logo: String?,
    subscribersCount: String,
    viewModel: MainViewModel = koinInject<MainViewModel>(),
) {
    var stateRelevance by remember { mutableStateOf<ResultState<Youtube>>(ResultState.LOADING) }
    var commentData by remember { mutableStateOf<Comments?>(null) }
    var channelDetail by remember { mutableStateOf<Channel?>(null) }
    var descriptionEnabled by remember { mutableStateOf(false) }
    var displayVideoPlayer by remember { mutableStateOf(false) }
    var isCommentLive by remember { mutableStateOf(false) }
    var isShareEnabled by remember { mutableStateOf(false) }
    var isSubscribed by remember { mutableStateOf(false) }
    var subscribedMenu by remember { mutableStateOf(false) }
    var unSubscribe by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("All") }
    val notificationMessage = remember { mutableStateOf("") }
    val navigator = LocalNavigator.current
    val isDark by LocalThemeIsDark.current

    LaunchedEffect(Unit) {
        if (search?.snippet?.channelId.toString().isNotEmpty()) {
            viewModel.getChannelDetails(search?.snippet?.channelId.toString())
        }
        viewModel.getVideoComments(video?.id.toString(), order = "relevance")
        viewModel.getRelevance()
    }
    stateRelevance = viewModel.relevance.collectAsState().value
    val commentsState by viewModel.videoComments.collectAsState()
    val channelDetails by viewModel.channelDetails.collectAsState()

    when (commentsState) {
        is ResultState.LOADING -> {
            //  ShimmerEffectSingleVideo()
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
    when (channelDetails) {
        is ResultState.LOADING -> {
            //  ShimmerEffectSingleVideo()
        }

        is ResultState.SUCCESS -> {
            val data = (channelDetails as ResultState.SUCCESS).response
            channelDetail = data
        }

        is ResultState.ERROR -> {
            val error = (channelDetails as ResultState.ERROR).error
            ErrorBox(error)
        }
    }


    val scope = rememberCoroutineScope()
    var videoUrl by remember { mutableStateOf("https://www.youtube.com/watch?v=${video?.id}") }
    val videoDownloader = remember { VideoDownloader() }
    var downloadOutput by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isDownloadedSuccessfully by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var downloadedFilePath by remember { mutableStateOf("") }

    fun downloadVideo() {
        scope.launch {
            showDialog = true
            videoDownloader.downloadVideo(videoUrl) { prog, output ->
                progress = prog
                downloadOutput = output
            }.also {
                downloadOutput = it
                isDownloadedSuccessfully = true
                showDialog = false
            }
        }
    }
    if (isDownloadedSuccessfully) {
        AlertDialog(
            onDismissRequest = { isDownloadedSuccessfully = false },
            title = {
                Text(
                    "Download Complete",
                    color = Color.Black
                )
            },
            text = {
                Column {
                    Text("File downloaded successfully:", color = Color.Black)
                    Text(
                        downloadedFilePath,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { isDownloadedSuccessfully = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Text("OK", color = Color.White)
                }
            },
            backgroundColor = Color.White,
            contentColor = Color.Black
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    "Downloading Video",
                    color = Color.Black
                )
            },
            text = {
                Column {
                    Text("Downloading video, please wait...", color = Color.Black)
                    LinearProgressIndicator(
                        progress = { progress },
                        color = Color.Blue,
                        trackColor = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Progress: ${(progress * 100).toInt()}%", color = Color.Black)
                    Text(downloadOutput, color = Color.Black)
                }
            },
            confirmButton = {},
            backgroundColor = Color.White,
            contentColor = Color.Black
        )
    }



    Column(
        modifier = Modifier.fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .verticalScroll(state = rememberScrollState())
    ) {


        if (displayVideoPlayer) {
            VideoPlayer(
                modifier = Modifier.fillMaxWidth().height(340.dp),
                url = "https://www.youtube.com/watch?v=${video?.id}"
            )
            IconButton(onClick = {
                displayVideoPlayer = false
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null
                )
            }

        } else {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(356.dp),
                contentAlignment = Alignment.Center
            ) {

                val image = video?.snippet?.thumbnails?.high?.url.toString()
                NetworkImage(
                    url = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )

                Box(
                    modifier = Modifier.width(89.dp).height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            displayVideoPlayer = true
                        },
                        modifier = Modifier
                            .pointerHoverIcon(icon = PointerIcon.Hand)
                            .align(alignment = Alignment.Center)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                }

                IconButton(
                    onClick = {
                        displayVideoPlayer = false
                        navigator?.pop()
                    },
                    modifier = Modifier.padding(top = 8.dp, start = 6.dp)
                        .align(alignment = Alignment.TopStart)
                        .pointerHoverIcon(icon = PointerIcon.Hand)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Arrow Back",
                        tint = Color.White
                    )
                }


                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.primary)
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    Text(
                        text = formatVideoDuration(video?.contentDetails?.duration.toString())
                            ?: "00:00",
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = video?.snippet?.title.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.weight(0.9f)
            )
            Icon(imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier
                    .pointerHoverIcon(icon = PointerIcon.Hand).size(24.dp).clickable {
                        descriptionEnabled = true
                    })
        }


        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val views = video?.statistics?.viewCount.toString()
            val pubDate = video?.snippet?.publishedAt.toString()

            Text(
                text = "${formatViewCount(views)} views - ${
                    getFormattedDate(
                        pubDate
                    )
                }",
                fontSize = 14.sp
            )
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().height(1.dp).padding(vertical = 8.dp),
            thickness = 1.dp,
            color = Color.LightGray
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
        ) {

            Card(
                modifier = Modifier.height(40.dp).padding(4.dp).background(
                    color = if (isDark) Color(0xFF202020) else Color.White, shape = RoundedCornerShape(8.dp)
                ),
                colors = CardColors(
                    containerColor = Color.Gray,
                    contentColor = if (isDark) Color(0xFF202020) else Color.White,
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                            .pointerHoverIcon(icon = PointerIcon.Hand).clickable { },
                        tint = if (isDark) Color.White else Color.Black
                    )

                    val videoLikes = video?.statistics?.likeCount.toString()
                    Text(
                        text = formatLikes(videoLikes),
                        fontSize = 14.sp,
                        color = if (isDark) Color.White else Color.Black
                    )

                    Spacer(modifier = Modifier.width(4.dp))


                    Box(
                        modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.White)
                    )

                    Spacer(modifier = Modifier.width(4.dp))


                    Icon(
                        imageVector = Icons.Default.ThumbDown,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                            .pointerHoverIcon(icon = PointerIcon.Hand).clickable { },
                        tint = if (isDark) Color.White else Color.Black
                    )
                }
            }


            Card(
                modifier = Modifier.height(40.dp).pointerHoverIcon(icon = PointerIcon.Hand)
                    .padding(4.dp),
                onClick = {
                    isShareEnabled = !isShareEnabled
                },
                colors = CardColors(
                    containerColor = Color.Gray,
                    contentColor = if (isDark) Color(0xFF202020) else Color.White,
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (isDark) Color.White else Color.Black
                    )

                    Text(
                        text = "Share",
                        fontSize = 14.sp,
                        color = if (isDark) Color.White else Color.Black
                    )
                }
                if (isShareEnabled) {
                    ShareManager(
                        title = video?.snippet?.title.toString(),
                        videoUrl = Constant.VIDEO_URL + video?.id
                    )
                }
            }


            Card(
                modifier = Modifier.height(40.dp).pointerHoverIcon(icon = PointerIcon.Hand)
                    .padding(4.dp),
                onClick = { },
                colors = CardColors(
                    containerColor = Color.Gray,
                    contentColor = if (isDark) Color(0xFF202020) else Color.White,
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp)
                        .clickable {
                            downloadVideo()
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (isDark) Color.White else Color.Black
                    )

                    Text(
                        text = "Download",
                        fontSize = 14.sp,
                        color = if (isDark) Color.White else Color.Black
                    )
                }
            }


            Card(
                modifier = Modifier.height(40.dp).pointerHoverIcon(icon = PointerIcon.Hand)
                    .padding(4.dp),
                onClick = { },
                colors = CardColors(
                    containerColor = Color.Gray,
                    contentColor = if (isDark) Color(0xFF202020) else Color.White,
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (isDark) Color.White else Color.Black
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(6.dp))


        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().height(2.dp),
            thickness = 1.dp,
            color = Color.LightGray
        )



        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            println("CHANNEL_DATA: $logo")
            val channelImage =
                if (channelData?.snippet?.thumbnails?.high?.url?.isEmpty() == true) if (search?.snippet?.channelId?.isNotEmpty() == true) channelDetail?.items?.first()?.snippet?.thumbnails?.high?.url.toString() else logo.toString() else channelData?.snippet?.thumbnails?.high?.url.toString()
            if (channelData?.snippet?.thumbnails?.high?.url.isNullOrBlank()) {
                NetworkImage(
                    url = if (search?.snippet?.channelId?.isNotEmpty() == true) channelDetail?.items?.first()?.snippet?.thumbnails?.high?.url.toString() else logo.toString(),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp).clip(CircleShape)
                        .pointerHoverIcon(icon = PointerIcon.Hand).clickable {
                            channelData?.let { channelItem ->
                                navigator?.push(ChannelScreen(channelItem))
                            }
                        },
                    contentScale = ContentScale.FillBounds
                )
            } else {
                NetworkImage(
                    url = channelImage,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp).clip(CircleShape)
                        .pointerHoverIcon(icon = PointerIcon.Hand).clickable {
                            channelData?.let { channelItem ->
                                navigator?.push(ChannelScreen(channelItem))
                            }
                        },
                    contentScale = ContentScale.FillBounds
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement
                    .spacedBy(4.dp)
            ) {
                val channelTitle =
                    if (search?.snippet?.channelTitle?.isEmpty() == true) channelDetail?.items?.first()?.snippet?.title.toString() else video?.snippet?.channelTitle.toString()

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = channelTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isSubscribed) 12.sp else MaterialTheme.typography.titleSmall.fontSize

                    )
                    val isVerified = channelData?.status?.isLinked
                    if (isVerified == true) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 4.dp)
                                .size(if (isSubscribed) 15.dp else 25.dp),
                            tint = if (isDark) Color.White else Color.Black
                        )
                    }
                }
                val subscribers =
                    if (channelData?.statistics?.subscriberCount.isNullOrBlank()) if (subscribersCount.isEmpty()) channelDetail?.items?.first()?.statistics?.subscriberCount.toString() else subscribersCount else channelData?.statistics?.subscriberCount
                Text(
                    text = "${formatSubscribers(subscribers)} Subscribers",
                    fontSize = 14.sp
                )

            }

            Column(
                modifier = Modifier.wrapContentWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Row {
                    AnimatedVisibility(!isSubscribed) {
                        FilledTonalButton(
                            onClick = {
                                subscribedMenu = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "SUBSCRIBE",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .pointerHoverIcon(icon = PointerIcon.Hand)
                                    .clickable {
                                        isSubscribed = true
                                        notificationMessage.value = "Subscription Added"
                                    }
                            )
                        }
                    }

                    AnimatedVisibility(isSubscribed) {
                        FilledTonalButton(
                            onClick = {
                                subscribedMenu = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = when (selectedOption) {
                                    "All" -> Icons.Filled.NotificationsActive
                                    "Personalized" -> Icons.Outlined.Notifications
                                    "None" -> Icons.Outlined.NotificationsOff
                                    "Unsubscribe" -> Icons.Outlined.PersonOutline
                                    else -> Icons.Filled.NotificationsActive
                                },
                                contentDescription = null,
                                modifier = Modifier.size(25.dp),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Subscribed",
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                AnimatedVisibility(subscribedMenu) {
                    DropdownMenu(
                        expanded = subscribedMenu,
                        onDismissRequest = {
                            subscribedMenu = false
                        }
                    ) {
                        listOf("All", "Personalized", "None", "Unsubscribe").forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    if (option == "Unsubscribe") {
                                        unSubscribe = true
                                    } else {
                                        selectedOption = option
                                        when (selectedOption) {
                                            "All" -> notificationMessage.value =
                                                "You'll get All the Notifications"

                                            "Personalized" -> notificationMessage.value =
                                                "You'll get Personalized Notifications"
                                        }
                                    }
                                    subscribedMenu = false
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = when (option) {
                                            "All" -> Icons.Filled.NotificationsActive
                                            "Personalized" -> Icons.Outlined.Notifications
                                            "None" -> Icons.Outlined.NotificationsOff
                                            "Unsubscribe" -> Icons.Outlined.PersonOutline
                                            else -> Icons.Filled.NotificationsActive
                                        },
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
        if (unSubscribe) {
            AlertDialog(
                onDismissRequest = {
                    unSubscribe = false
                },
                text = {
                    Text("Unsubscribe from ${channelData?.snippet?.title}")
                },
                confirmButton = {
                    TextButton(onClick = {
                        isSubscribed = false
                        unSubscribe = false
                        notificationMessage.value = "Subscription Removed"
                    }) {
                        Text("Unsubscribe")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        unSubscribe = false
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
        if (notificationMessage.value.isNotBlank()) {
            Notify(notificationMessage.value)
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().height(2.dp),
            thickness = 1.dp,
            color = Color.LightGray
        )



        Column(
            modifier = Modifier.fillMaxWidth()
                .pointerHoverIcon(icon = PointerIcon.Hand)
                .clickable {
                    isCommentLive = !isCommentLive
                }
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Comments ${formatViewComments(video?.statistics?.commentCount.toString()) ?: 0.0}",
                    fontSize = MaterialTheme.typography.labelMedium.fontSize
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "More Comments Icon"
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                NetworkImage(
                    url = commentData?.items?.first()?.snippet?.topLevelComment?.snippet?.authorProfileImageUrl
                        ?: channelData?.snippet?.thumbnails?.high?.url.toString(),
                    contentDescription = "Comment User Profile",
                    modifier = Modifier.size(25.dp).clip(shape = CircleShape),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.width(4.dp))
                val firstComment =
                    commentData?.items?.get(0)?.snippet?.topLevelComment?.snippet?.textOriginal
                        ?: "No Comments Available"
                Text(
                    text = firstComment,
                    modifier = Modifier.padding(start = 3.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                )
            }
        }


        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().height(2.dp),
            thickness = 1.dp,
            color = Color.LightGray
        )
        Text(
            text = "More Videos",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
        RelevanceList(stateRelevance)

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
                        Text(
                            text = "Description",
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            modifier = Modifier.fillMaxWidth().weight(1f).padding(start = 4.dp)
                        )

                        IconButton(onClick = {
                            descriptionEnabled = false
                        }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null)
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth().padding(2.dp),
                        thickness = 2.dp,
                        color = DividerDefaults.color
                    )

                    val videoTitle = video?.snippet?.title.toString()
                    Text(
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
                        NetworkImage(
                            modifier = Modifier
                                .size(15.dp)
                                .clip(CircleShape)
                                .clickable {
                                    navigator?.push(ChannelScreen(channelData!!))
                                },
                            url = channelData?.snippet?.thumbnails?.default?.url.toString(),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        channelData?.snippet?.title?.let {
                            Text(
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
                            Text(
                                text = formatLikes(video?.statistics?.likeCount),
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )

                            Text(
                                text = "Likes",
                                fontWeight = FontWeight.Normal,
                                fontSize = MaterialTheme.typography.labelSmall.fontSize
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = formatViewCount(video?.statistics?.viewCount),
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )

                            Text(
                                text = "Views",
                                fontWeight = FontWeight.Normal,
                                fontSize = MaterialTheme.typography.labelSmall.fontSize
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val (formattedMonth, day, year) = getFormattedDateLikeMonthDay(video?.snippet?.publishedAt.toString())

                            Text(
                                text = "$formattedMonth $day",
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )

                            Text(
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
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var descExpanded by remember { mutableStateOf(false) }
                        val videoDescription = video?.snippet?.description ?: ""
                        val showMoreThreshold = 150
                        val displayDescription =
                            if (descExpanded) videoDescription else videoDescription.take(
                                showMoreThreshold
                            )

                        val descriptionText = buildAnnotatedString {
                            withStyle(style = ParagraphStyle(textAlign = TextAlign.Start)) {
                                append(displayDescription)
                            }
                            if (videoDescription.length > showMoreThreshold) {
                                withStyle(style = SpanStyle(fontSize = 14.sp, color = Color.Gray)) {
                                    append(if (descExpanded) " show less" else "... show more")
                                }
                            }
                        }

                        Text(
                            text = descriptionText,
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize(
                                    tween(
                                        durationMillis = 1000,
                                        delayMillis = 500,
                                        easing = FastOutLinearInEasing
                                    )
                                )
                                .padding(top = 16.dp, start = 4.dp, end = 4.dp)
                                .clickable { descExpanded = !descExpanded },
                            maxLines = if (descExpanded) Int.MAX_VALUE else 9,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        )
                    }


                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 8.dp,
                        color = DividerDefaults.color
                    )

                    channelData?.snippet?.title?.let {
                        Text(
                            text = "More From $it",
                            fontWeight = FontWeight.Normal,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            modifier = Modifier.fillMaxWidth().padding(
                                horizontal = 16.dp, vertical = 8.dp
                            ),
                            maxLines = 1,
                            textAlign = TextAlign.Justify,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NetworkImage(
                            modifier = Modifier.size(60.dp).clip(CircleShape).clickable {
                                channelData?.let {
                                    navigator?.push(ChannelScreen(channelData))
                                }
                            },
                            contentDescription = "Channel Logo",
                            contentScale = ContentScale.FillBounds,
                            url = channelData?.snippet?.thumbnails?.default?.url.toString()
                        )

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            val channelTitle = video?.snippet?.channelTitle.toString()

                            Text(
                                text = channelTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "${formatSubscribers(channelData?.statistics?.subscriberCount)} Subscribers",
                                fontSize = 14.sp
                            )

                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(
                            horizontal = 8.dp, vertical = 4.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedCard(
                            onClick = {
                                channelData?.let { item ->
                                    navigator?.push(ChannelScreen(item))
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
                                Icon(
                                    imageVector = Icons.Outlined.VideoLibrary,
                                    contentDescription = "Videos",
                                    modifier = Modifier.padding(8.dp)
                                )
                                Text(
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
                                channelData?.let { channel ->
                                    navigator?.push(ChannelDetail(channel))
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
                                Icon(
                                    imageVector = Icons.Outlined.AccountBox,
                                    contentDescription = "About",
                                    modifier = Modifier.padding(8.dp)
                                )
                                Text(
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
    if (isCommentLive) {
        if (video?.statistics?.commentCount.isNullOrBlank()) {
           Notify(message = "No Comments Found...")
        }
        var commentInput by remember { mutableStateOf("") }

        if (commentData?.items?.first()?.snippet?.topLevelComment?.snippet?.textOriginal?.isNotEmpty() == true) {
            ModalBottomSheet(
                onDismissRequest = {
                    isCommentLive = false
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
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Comments",
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            isCommentLive = false
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Icon"
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = DividerDefaults.color
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val buttons = listOf(
                        "Top",
                        "Newest"
                    )
                    var selectedButton by remember { mutableStateOf(buttons.first()) }

                    fun fetchComments(order: String) {
                        viewModel.getVideoComments(video?.id.toString(), order)
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
                                Text(
                                    text = title,
                                    color = if (selectedButton == title) Color.White else Color.Black
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                        }
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .background(color = Color.LightGray),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Remember to keep comments respectful and to follow our Community Guidelines",
                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth().padding(12.dp)
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = DividerDefaults.color
                    )

                    commentData?.let { comments ->
                        CommentsList(comments, modifier = Modifier.weight(1f))
                    }

                    Row(
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        NetworkImage(
                            modifier = Modifier.size(25.dp)
                                .clip(CircleShape)
                                .align(alignment = Alignment.CenterVertically),
                            contentDescription = "Channel Image",
                            contentScale = ContentScale.Crop,
                            url = channelData?.brandingSettings?.image?.bannerExternalUrl.toString(),
                        )
                        Spacer(modifier = Modifier.width(8.dp))

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
                                Text(text = "Add a comment...")
                            },
                            singleLine = true,
                            maxLines = 1,
                            shape = RoundedCornerShape(12.dp),
                        )
                    }

                }
            }
        }
    }
}