package org.comon.moviefriends.data.repo

import android.app.Activity
import android.content.Context
import org.comon.moviefriends.data.datasource.firebase.UserDataSource
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.domain.repo.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor (
    private val userDataSource: UserDataSource
): UserRepository {

    override suspend fun kakaoLogin(context: Context) =
        userDataSource.kakaoLogin(context)

    override suspend fun googleLogin(context: Activity) =
        userDataSource.googleLogin(context)

    override suspend fun insertUserInfoToFireStore(userInfo: UserInfo) =
        userDataSource.insertUserInfoToFireStore(userInfo)

    override fun connectSendBird(sendBirdId: String, sendBirdToken: String) =
        userDataSource.connectSendBird(sendBirdId, sendBirdToken)

}