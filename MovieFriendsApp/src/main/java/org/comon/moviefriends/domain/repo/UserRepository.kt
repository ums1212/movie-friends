package org.comon.moviefriends.domain.repo

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.datasource.firebase.FirebaseAuthResult
import org.comon.moviefriends.data.entity.firebase.UserInfo
import org.comon.moviefriends.data.entity.sendbird.ResponseSendBirdUserDto
import org.comon.moviefriends.presenter.viewmodel.LoginResult

interface UserRepository {

    suspend fun kakaoLogin(context: Context): Flow<FirebaseAuthResult<FirebaseUser>>

    suspend fun googleLogin(context: Activity): Flow<FirebaseAuthResult<FirebaseUser>>

    suspend fun insertUserInfoToFireStore(userInfo: UserInfo): Flow<LoginResult<Boolean>>

    suspend fun createSendBirdUser(userInfo: UserInfo): ResponseSendBirdUserDto?

    fun connectSendBird(sendBirdId: String, sendBirdToken: String, )

}