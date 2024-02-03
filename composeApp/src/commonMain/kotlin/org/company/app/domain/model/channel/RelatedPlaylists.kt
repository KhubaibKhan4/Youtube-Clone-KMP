package org.company.app.domain.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RelatedPlaylists(
    @SerialName("likes")
    val likes: String,
    @SerialName("uploads")
    val uploads: String
)