package org.company.app.data.model.comments


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Replies(
    @SerialName("comments")
    val comments: List<Comment>
)