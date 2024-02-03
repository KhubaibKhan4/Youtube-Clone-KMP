package org.company.app.domain.model.comments


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SnippetX(
    @SerialName("canReply")
    val canReply: Boolean,
    @SerialName("channelId")
    val channelId: String,
    @SerialName("isPublic")
    val isPublic: Boolean,
    @SerialName("topLevelComment")
    val topLevelComment: org.company.app.domain.model.comments.TopLevelComment,
    @SerialName("totalReplyCount")
    val totalReplyCount: Int,
    @SerialName("videoId")
    val videoId: String
)