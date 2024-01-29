package org.company.app.domain.usecases

sealed class ResultState<out T> {
    object LOADING: ResultState<Nothing>()
    data class SUCCESS<T>(val response: T) : ResultState<T>()
    data class ERROR(val error: String): ResultState<Nothing>()
}