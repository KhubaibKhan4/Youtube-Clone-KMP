package org.company.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.SmsFailed
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.company.app.data.model.channel.Channel
import org.company.app.data.model.comments.Comments
import org.company.app.data.model.comments.Item
import org.company.app.domain.repository.Repository
import org.company.app.domain.usecases.ResultState
import org.company.app.presentation.MainViewModel
import org.company.app.theme.LocalThemeIsDark
import org.company.app.ui.screens.formatLikes

@Composable
fun CommentsList(
    comments: Comments,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        comments.items?.let { items ->
            items(items) { comments ->
                CommentItems(comments)
            }
        }
    }

}

@Composable
fun CommentItems(comments: Item) {
    val localNavigator = LocalNavigator.current
    val repository = remember { Repository() }
    val viewModel = remember { MainViewModel(repository) }
    var channelData by remember { mutableStateOf<Channel?>(null) }
    var commentExpanded by remember { mutableStateOf(false) }
    var repliesExpanded by remember { mutableStateOf(false) }
    val isDark by LocalThemeIsDark.current

    LaunchedEffect(Unit) {
        viewModel.getChannelDetails(comments.snippet.channelId)
    }
    val state: ResultState<Channel> by viewModel.channelDetails.collectAsState()
    when (state) {
        is ResultState.LOADING -> {
            CircularProgressIndicator()
        }

        is ResultState.SUCCESS -> {
            val data = (state as ResultState.SUCCESS).response
            channelData = data
        }

        is ResultState.ERROR -> {
            val error = (state as ResultState.ERROR).error
            ErrorBox(error)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp, end = 8.dp, start = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Channel Image
            NetworkImage(
                contentDescription = "Channel Image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                url = comments.snippet.topLevelComment.snippet.authorProfileImageUrl.toString(),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.width(12.dp))
            // Channel Username and Published Time
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                val isPinned =
                    comments.snippet.topLevelComment.snippet.channelId == "likelySpam"
                val pinned = comments.snippet.topLevelComment.snippet.authorChannelId.value.equals(comments.snippet.channelId)

                AnimatedVisibility(pinned) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = if (isPinned) Color.DarkGray else Color.Transparent)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                imageVector = Icons.Default.PinDrop,
                                contentDescription = "Pinned Icon",
                                modifier = Modifier.size(15.dp),
                                tint =if (isDark) Color.White else Color.DarkGray
                            )
                            Text(
                                text = "Pinned by ${comments.snippet.topLevelComment.snippet.authorDisplayName}",
                                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                color = if (isDark) Color.White else Color.Black
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = comments.snippet.topLevelComment.snippet.authorDisplayName,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontWeight = FontWeight.Bold,
                        color =if (isDark) Color.White else Color.Black,
                    )
                    val isVerified = channelData?.items?.get(0)?.status?.isLinked == true && channelData?.items?.get(0)?.id.equals(comments.snippet.topLevelComment.snippet.authorChannelId.value)
                    if (isVerified) {
                        androidx.compose.material.Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp).padding(start = 4.dp),
                            tint = if (isDark) Color.White else Color.Black
                        )
                    } else {
                        // Nothing Here....
                    }
                }

                Text(
                    text = getFormattedDate(comments.snippet.topLevelComment.snippet.publishedAt.toString()),
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    color =if (isDark) Color.White else Color.Gray
                )
            }

        }

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 56.dp, end = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            val commentText = comments.snippet.topLevelComment.snippet.textOriginal
            val truncatedComment = buildAnnotatedString {
                withStyle(style = SpanStyle(color = if (isDark) Color.White else Color.Black)) {
                    if (!commentExpanded && commentText.length > 100) {
                        append(commentText.take(100))
                        append("... ")
                        append("See More")
                    } else {
                        append(commentText)
                    }
                }
            }
            // Comment Text
            Text(
                text = truncatedComment,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = if (isDark) Color.White else Color.Black,
                maxLines = if (commentExpanded) Int.MAX_VALUE else 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .clickable {
                        commentExpanded = !commentExpanded
                    }
            )

            // Full Comment (AnimatedVisibility)
            AnimatedVisibility(visible = commentExpanded) {
                Text(
                    text = commentText,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = if (isDark) Color.White else Color.Black,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }

            // Like, Dislike, Comment Icons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ThumbUp,
                        contentDescription = "Thumb Up",
                        tint =if (isDark) Color.White else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = formatLikes(comments.snippet.topLevelComment.snippet.likeCount.toString()),
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                        color =if (isDark) Color.White else Color.Gray
                    )
                }
                Row(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ThumbDown,
                        contentDescription = "Thumb Down",
                        tint =if (isDark) Color.White else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Comment,
                        contentDescription = "Comment",
                        tint =if (isDark) Color.White else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            // Replies
            Text(
                text = "${comments.snippet.totalReplyCount} replies",
                color =if (isDark) Color.White else Color.Blue,
                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                modifier = Modifier.clickable {
                    //  OnClick Replies to Display Replies List
                    repliesExpanded = !repliesExpanded
                }
            )
            if (comments.snippet.totalReplyCount == 0) {
                // Can't Navigate Because Replies are zero
            } else {
                AnimatedVisibility(visible = repliesExpanded) {
                    CommentItemWithReplies(comments)
                }
            }

        }
    }
}

