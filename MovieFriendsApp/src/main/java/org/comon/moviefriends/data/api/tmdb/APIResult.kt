package org.comon.moviefriends.data.api.tmdb

sealed class APIResult<out T> {
    data object NoConstructor : APIResult<Nothing>()
    data object Loading : APIResult<Nothing>()
    data class Success<T>(val resultData: T) : APIResult<T>()
    data class NetworkError(val exception: Throwable) : APIResult<Nothing>()
}