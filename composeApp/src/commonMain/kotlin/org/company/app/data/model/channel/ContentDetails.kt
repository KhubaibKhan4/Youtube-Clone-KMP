package org.company.app.data.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentDetails(
    @SerialName("relatedPlaylists")
    val relatedPlaylists: RelatedPlaylists? = null
)