package org.company.app.domain.model.videos


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Youtube(
    @SerialName("etag")
    val etag: String? = null,
    @SerialName("items")
    val items: List<org.company.app.domain.model.videos.Item>?,
    @SerialName("kind")
    val kind: String? = null,
    @SerialName("nextPageToken")
    val nextPageToken: String? = null,
    @SerialName("regionCode")
    val regionCode: String? = null,
    @SerialName("pageInfo")
    val pageInfo: org.company.app.domain.model.videos.PageInfo? = null,
)