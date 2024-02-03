package org.company.app.domain.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Search(
    @SerialName("etag")
    val etag: String? = null,
    @SerialName("items")
    val items: List<org.company.app.domain.model.search.Item>? = null,
    @SerialName("kind")
    val kind: String? = null,
    @SerialName("nextPageToken")
    val nextPageToken: String? = null,
    @SerialName("pageInfo")
    val pageInfo: org.company.app.domain.model.search.PageInfo? = null,
    @SerialName("regionCode")
    val regionCode: String? = null
)