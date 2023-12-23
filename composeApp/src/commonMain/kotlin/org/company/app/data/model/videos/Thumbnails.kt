package org.company.app.data.model.videos


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Thumbnails(
    @SerialName("default")
    val default: Default,
    @SerialName("high")
    val high: High,
    @SerialName("maxres")
    val maxres: Maxres?,
    @SerialName("medium")
    val medium: Medium,
    @SerialName("standard")
    val standard: Standard? = null
)