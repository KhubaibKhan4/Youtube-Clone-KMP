package org.company.app.data.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Search(
    @SerialName("etag")
    val etag: String? = null,
    @SerialName("items")
    val items: List<Item>? = null,
    @SerialName("kind")
    val kind: String? = null,
    @SerialName("nextPageToken")
    val nextPageToken: String? = null,
    @SerialName("pageInfo")
    val pageInfo: PageInfo? = null,
    @SerialName("regionCode")
    val regionCode: String? = null
)