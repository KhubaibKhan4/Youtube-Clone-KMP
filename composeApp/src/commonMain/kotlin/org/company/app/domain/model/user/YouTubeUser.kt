package org.company.app.domain.model.user

data class YouTubeUser(
    val idToken: String,
    val displayName: String? = null,
    val profilePicUrl : String? = null
)
