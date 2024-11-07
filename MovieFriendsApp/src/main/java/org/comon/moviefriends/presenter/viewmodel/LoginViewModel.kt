package org.comon.moviefriends.presenter.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.repo.LoginRepository
import java.util.UUID

class LoginViewModel: ViewModel() {

    private val auth = Firebase.auth

    private val repository = LoginRepository()

    fun checkLogin() = false

    private val _loginState = MutableStateFlow(false)
    val loginState = _loginState.asStateFlow()


    fun kakaoLogin(
        context: Context,
        moveToScaffoldScreen: () -> Unit,
        moveToNextScreen: (user: FirebaseUser) -> Unit
    ) {
        viewModelScope.launch {
            repository.kakaoLogin(
                context,
                moveToScaffoldScreen
            ){ user ->
                moveToNextScreen(user)
            }
        }
    }

    fun googleLogin(
        context: Context,
        googleOAuth: String,
        moveToSubmitNickNameScreen: (user: FirebaseUser?) -> Unit,
        loadingState: MutableState<Boolean>,
        showErrorMessage: () -> Unit,
    ){
        viewModelScope.launch {
            repository.googleLogin(context, googleOAuth).collectLatest { result ->
                when(result){
                    APIResult.Loading -> loadingState.value = true
                    is APIResult.Success -> {
                        loadingState.value = false
                        _loginState.emit(true)
                        moveToSubmitNickNameScreen(result.resultData)
                    }
                    is APIResult.NetworkError -> {
                        loadingState.value = false
                        Log.d("test1234", "${result.exception}")
                        showErrorMessage()
                        moveToSubmitNickNameScreen(null)
                    }
                    else -> loadingState.value = false
                }
            }
        }
    }
}

enum class LoginCategory {
    WITHOUT_LOGIN,
    KAKAO_LOGIN,
    GOOGLE_LOGIN,
}