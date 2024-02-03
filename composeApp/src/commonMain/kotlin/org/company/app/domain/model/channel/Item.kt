package org.company.app.domain.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("brandingSettings")
    val brandingSettings: org.company.app.domain.model.channel.BrandingSettings,
    @SerialName("contentDetails")
    val contentDetails: org.company.app.domain.model.channel.ContentDetails,
    @SerialName("contentOwnerDetails")
    val contentOwnerDetails: org.company.app.domain.model.channel.ContentOwnerDetails?,
    @SerialName("etag")
    val etag: String,
    @SerialName("id")
    val id: String,
    @SerialName("kind")
    val kind: String,
    @SerialName("snippet")
    val snippet: org.company.app.domain.model.channel.Snippet,
    @SerialName("statistics")
    val statistics: org.company.app.domain.model.channel.Statistics,
    @SerialName("status")
    val status: org.company.app.domain.model.channel.Status,
    @SerialName("topicDetails")
    val topicDetails: org.company.app.domain.model.channel.TopicDetails? = null
)