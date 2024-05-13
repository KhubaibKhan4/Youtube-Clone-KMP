package org.company.app.domain.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Channel(
    @SerialName("etag")
    val etag: String?=null,
    @SerialName("items")
    val items: List<Item>?=null,
    @SerialName("kind")
    val kind: String?=null,
    @SerialName("pageInfo")
    val pageInfo: PageInfo?= null
)