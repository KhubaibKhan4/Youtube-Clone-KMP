package org.company.app.data.model.videos


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("contentDetails")
    val contentDetails: ContentDetails? = null,
    @SerialName("etag")
    val etag: String,
    @SerialName("id")
    val id: String,
    @SerialName("kind")
    val kind: String,
    @SerialName("snippet")
    val snippet: Snippet,
    @SerialName("statistics")
    val statistics: Statistics? = null
)