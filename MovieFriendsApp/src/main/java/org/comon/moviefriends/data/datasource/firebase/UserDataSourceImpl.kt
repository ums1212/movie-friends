package org.comon.moviefriends.data.datasource.firebase

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.messaging.FirebaseMessaging
import com.sendbird.android.SendbirdChat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.data.datasource.sendbird.SendBirdService
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.data.model.sendbird.CreateSendBirdUserDto
import org.comon.moviefriends.data.model.sendbird.ResponseSendBirdUserDto
import org.comon.moviefriends.presenter.viewmodel.LoginResult
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor (
    private val auth: FirebaseAuth,
    private val fs: FirebaseFirestore,
): UserDataSource {

    override suspend fun kakaoLogin(context: Context) = flow {
        emit(FirebaseAuthResult.Loading)
        val providerBuilder = OAuthProvider.newBuilder("oidc.kakao")
        val result = auth.startActivityForSignInWithProvider(context as Activity, providerBuilder.build()).await()
        val user = result.user
        val additionalUserInfo = result.additionalUserInfo
        if(user==null || additionalUserInfo==null) throw FirebaseAuthException("kakaoLogin","FirebaseUser is Null")
        if(additionalUserInfo.isNewUser){
            // 신규 유저일 경우
            emit(FirebaseAuthResult.NewUser(user))
        }else{
            // 기존 유저일 경우
            try {
                val userInfo = getUserInfoFromFireStore(user.uid)
                connectSendBird(userInfo.sendBirdId, userInfo.sendBirdToken)
                MFPreferences.setUserInfo(userInfo)
                MFPreferences.setFcmToken(userInfo.fcmToken)
                emit(FirebaseAuthResult.LoginSuccess)
            }catch (e: FirebaseFirestoreException){
                // SNS 계정 연결은 되어있으나 파이어스토어에 등록되어있지 않은 상태이면
                emit(FirebaseAuthResult.NewUser(user))
            }
        }
    }.catch {
        emit(FirebaseAuthResult.NetworkError(it))
    }

    override suspend fun googleLogin(context: Activity) = flow {
        emit(FirebaseAuthResult.Loading)
        val provider = OAuthProvider.newBuilder("google.com")
        val result = auth.startActivityForSignInWithProvider(context, provider.build()).await()
        val user = result.user
        val additionalUserInfo = result.additionalUserInfo
        if(user==null || additionalUserInfo==null) throw FirebaseAuthException("kakaoLogin","FirebaseUser is Null")
        if(additionalUserInfo.isNewUser){
            // 신규 유저일 경우
            emit(FirebaseAuthResult.NewUser(user))
        }else{
            // 기존 유저일 경우
            try {
                val userInfo = getUserInfoFromFireStore(user.uid)
                connectSendBird(userInfo.sendBirdId, userInfo.sendBirdToken)
                MFPreferences.setUserInfo(userInfo)
                MFPreferences.setFcmToken(userInfo.fcmToken)
                emit(FirebaseAuthResult.LoginSuccess)
            }catch (e: FirebaseFirestoreException){
                // SNS 계정 연결은 되어있으나 파이어스토어에 등록되어있지 않은 상태이면
                emit(FirebaseAuthResult.NewUser(user))
            }
        }
    }.catch {
        emit(FirebaseAuthResult.NetworkError(it))
    }

    override suspend fun insertUserInfoToFireStore(userInfo: UserInfo) = flow {
        val sendBirdUser = createSendBirdUser(userInfo)
        if(sendBirdUser!=null){
            val sendBirdId = sendBirdUser.userId
            val sendBirdToken = sendBirdUser.accessToken
            MFPreferences.setSendBirdToken(sendBirdToken)
            val token = FirebaseMessaging.getInstance().token.await()
            val tokenUser = userInfo.copy(fcmToken = token, sendBirdId = sendBirdId, sendBirdToken = sendBirdToken)
            MFPreferences.setFcmToken(token)
            MFPreferences.setUserInfo(tokenUser)
            insertUserFcmToken(userInfo.id, token)
            fs.collection("user").add(tokenUser).await()
            emit(LoginResult.Success(true))
        }else{
            emit(LoginResult.NetworkError(Exception("sendbird 네트워크 에러")))
        }
    }.catch {
        emit(LoginResult.NetworkError(it))
    }

    override suspend fun getUserInfoFromFireStore(uid: String): UserInfo {
        val querySnapshot = fs.collection("user").whereEqualTo("id", uid).get().await()
        val userInfo = querySnapshot.documents.first().toObject(UserInfo::class.java)
            ?: throw FirebaseFirestoreException("해당 유저가 없습니다.", FirebaseFirestoreException.Code.NOT_FOUND)
        return userInfo
    }

    override suspend fun insertUserFcmToken(userId: String, token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            fs.collection("user_token").add(mapOf("token" to token, "userId" to userId))
        }
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