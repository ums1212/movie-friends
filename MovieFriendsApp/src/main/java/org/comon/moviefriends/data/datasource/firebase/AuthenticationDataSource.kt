package org.comon.moviefriends.data.datasource.firebase

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.UserInfo

class AuthenticationDataSource {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    suspend fun googleLogin(context: Context, googleOAuth: String) = flow {
        emit(APIResult.Loading)

        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(googleOAuth)
            .build()
        Log.d("test1234", "googleIdOption")

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        Log.d("test1234", "request")

        runCatching {
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
                        signIn.user?.let {
                            emit(APIResult.Success(APIResult.Success(it.uid)))
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




}