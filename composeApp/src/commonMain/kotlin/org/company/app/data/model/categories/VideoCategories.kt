package org.company.app.data.model.categories


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoCategories(
    @SerialName("etag")
    val etag: String? = null,
    @SerialName("items")
    val items: List<Item>? = null,
    @SerialName("kind")
    val kind: String? = null
)