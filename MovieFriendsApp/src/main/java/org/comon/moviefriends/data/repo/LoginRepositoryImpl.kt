package org.comon.moviefriends.data.repo

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseUser
import org.comon.moviefriends.data.datasource.firebase.AuthenticationDataSource
import org.comon.moviefriends.data.datasource.firebase.AuthenticationDataSourceImpl
import org.comon.moviefriends.data.model.firebase.UserInfo

class LoginRepositoryImpl(
    private val authDataSource: AuthenticationDataSource = AuthenticationDataSourceImpl()
): LoginRepository {

    override fun kakaoLogin(
        context: Context,
        moveToScaffoldScreen: () -> Unit,
        moveToNextScreen: (user: FirebaseUser) -> Unit
    ) {
        authDataSource.kakaoLogin(context, moveToScaffoldScreen){ user ->
            moveToNextScreen(user)
        }
    }

    override suspend fun googleLogin(context: Activity, googleOAuth: String) =
        authDataSource.googleLogin(context, googleOAuth)

    override suspend fun insertUserInfoToFireStore(userInfo: UserInfo) =
        authDataSource.insertUserInfoToFireStore(userInfo)

}