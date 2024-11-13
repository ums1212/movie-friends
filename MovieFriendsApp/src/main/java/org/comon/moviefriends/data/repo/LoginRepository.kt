package org.comon.moviefriends.data.repo

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.presenter.viewmodel.LoginResult

interface LoginRepository {

    fun kakaoLogin(
        context: Context,
        moveToScaffoldScreen: () -> Unit,
        moveToNextScreen: (user: FirebaseUser) -> Unit
    ): Unit

    suspend fun googleLogin(
        context: Activity,
        googleOAuth: String
    ): Flow<APIResult<FirebaseUser?>>

    suspend fun insertUserInfoToFireStore(
        userInfo: UserInfo
    ): Flow<LoginResult<Boolean>>

}