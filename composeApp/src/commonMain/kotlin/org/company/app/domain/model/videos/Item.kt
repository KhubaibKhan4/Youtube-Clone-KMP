package org.company.app.domain.model.videos


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("contentDetails")
    val contentDetails: org.company.app.domain.model.videos.ContentDetails? = null,
    @SerialName("etag")
    val etag: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("kind")
    val kind: String? = null,
    @SerialName("snippet")
    val snippet: org.company.app.domain.model.videos.Snippet? = null,
    @SerialName("statistics")
    val statistics: org.company.app.domain.model.videos.Statistics? = null,
    @SerialName("player")
    val player: org.company.app.domain.model.videos.Player? = null
)