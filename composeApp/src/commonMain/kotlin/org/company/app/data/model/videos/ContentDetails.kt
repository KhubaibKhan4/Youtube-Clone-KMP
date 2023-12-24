package org.company.app.data.model.videos


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentDetails(
    @SerialName("caption")
    val caption: String? = null,
    @SerialName("contentRating")
    val contentRating: ContentRating?,
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
    val regionRestriction: RegionRestriction?
)