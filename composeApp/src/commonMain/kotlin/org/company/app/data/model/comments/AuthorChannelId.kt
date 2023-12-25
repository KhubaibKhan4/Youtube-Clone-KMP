package org.company.app.data.model.comments


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorChannelId(
    @SerialName("value")
    val value: String
)