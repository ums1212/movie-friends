package org.comon.moviefriends.data.repo

import android.app.Activity
import android.content.Context
import org.comon.moviefriends.data.datasource.firebase.AuthenticationDataSource
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.domain.repo.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor (
    private val authDataSource: AuthenticationDataSource
): LoginRepository {

    override suspend fun kakaoLogin(context: Context) =
        authDataSource.kakaoLogin(context)

    override suspend fun googleLogin(context: Activity) =
        authDataSource.googleLogin(context)

    override suspend fun insertUserInfoToFireStore(userInfo: UserInfo) =
        authDataSource.insertUserInfoToFireStore(userInfo)

    override fun connectSendBird(sendBirdId: String, sendBirdToken: String) =
        authDataSource.connectSendBird(sendBirdId, sendBirdToken)

}