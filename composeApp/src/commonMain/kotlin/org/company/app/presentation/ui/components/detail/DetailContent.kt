package org.company.app.presentation.ui.components.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.UnfoldMoreDouble
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.seiko.imageloader.rememberImagePainter
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.PlayCircle
import org.company.app.Notify
import org.company.app.ShareManager
import org.company.app.VideoPlayer
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
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
    var descriptionEnabled by remember { mutableStateOf(false) }
    var displayVideoPlayer by remember { mutableStateOf(false) }
    var isCommentLive by remember { mutableStateOf(false) }
    var isShareEnabled by remember { mutableStateOf(false) }
    var isSubscribed by remember { mutableStateOf(false) }
    var subscribedMenu by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("All") }
    val notificationMessage = remember { mutableStateOf("") }
    val navigator = LocalNavigator.current
    val isDark by LocalThemeIsDark.current

    LaunchedEffect(Unit) {
        viewModel.getVideoComments(video?.id.toString(), order = "relevance")
        viewModel.getRelevance()
    }
    stateRelevance = viewModel.relevance.collectAsState().value
    val commentsState by viewModel.videoComments.collectAsState()

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
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(state = rememberScrollState())
    ) {


        if (displayVideoPlayer) {
            VideoPlayer(
                modifier = Modifier.fillMaxWidth().height(340.dp),
                url = "https://www.youtube.com/watch?v=${video?.id}",
                thumbnail = video?.snippet?.thumbnails?.high?.url,
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
                            imageVector = FontAwesomeIcons.Solid.PlayCircle,
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
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        ) {

            Card(
                modifier = Modifier.height(40.dp).padding(4.dp).background(
                    color = Color.White, shape = RoundedCornerShape(8.dp)
                ),
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
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp)
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
                if (channelData?.snippet?.thumbnails?.high?.url?.isEmpty() == true) logo.toString() else channelData?.snippet?.thumbnails?.high?.url.toString()
            if (channelData?.snippet?.thumbnails?.high?.url.isNullOrBlank()) {
                NetworkImage(
                    url = logo.toString(),
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
                val channelTitle = video?.snippet?.channelTitle.toString()

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
                    if (channelData?.statistics?.subscriberCount.isNullOrBlank()) subscribersCount else channelData?.statistics?.subscriberCount
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
                                        isSubscribed = false
                                        notificationMessage.value = "Unsubscribed!"
                                    } else {
                                        selectedOption = option
                                        when (selectedOption) {
                                            "All" -> notificationMessage.value =
                                                "You'll get All the Notifications"

                                            "Personalized" -> notificationMessage.value =
                                                "You'll get Personalized Notifications"

                                            "None" -> notificationMessage.value =
                                                "Notifications turned off for this Channel"
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
                    text = "Comments ${formatViewComments(video?.statistics?.commentCount.toString())}",
                    fontSize = MaterialTheme.typography.labelMedium.fontSize
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.UnfoldMoreDouble,
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
                    url = channelData?.brandingSettings?.image?.bannerExternalUrl.toString(),
                    contentDescription = "Comment User Profile",
                    modifier = Modifier.size(25.dp).clip(shape = CircleShape),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.width(4.dp))
                val firstComment =
                    commentData?.items?.get(0)?.snippet?.topLevelComment?.snippet?.textOriginal.toString()
                Text(
                    text = firstComment,
                    modifier = Modifier.padding(start = 3.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                )
            }
        }


        // Horizontal Divider
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().height(2.dp),
            thickness = 1.dp,
            color = Color.LightGray
        )
        // More Videos Section (Lazy Column)
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
                        // Channel Image
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
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        var desc_expanded by remember { mutableStateOf(false) }
                        val videoDescription = video?.snippet?.description.toString()
                        Text(
                            text = videoDescription,
                            modifier = Modifier.fillMaxWidth().weight(1f)
                                .padding(top = 16.dp, start = 4.dp, end = 4.dp),
                            maxLines = if (desc_expanded) 40 else 9,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        )

                        Text(
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
                        channelData?.snippet?.thumbnails?.default?.url?.let {
                            rememberImagePainter(
                                it
                            )
                        }?.let {
                            Image(
                                painter = it,
                                contentDescription = null,
                                modifier = Modifier.size(60.dp).clip(CircleShape).clickable {
                                    navigator?.push(ChannelScreen(channelData))
                                },
                                contentScale = ContentScale.FillBounds
                            )
                        }

                        // Channel Info
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
                // Comment Title Section
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
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = DividerDefaults.color)

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

                HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = DividerDefaults.color)

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