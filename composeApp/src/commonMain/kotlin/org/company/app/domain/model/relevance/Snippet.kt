package org.company.app.domain.model.relevance


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Snippet(
    @SerialName("channelId")
    val channelId: String,
    @SerialName("channelTitle")
    val channelTitle: String,
    @SerialName("description")
    val description: String,
    @SerialName("liveBroadcastContent")
    val liveBroadcastContent: String,
    @SerialName("publishTime")
    val publishTime: String,
    @SerialName("publishedAt")
    val publishedAt: String,
    @SerialName("thumbnails")
    val thumbnails: org.company.app.domain.model.relevance.Thumbnails,
    @SerialName("title")
    val title: String
)