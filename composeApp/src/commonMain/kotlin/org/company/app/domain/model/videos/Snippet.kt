package org.company.app.domain.model.videos


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
    val localized: org.company.app.domain.model.videos.Localized? = null,
    @SerialName("publishedAt")
    val publishedAt: String? = null,
    @SerialName("tags")
    val tags: List<String>?,
    @SerialName("thumbnails")
    val thumbnails: org.company.app.domain.model.videos.Thumbnails? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("position")
    val position: Int? = null,
    @SerialName("resourceId")
    val resourceId: org.company.app.domain.model.videos.ResourceId? = null,
)