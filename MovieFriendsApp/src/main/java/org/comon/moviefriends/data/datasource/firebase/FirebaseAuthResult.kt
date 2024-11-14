package org.comon.moviefriends.data.datasource.firebase

sealed class FirebaseAuthResult<out T> {
    data object Loading : FirebaseAuthResult<Nothing>()
    data object LoginSuccess: FirebaseAuthResult<Nothing>()
    data class NewUser<T>(val resultData: T) : FirebaseAuthResult<T>()
    data class NetworkError(val exception: Throwable) : FirebaseAuthResult<Nothing>()
}