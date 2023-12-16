package org.company.app.domain.usecases

import org.company.app.data.model.search.Search
import org.company.app.data.model.videos.Youtube

sealed class SearchState {
    object LOADING : SearchState()
    data class SUCCESS(val search: Search) : SearchState()
    data class ERROR(val error: String) : SearchState()
}