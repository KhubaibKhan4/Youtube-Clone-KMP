package org.company.app.domain.model.relevance


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Relevance(
    @SerialName("etag")
    val etag: String,
    @SerialName("items")
    val items: List<org.company.app.domain.model.relevance.Item>,
    @SerialName("kind")
    val kind: String,
    @SerialName("nextPageToken")
    val nextPageToken: String,
    @SerialName("pageInfo")
    val pageInfo: org.company.app.domain.model.relevance.PageInfo,
    @SerialName("regionCode")
    val regionCode: String
)