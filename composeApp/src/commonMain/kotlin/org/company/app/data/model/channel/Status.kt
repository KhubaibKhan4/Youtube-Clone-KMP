package org.company.app.data.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Status(
    @SerialName("isLinked")
    val isLinked: Boolean,
    @SerialName("longUploadsStatus")
    val longUploadsStatus: String,
    @SerialName("madeForKids")
    val madeForKids: Boolean? = null,
    @SerialName("privacyStatus")
    val privacyStatus: String
)