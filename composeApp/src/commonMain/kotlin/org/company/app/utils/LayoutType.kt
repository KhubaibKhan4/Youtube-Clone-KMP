package org.company.app.utils

sealed interface LayoutType{
    object List: LayoutType
    data class Grid(val columns: Int): LayoutType
}
data class LayoutMeta(
    val layoutType: LayoutType,
    val favouriteEnabled: Boolean
)
data class LayoutInformation(
    val layoutMeta: LayoutMeta
)