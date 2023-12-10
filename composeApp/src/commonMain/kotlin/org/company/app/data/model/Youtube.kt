package org.company.app.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Youtube(
    @SerialName("etag")
    val etag: String,
    @SerialName("items")
    val items: List<Item>,
    @SerialName("kind")
    val kind: String,
    @SerialName("nextPageToken")
    val nextPageToken: String,
    @SerialName("pageInfo")
    val pageInfo: PageInfo
)