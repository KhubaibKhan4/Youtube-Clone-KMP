package org.company.app.domain.model.videos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    @SerialName("embedHtml")
    val embedHtml: String
)
