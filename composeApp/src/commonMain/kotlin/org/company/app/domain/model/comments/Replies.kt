package org.company.app.domain.model.comments


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Replies(
    @SerialName("comments")
    val comments: List<org.company.app.domain.model.comments.Comment>
)