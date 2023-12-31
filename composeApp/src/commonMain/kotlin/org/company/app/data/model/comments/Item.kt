package org.company.app.data.model.comments


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("etag")
    val etag: String,
    @SerialName("id")
    val id: String,
    @SerialName("kind")
    val kind: String,
    @SerialName("replies")
    val replies: Replies?,
    @SerialName("snippet")
    val snippet: SnippetX
)