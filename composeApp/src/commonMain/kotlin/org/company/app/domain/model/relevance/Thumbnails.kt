package org.company.app.domain.model.relevance


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Thumbnails(
    @SerialName("default")
    val default: org.company.app.domain.model.relevance.Default,
    @SerialName("high")
    val high: org.company.app.domain.model.relevance.High,
    @SerialName("medium")
    val medium: org.company.app.domain.model.relevance.Medium
)