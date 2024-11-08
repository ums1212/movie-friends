package org.comon.moviefriends.data.datasource.firebase

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.UserInfo
import org.comon.moviefriends.presenter.viewmodel.LoginResult

class AuthenticationDataSource {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    fun kakaoLogin(
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

    suspend fun googleLogin(context: Context, googleOAuth: String) = flow {
        emit(APIResult.Loading)

        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(googleOAuth)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        request.runCatching {
            credentialManager.getCredential(
                request = request,
                context = context,
            )
        }.onSuccess {
            when (it.credential) {
                is CustomCredential -> {
                    if (it.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        // Use googleIdTokenCredential and extract id to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(it.credential.data)
                        val signIn =
                            auth.signInWithCustomToken(googleIdTokenCredential.idToken).await()
                        signIn.user?.let { user ->
                            emit(APIResult.Success(user))
                        }
                    }
                }
            }
        }.onFailure {
            emit(APIResult.NetworkError(it))
        }
    }.catch {
        Log.d("test1234", "$it")
        emit(APIResult.NetworkError(it))
    }

    suspend fun insertUserInfo(userInfo: UserInfo) = flow {
        emit(APIResult.Loading)
        auth.currentUser?.let {
            auth.updateCurrentUser(it).await()
            db.collection("user").add(userInfo).await()
            emit(APIResult.Success(true))
        }
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    suspend fun deleteUserInfo(userId: String) = flow {
        emit(APIResult.Loading)
        auth.currentUser?.let {
            auth.updateCurrentUser(it).await()
            it.delete().await()
            db.collection("user")
                .whereEqualTo("id", userId).get().await()
                .documents.first().reference.delete().await()
            emit(APIResult.Success(true))
        }
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    suspend fun insertUserInfoToFireStore(userInfo: UserInfo) = flow {
        emit(LoginResult.Loading)
        db.collection("user").add(userInfo).await()
        emit(LoginResult.Success(true))
    }.catch {
        emit(LoginResult.NetworkError(it))
    }




}