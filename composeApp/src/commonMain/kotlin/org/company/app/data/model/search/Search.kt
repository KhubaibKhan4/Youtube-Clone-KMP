package org.company.app.data.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Search(
    @SerialName("etag")
    val etag: String,
    @SerialName("items")
    val items: List<Item>,
    @SerialName("kind")
    val kind: String,
    @SerialName("nextPageToken")
    val nextPageToken: String,
    @SerialName("pageInfo")
    val pageInfo: PageInfo,
    @SerialName("regionCode")
    val regionCode: String
)