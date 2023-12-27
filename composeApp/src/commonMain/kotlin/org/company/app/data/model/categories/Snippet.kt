package org.company.app.data.model.categories


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Snippet(
    @SerialName("assignable")
    val assignable: Boolean,
    @SerialName("channelId")
    val channelId: String,
    @SerialName("title")
    val title: String
)