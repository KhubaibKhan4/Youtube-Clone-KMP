package org.company.app.data.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Snippet(
    @SerialName("country")
    val country: String? = null,
    @SerialName("customUrl")
    val customUrl: String,
    @SerialName("description")
    val description: String,
    @SerialName("localized")
    val localized: Localized,
    @SerialName("publishedAt")
    val publishedAt: String,
    @SerialName("thumbnails")
    val thumbnails: Thumbnails,
    @SerialName("title")
    val title: String
)