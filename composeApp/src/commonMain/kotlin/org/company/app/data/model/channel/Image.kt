package org.company.app.data.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    @SerialName("bannerExternalUrl")
    val bannerExternalUrl: String
)