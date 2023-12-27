package org.company.app.domain.usecases

import org.company.app.data.model.categories.VideoCategories

sealed class CategoriesState {
    object LOADING : CategoriesState()
    data class SUCCESS(val categories: VideoCategories) : CategoriesState()
    data class ERROR(val error: String) : CategoriesState()
}