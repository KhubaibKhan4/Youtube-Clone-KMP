package org.company.app.domain.model.videos


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Thumbnails(
    @SerialName("default")
    val default: org.company.app.domain.model.videos.Default? = null,
    @SerialName("high")
    val high: org.company.app.domain.model.videos.High? = null,
    @SerialName("maxres")
    val maxres: org.company.app.domain.model.videos.Maxres?,
    @SerialName("medium")
    val medium: org.company.app.domain.model.videos.Medium,
    @SerialName("standard")
    val standard: org.company.app.domain.model.videos.Standard? = null
)