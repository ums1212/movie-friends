package org.comon.moviefriends.data.datasource.firebase

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.data.model.sendbird.ResponseSendBirdUserDto
import org.comon.moviefriends.presenter.viewmodel.LoginResult

interface AuthenticationDataSource {
    suspend fun kakaoLogin(context: Context): Flow<FirebaseAuthResult<FirebaseUser>>

    suspend fun googleLogin(context: Activity): Flow<FirebaseAuthResult<FirebaseUser>>

    suspend fun insertUserInfoToFireStore(
        userInfo: UserInfo
    ): Flow<LoginResult<Boolean>>

    suspend fun getUserInfoFromFireStore(
        uid: String
    ): UserInfo

    suspend fun insertUserFcmToken(
        userId: String,
        token: String
    )

    suspend fun createSendBirdUser(userInfo: UserInfo): ResponseSendBirdUserDto?

    fun connectSendBird(sendBirdId: String, sendBirdToken: String)
}