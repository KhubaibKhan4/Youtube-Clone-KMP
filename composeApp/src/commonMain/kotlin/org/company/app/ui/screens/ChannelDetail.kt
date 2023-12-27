package org.company.app.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.company.app.data.model.channel.Item

class ChannelDetail(
    private val channel: Item
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val localUri = LocalUriHandler.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
                .padding(top = 49.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        navigator?.pop()
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                    Text(
                        text = channel.snippet?.title.toString(),
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Cast, contentDescription = null)
                    }
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                //Description
                Text(
                    text = "Description",
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                val description = channel.brandingSettings.channel.description
                Text(
                    text = description,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    fontWeight = FontWeight.Normal,
                )

                Spacer(modifier = Modifier.height(16.dp))

                //Links
                Text(
                    text = "Links",
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(imageVector = Icons.Default.Facebook, contentDescription = "Facebook")
                    Spacer(modifier = Modifier.width(6.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Follow",
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        )
                        // Use regular expressions to find social links
                        val socialLinks =
                            Regex("(?i)\\b(?:twitter|instagram|facebook|linkedin|youtube)\\b[\\w/@]+")
                                .findAll(description)
                                .map { it.value }
                                .toList()

                        for (link in socialLinks) {
                            Text(
                                text = link,
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                color = Color.Blue
                            )
                            println("Social Link: $link")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                //More Info
                Text(
                    text = "More info",
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))


                // Channel Link
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 12.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.Link,
                        contentDescription = "Link Icon",
                        modifier = Modifier.size(25.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    val channelURL = "https://www/youtube.com/" + channel.snippet.customUrl
                    Text(
                        text = channelURL,
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            localUri.openUri(channelURL)
                        }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }

                // Country
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 12.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = "Country Icon",
                        modifier = Modifier.size(25.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = channel.brandingSettings.channel.country.toString())
                    Spacer(modifier = Modifier.height(6.dp))
                }

                // View Info
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 12.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp, contentDescription = "View Icon",
                        modifier = Modifier.size(25.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${channel.statistics.viewCount} views")
                    Spacer(modifier = Modifier.height(6.dp))
                }


            }
        }
    }
}
