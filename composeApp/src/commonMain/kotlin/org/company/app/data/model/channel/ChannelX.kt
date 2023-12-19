package org.company.app.data.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelX(
    @SerialName("country")
    val country: String,
    @SerialName("description")
    val description: String,
    @SerialName("keywords")
    val keywords: String? = null,
    @SerialName("title")
    val title: String,
    @SerialName("unsubscribedTrailer")
    val unsubscribedTrailer: String? = null
)