package org.company.app.data.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Snippet(
    @SerialName("customUrl")
    val customUrl: String,
    @SerialName("description")
    val description: String,
    @SerialName("localized")
    val localized: Localized? = null,
    @SerialName("publishedAt")
    val publishedAt: String,
    @SerialName("thumbnails")
    val thumbnails: Thumbnails,
    @SerialName("country")
    val country: String? = null,
    @SerialName("title")
    val title: String
)