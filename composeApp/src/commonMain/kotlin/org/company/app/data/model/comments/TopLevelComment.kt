package org.company.app.data.model.comments


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopLevelComment(
    @SerialName("etag")
    val etag: String,
    @SerialName("id")
    val id: String,
    @SerialName("kind")
    val kind: String,
    @SerialName("snippet")
    val snippet: SnippetXX
)