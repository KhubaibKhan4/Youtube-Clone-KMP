package org.company.app.data.model.relevance


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Medium(
    @SerialName("height")
    val height: Int,
    @SerialName("url")
    val url: String,
    @SerialName("width")
    val width: Int
)