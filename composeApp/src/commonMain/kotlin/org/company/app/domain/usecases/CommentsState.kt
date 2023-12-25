package org.company.app.domain.usecases

import org.company.app.data.model.comments.Comments

sealed class CommentsState {
    object LOADING : CommentsState()
    data class SUCCESS(val comments: Comments) : CommentsState()
    data class ERROR(val error: String) : CommentsState()
}