package org.company.app.domain.model.videos


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class High(
    @SerialName("height")
    val height: Int? = null,
    @SerialName("url")
    val url: String,
    @SerialName("width")
    val width: Int? = null
)