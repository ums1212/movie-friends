package org.comon.moviefriends.api

sealed class TMDBResult<out T> {
    data object NoConstructor : TMDBResult<Nothing>()
    data object Loading : TMDBResult<Nothing>()
    data class Success<T>(val resultData: T) : TMDBResult<T>()
    data class NetworkError(val exception: Throwable) : TMDBResult<Nothing>()
}