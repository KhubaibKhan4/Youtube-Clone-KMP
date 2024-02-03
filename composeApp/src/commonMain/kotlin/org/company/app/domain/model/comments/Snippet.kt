package org.company.app.domain.model.comments


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Snippet(
    @SerialName("authorChannelId")
    val authorChannelId: org.company.app.domain.model.comments.AuthorChannelId,
    @SerialName("authorChannelUrl")
    val authorChannelUrl: String,
    @SerialName("authorDisplayName")
    val authorDisplayName: String,
    @SerialName("authorProfileImageUrl")
    val authorProfileImageUrl: String,
    @SerialName("canRate")
    val canRate: Boolean,
    @SerialName("channelId")
    val channelId: String,
    @SerialName("likeCount")
    val likeCount: Int,
    @SerialName("parentId")
    val parentId: String,
    @SerialName("publishedAt")
    val publishedAt: String,
    @SerialName("textDisplay")
    val textDisplay: String,
    @SerialName("textOriginal")
    val textOriginal: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("videoId")
    val videoId: String,
    @SerialName("viewerRating")
    val viewerRating: String
)