package org.company.app.domain.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelX(
    @SerialName("country")
    val country: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("keywords")
    val keywords: String? = null,
    @SerialName("title")
    val title: String,
    @SerialName("trackingAnalyticsAccountId")
    val trackingAnalyticsAccountId: String? = null,
    @SerialName("unsubscribedTrailer")
    val unsubscribedTrailer: String? = null
)