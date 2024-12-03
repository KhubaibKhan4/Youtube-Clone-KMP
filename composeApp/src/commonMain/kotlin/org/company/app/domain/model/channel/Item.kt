package org.company.app.domain.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("brandingSettings")
    val brandingSettings: BrandingSettings,
    @SerialName("contentDetails")
    val contentDetails: ContentDetails,
    @SerialName("contentOwnerDetails")
    val contentOwnerDetails: ContentOwnerDetails?,
    @SerialName("etag")
    val etag: String,
    @SerialName("id")
    val id: String,
    @SerialName("kind")
    val kind: String,
    @SerialName("snippet")
    val snippet: Snippet,
    @SerialName("statistics")
    val statistics: Statistics,
    @SerialName("status")
    val status: Status,
    @SerialName("topicDetails")
    val topicDetails: TopicDetails? = null
)