package org.comon.moviefriends.data.datasource.firebase

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.presenter.viewmodel.LoginResult

class AuthenticationDataSourceImpl(
    private val auth: FirebaseAuth = Firebase.auth,
    private val db: FirebaseFirestore = Firebase.firestore
): AuthenticationDataSource {

    override fun kakaoLogin(
        context: Context,
        moveToScaffoldScreen: () -> Unit,
        moveToNextScreen: (user: FirebaseUser) -> Unit
    ) {
        val TAG = "kakaoLogin"

        val providerBuilder = OAuthProvider.newBuilder("oidc.kakao")
        val pendingResultTask = auth.pendingAuthResult
        if(pendingResultTask != null){
            pendingResultTask
                .addOnSuccessListener {
                    Log.d(TAG, "이미 pending된 authResult가 있음")
                    it.user?.let{ user ->
                        moveToNextScreen(user)
                    }
                }
                .addOnFailureListener {
                    // Handle failure.
                    Log.e(TAG, "pendingResultTask 실패")
                }
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.
            auth
                .startActivityForSignInWithProvider(context as Activity, providerBuilder.build())
                .addOnSuccessListener {
                    // User is signed in.
                    // IdP data available in
                    // authResult.getAdditionalUserInfo().getProfile().
                    // The OAuth access token can also be retrieved:
                    // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                    // The OAuth secret can be retrieved by calling:
                    // ((OAuthCredential)authResult.getCredential()).getSecret().
                    it.user?.let { user ->
                        Log.d(TAG, "파이어베이스에 계정 등록 완료")
                        moveToNextScreen(user)
                    }
                }
                .addOnFailureListener {
                    // Handle failure.
                    Log.e(TAG, "파이어베이스에 계정 등록 실패")
                }
        }

    }

    override suspend fun googleLogin(context: Activity, googleOAuth: String) = flow {
        emit(APIResult.Loading)
        val provider = OAuthProvider.newBuilder("google.com")
        val result = auth.startActivityForSignInWithProvider(context, provider.build()).await()
        emit(APIResult.Success(result.user))
    }.catch {
        emit(APIResult.NetworkError(it))
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

    override suspend fun insertUserFcmToken(userId: String, token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            db.collection("user_token").add(mapOf("token" to token, "userId" to userId))
        }
    }
}