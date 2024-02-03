package org.company.app.domain.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Default(
    @SerialName("height")
    val height: Int? = null,
    @SerialName("url")
    val url: String,
    @SerialName("width")
    val width: Int? = null
)