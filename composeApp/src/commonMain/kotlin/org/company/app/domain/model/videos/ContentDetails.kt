package org.company.app.domain.model.videos


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentDetails(
    @SerialName("caption")
    val caption: String? = null,
    @SerialName("contentRating")
    val contentRating: org.company.app.domain.model.videos.ContentRating?,
    @SerialName("definition")
    val definition: String? = null,
    @SerialName("dimension")
    val dimension: String? = null,
    @SerialName("duration")
    val duration: String? = null,
    @SerialName("licensedContent")
    val licensedContent: Boolean? = null,
    @SerialName("projection")
    val projection: String? = null,
    @SerialName("itemCount")
    val itemCount: Int? = null,
    @SerialName("regionRestriction")
    val regionRestriction: org.company.app.domain.model.videos.RegionRestriction?
)