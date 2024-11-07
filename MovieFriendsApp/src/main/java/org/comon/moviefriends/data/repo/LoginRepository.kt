package org.comon.moviefriends.data.repo

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import org.comon.moviefriends.data.datasource.firebase.AuthenticationDataSource

class LoginRepository {

    private val authDataSource = AuthenticationDataSource()

    fun kakaoLogin(
        context: Context,
        moveToScaffoldScreen: () -> Unit,
        moveToNextScreen: (user: FirebaseUser) -> Unit) =
        authDataSource.kakaoLogin(
            context,
            moveToScaffoldScreen
        ){ user ->
            moveToNextScreen(user)
        }

    suspend fun googleLogin(context: Context, googleOAuth: String) =
        authDataSource.googleLogin(context, googleOAuth)
}