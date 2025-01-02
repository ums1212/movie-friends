package org.comon.moviefriends.data.repo

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.messaging.FirebaseMessaging
import com.sendbird.android.SendbirdChat
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.data.datasource.firebase.FirebaseAuthResult
import org.comon.moviefriends.data.datasource.firebase.UserDataSource
import org.comon.moviefriends.data.datasource.sendbird.SendBirdService
import org.comon.moviefriends.data.entity.firebase.UserInfo
import org.comon.moviefriends.data.entity.sendbird.CreateSendBirdUserDto
import org.comon.moviefriends.data.entity.sendbird.ResponseSendBirdUserDto
import org.comon.moviefriends.domain.repo.UserRepository
import org.comon.moviefriends.presentation.viewmodel.LoginResult
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor (
    private val userDataSource: UserDataSource,
    private val auth: FirebaseAuth,
    private val fcm: FirebaseMessaging,
): UserRepository {

    private suspend fun snsLogin(context: Context, provider: String) = runCatching {
        val providerBuilder = OAuthProvider.newBuilder(provider)
        val signInResult = auth.startActivityForSignInWithProvider(context as Activity, providerBuilder.build()).await()
        val user = signInResult.user ?: throw FirebaseAuthException("snsLogin","FirebaseUser is Null")
        val additionalUserInfo = signInResult.additionalUserInfo ?: throw FirebaseAuthException("snsLogin","FirebaseUser is Null")
        var loginResult: FirebaseAuthResult<FirebaseUser> = FirebaseAuthResult.LoginSuccess
        if(additionalUserInfo.isNewUser){
            // 신규 유저일 경우
            loginResult = FirebaseAuthResult.NewUser(user)
        }else{
            // 기존 유저일 경우
            try {
                userDataSource.getUserInfoFromFireStore(user.uid)
                    .onSuccess { userInfo ->
                        connectSendBird(userInfo.sendBirdId, userInfo.sendBirdToken)
                        val newToken = fcm.token.await()
                        if(userInfo.fcmToken != newToken){
                            userDataSource.updateFcmToken(user.uid, newToken)
                        }
                        MFPreferences.setUserInfo(userInfo)
                        MFPreferences.setFcmToken(newToken)
                        loginResult = FirebaseAuthResult.LoginSuccess
                    }
                    .onFailure { throw it }
            }catch (e: FirebaseFirestoreException){
                // SNS 계정 연결은 되어있으나 파이어스토어에 등록되어있지 않은 상태이면
                loginResult = FirebaseAuthResult.NewUser(user)
            }
        }
        loginResult
    }

    override suspend fun kakaoLogin(context: Context) = flow {
        emit(FirebaseAuthResult.Loading)
        snsLogin(context, "oidc.kakao")
            .onSuccess { emit(it) }
            .onFailure { FirebaseAuthResult.NetworkError(it) }
    }.catch {
        emit(FirebaseAuthResult.NetworkError(it))
    }

    override suspend fun googleLogin(context: Activity) = flow {
        emit(FirebaseAuthResult.Loading)
        snsLogin(context, "google.com")
            .onSuccess { emit(it) }
            .onFailure { FirebaseAuthResult.NetworkError(it) }
    }.catch {
        emit(FirebaseAuthResult.NetworkError(it))
    }

    override suspend fun insertUserInfoToFireStore(userInfo: UserInfo) = flow {
        val sendBirdUser = createSendBirdUser(userInfo)
        if(sendBirdUser!=null){
            val sendBirdId = sendBirdUser.userId
            val sendBirdToken = sendBirdUser.accessToken
            MFPreferences.setSendBirdToken(sendBirdToken)
            val token = fcm.token.await()
            val tokenUser = userInfo.copy(
                fcmToken = token,
                sendBirdId = sendBirdId,
                sendBirdToken = sendBirdToken
            )
            MFPreferences.setFcmToken(token)
            MFPreferences.setUserInfo(tokenUser)
            userDataSource.insertUserInfoToFireStore(userInfo)
            emit(LoginResult.Success(true))
        }else{
            emit(LoginResult.NetworkError(Exception("sendbird 네트워크 에러")))
        }
    }.catch {
        emit(LoginResult.NetworkError(it))
    }

    override suspend fun createSendBirdUser(userInfo: UserInfo): ResponseSendBirdUserDto? {
        val sendBirdUser = CreateSendBirdUserDto(
            userId = userInfo.id,
            nickname = userInfo.nickName,
            profileUrl = userInfo.profileImage,
            issueAccessToken = true,
            sessionTokenExpiresAt = 1542945056625,
            metadata = CreateSendBirdUserDto.Metadata("","")
        )
        return SendBirdService.getInstance().createSendBirdUser(sendBirdUser).body()
    }

    override fun connectSendBird(sendBirdId: String, sendBirdToken: String) {
        SendbirdChat.connect(sendBirdId, sendBirdToken) { _, sendbirdException ->
            if(sendbirdException!=null){
                Log.e("connectSendBird", "$sendbirdException")
            }else{
                Log.i("connectSendBird", "success")
            }
        }
    }

}