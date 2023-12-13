package org.company.app.domain.usecases

import org.company.app.data.model.Youtube

sealed class YoutubeState {
    object LOADING : YoutubeState()
    data class SUCCESS(val youtube: Youtube) : YoutubeState()
    data class ERROR(val error: String) : YoutubeState()
}