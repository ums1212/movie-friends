package org.comon.moviefriends.data.datasource.firebase

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.presenter.viewmodel.LoginResult

interface AuthenticationDataSource {
    fun kakaoLogin(
        context: Context,
        moveToScaffoldScreen: () -> Unit,
        moveToNextScreen: (user: FirebaseUser) -> Unit
    )

    suspend fun googleLogin(
        context: Activity,
        googleOAuth: String
    ): Flow<APIResult<FirebaseUser?>>

    suspend fun insertUserInfoToFireStore(
        userInfo: UserInfo
    ): Flow<LoginResult<Boolean>>

    suspend fun insertUserFcmToken(
        userId: String,
        token: String
    )
}