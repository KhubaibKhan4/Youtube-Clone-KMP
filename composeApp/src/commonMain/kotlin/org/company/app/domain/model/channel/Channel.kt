package org.company.app.domain.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Channel(
    @SerialName("etag")
    val etag: String,
    @SerialName("items")
    val items: List<org.company.app.domain.model.channel.Item>,
    @SerialName("kind")
    val kind: String,
    @SerialName("pageInfo")
    val pageInfo: org.company.app.domain.model.channel.PageInfo
)