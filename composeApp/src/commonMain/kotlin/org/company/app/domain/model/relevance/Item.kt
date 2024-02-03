package org.company.app.domain.model.relevance


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("etag")
    val etag: String,
    @SerialName("id")
    val id: org.company.app.domain.model.relevance.Id,
    @SerialName("kind")
    val kind: String,
    @SerialName("snippet")
    val snippet: org.company.app.domain.model.relevance.Snippet
)