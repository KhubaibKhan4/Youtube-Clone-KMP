package org.company.app.utils

sealed interface LayoutType {
    object List : LayoutType
    data class Grid(val columns: Int) : LayoutType
}

data class LayoutMeta(
    val layoutType: LayoutType,
    val favouriteEnabled: Boolean
)

data class LayoutInformation(
    val layoutMeta: LayoutMeta
)

data class Layout(
    val type: String,
    val columns: Int? = null
)

data class Meta(
    val canFavourite: Boolean,
    val mode: String
)

data class UiData(
    val layout: Map<String, Layout>,
    val meta: Meta
)