package org.company.app.data.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("brandingSettings")
    val brandingSettings: BrandingSettings? = null,
    @SerialName("contentDetails")
    val contentDetails: ContentDetails? = null,
    @SerialName("etag")
    val etag: String,
    @SerialName("id")
    val id: String,
    @SerialName("kind")
    val kind: String,
    @SerialName("snippet")
    val snippet: Snippet? = null,
    @SerialName("statistics")
    val statistics: Statistics? = null
)