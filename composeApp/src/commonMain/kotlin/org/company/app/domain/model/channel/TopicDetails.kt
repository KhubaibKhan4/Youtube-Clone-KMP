package org.company.app.domain.model.channel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicDetails(
    @SerialName("topicCategories")
    val topicCategories: List<String>,
    @SerialName("topicIds")
    val topicIds: List<String>
)