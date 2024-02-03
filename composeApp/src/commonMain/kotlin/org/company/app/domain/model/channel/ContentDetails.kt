package org.company.app.domain.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentDetails(
    @SerialName("relatedPlaylists")
    val relatedPlaylists: org.company.app.domain.model.channel.RelatedPlaylists
)