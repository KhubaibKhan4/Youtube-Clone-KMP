package org.company.app.domain.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Snippet(
    @SerialName("country")
    val country: String? = null,
    @SerialName("customUrl")
    val customUrl: String? = null,
    @SerialName("description")
    val description: String,
    @SerialName("localized")
    val localized: org.company.app.domain.model.channel.Localized,
    @SerialName("publishedAt")
    val publishedAt: String,
    @SerialName("thumbnails")
    val thumbnails: org.company.app.domain.model.channel.Thumbnails,
    @SerialName("title")
    val title: String
)