package org.company.app.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegionRestriction(
    @SerialName("allowed")
    val allowed: List<String>?,
    @SerialName("blocked")
    val blocked: List<String>?
)