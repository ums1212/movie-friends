package org.comon.moviefriends.data.repo

import android.content.Context
import org.comon.moviefriends.data.datasource.firebase.AuthenticationDataSource

class LoginRepository {

    private val authDataSource = AuthenticationDataSource()

    suspend fun googleLogin(context: Context, googleOAuth: String) = authDataSource.googleLogin(context, googleOAuth)
}