@Composable
fun CommentItemWithReplies(commentItem: Item) {
    val isDark by LocalThemeIsDark.current
    var commentExpanded by remember { mutableStateOf(false) }
    LazyColumn(modifier = Modifier.height(500.dp)) {

        item {
            commentItem.replies?.comments?.forEach { reply ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 8.dp, end = 8.dp, start = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Channel Image
                        val channelImage: Resource<Painter> =
                            asyncPainterResource(data = reply.snippet.authorProfileImageUrl)
                        KamelImage(
                            resource = channelImage,
                            contentDescription = "Channel Image",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            onLoading = {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(35.dp),
                                    progress = it
                                )
                            },
                            onFailure = {
                                Icon(
                                    Icons.Outlined.SmsFailed,
                                    contentDescription = "Failed to Load Images"
                                )
                            }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        // Channel Username and Published Time
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {

                            Text(
                                text = reply.snippet.authorDisplayName,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                fontWeight = FontWeight.Bold,
                                color =if (isDark) Color.White else Color.Black,
                            )
                            Text(
                                text = getFormattedDate(reply.snippet.publishedAt),
                                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                color = if (isDark) Color.White else Color.Gray
                            )
                        }

                    }

                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 56.dp, end = 8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        val commentText = reply.snippet.textOriginal
                        val truncatedComment = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = if (isDark) Color.White else Color.Black)) {
                                if (!commentExpanded && commentText.length > 100) {
                                    append(commentText.take(100))
                                    append("... ")
                                    append("See More")
                                } else {
                                    append(commentText)
                                }
                            }
                        }
                        // Comment Text
                        Text(
                            text = truncatedComment,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = if (isDark) Color.White else Color.Black,
                            maxLines = if (commentExpanded) Int.MAX_VALUE else 4,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 8.dp)
                                .clickable {
                                    commentExpanded = !commentExpanded
                                }
                        )

                        // Full Comment (AnimatedVisibility)
                        AnimatedVisibility(visible = commentExpanded) {
                            Text(
                                text = commentText,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                color = if (isDark) Color.White else Color.Black,
                                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                            )
                        }

                        // Like, Dislike, Comment Icons
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ThumbUp,
                                    contentDescription = "Thumb Up",
                                    tint =if (isDark) Color.White else Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = formatLikes(reply.snippet.likeCount.toString()),
                                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                    color =if (isDark) Color.White else Color.Gray
                                )
                            }
                            Row(
                                modifier = Modifier.padding(start = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ThumbDown,
                                    contentDescription = "Thumb Down",
                                    tint =if (isDark) Color.White else Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Comment,
                                    contentDescription = "Comment",
                                    tint =if (isDark) Color.White else Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}