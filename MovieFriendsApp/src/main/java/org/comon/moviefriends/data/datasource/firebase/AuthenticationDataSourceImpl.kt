package org.comon.moviefriends.data.datasource.firebase

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.BuildConfig
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.presenter.viewmodel.LoginResult

class AuthenticationDataSourceImpl(
    private val auth: FirebaseAuth = Firebase.auth,
    private val db: FirebaseFirestore = Firebase.firestore
): AuthenticationDataSource {

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
            val userInfo = getUserInfoFromFireStore(user.uid)
            MFPreferences.setUserInfo(userInfo)
            MFPreferences.setFcmToken(userInfo.fcmToken)
            emit(FirebaseAuthResult.LoginSuccess)
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
            val userInfo = getUserInfoFromFireStore(user.uid)
            MFPreferences.setUserInfo(userInfo)
            MFPreferences.setFcmToken(userInfo.fcmToken)
            emit(FirebaseAuthResult.LoginSuccess)
        }
    }.catch {
        emit(FirebaseAuthResult.NetworkError(it))
    }

    override suspend fun insertUserInfoToFireStore(userInfo: UserInfo) = flow {
        emit(LoginResult.Loading)
        val token = FirebaseMessaging.getInstance().token.await()
        val tokenUser = userInfo.copy(fcmToken = token)
        MFPreferences.setFcmToken(token)
        MFPreferences.setUserInfo(tokenUser)
        insertUserFcmToken(userInfo.id, token)
        db.collection("user").add(tokenUser).await()
        emit(LoginResult.Success(true))
    }.catch {
        emit(LoginResult.NetworkError(it))
    }

    override suspend fun getUserInfoFromFireStore(uid: String): UserInfo {
        val querySnapshot = db.collection("user").whereEqualTo("id", uid).get().await()
        val userInfo = querySnapshot.documents.first().toObject(UserInfo::class.java)
            ?: throw FirebaseFirestoreException("해당 유저가 없습니다.", FirebaseFirestoreException.Code.NOT_FOUND)
        return userInfo
    }

    override suspend fun insertUserFcmToken(userId: String, token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            db.collection("user_token").add(mapOf("token" to token, "userId" to userId))
        }
    }
}