package org.company.app.data.model.videos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    @SerialName("embedHtml")
    val embedHtml: String
)
