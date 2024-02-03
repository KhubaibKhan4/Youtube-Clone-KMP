package org.company.app.domain.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Thumbnails(
    @SerialName("default")
    val default: org.company.app.domain.model.channel.Default,
    @SerialName("high")
    val high: org.company.app.domain.model.channel.High,
    @SerialName("medium")
    val medium: org.company.app.domain.model.channel.Medium
)