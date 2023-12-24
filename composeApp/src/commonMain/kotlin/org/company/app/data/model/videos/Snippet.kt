package org.company.app.data.model.videos


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Snippet(
    @SerialName("categoryId")
    val categoryId: String? = null,
    @SerialName("channelId")
    val channelId: String,
    @SerialName("channelTitle")
    val channelTitle: String? = null,
    @SerialName("defaultAudioLanguage")
    val defaultAudioLanguage: String?,
    @SerialName("defaultLanguage")
    val defaultLanguage: String?,
    @SerialName("description")
    val description: String? = null,
    @SerialName("liveBroadcastContent")
    val liveBroadcastContent: String? = null,
    @SerialName("localized")
    val localized: Localized? = null,
    @SerialName("publishedAt")
    val publishedAt: String? = null,
    @SerialName("tags")
    val tags: List<String>?,
    @SerialName("thumbnails")
    val thumbnails: Thumbnails? = null,
    @SerialName("title")
    val title: String? = null
)