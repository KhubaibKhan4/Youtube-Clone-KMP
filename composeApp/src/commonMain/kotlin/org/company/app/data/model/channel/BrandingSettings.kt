package org.company.app.data.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BrandingSettings(
    @SerialName("channel")
    val channel: ChannelX,
    @SerialName("image")
    val image: Image
)