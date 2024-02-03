package org.company.app.domain.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Thumbnails(
    @SerialName("default")
    val default: org.company.app.domain.model.search.Default,
    @SerialName("high")
    val high: org.company.app.domain.model.search.High,
    @SerialName("medium")
    val medium: org.company.app.domain.model.search.Medium
)