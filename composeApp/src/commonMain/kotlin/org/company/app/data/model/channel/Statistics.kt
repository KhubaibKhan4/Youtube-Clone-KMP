package org.company.app.data.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Statistics(
    @SerialName("hiddenSubscriberCount")
    val hiddenSubscriberCount: Boolean,
    @SerialName("subscriberCount")
    val subscriberCount: String,
    @SerialName("videoCount")
    val videoCount: String,
    @SerialName("viewCount")
    val viewCount: String
)