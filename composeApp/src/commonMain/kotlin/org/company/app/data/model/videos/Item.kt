package org.company.app.data.model.videos


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("contentDetails")
    val contentDetails: ContentDetails? = null,
    @SerialName("etag")
    val etag: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("kind")
    val kind: String? = null,
    @SerialName("snippet")
    val snippet: Snippet? = null,
    @SerialName("statistics")
    val statistics: Statistics? = null,
    @SerialName("player")
    val player: Player? = null
)