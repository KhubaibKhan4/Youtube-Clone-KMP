package org.company.app.data.model.videos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResourceId(
    @SerialName("kind")
    val kind: String,
    @SerialName("videoId")
    val videoId: String
)
