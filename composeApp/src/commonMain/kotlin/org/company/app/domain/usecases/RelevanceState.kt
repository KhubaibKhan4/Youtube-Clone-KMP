package org.company.app.domain.usecases

import org.company.app.data.model.relevance.Relevance

sealed class RelevanceState {
    object LOADING : RelevanceState()
    data class SUCCESS(val relevance: Relevance) : RelevanceState()
    data class ERROR(val error: String) : RelevanceState()
}