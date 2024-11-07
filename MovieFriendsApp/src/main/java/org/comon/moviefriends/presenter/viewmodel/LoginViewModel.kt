package org.comon.moviefriends.presenter.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.repo.LoginRepository

class LoginViewModel: ViewModel() {

    private val auth = Firebase.auth

    private val repository = LoginRepository()

    fun checkLogin() = false

    private val _loginState = MutableStateFlow(false)
    val loginState = _loginState.asStateFlow()

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user = _user.asStateFlow()
    fun setUser(user:FirebaseUser?){
        viewModelScope.launch {
            _user.emit(user)
        }
    }

    fun kakaoLogin(
        context: Context,
        moveToScaffoldScreen: () -> Unit,
        moveToNextScreen: (user: FirebaseUser) -> Unit
    ) {
        viewModelScope.launch {
            repository.kakaoLogin(
                context = context,
                moveToScaffoldScreen = moveToScaffoldScreen,
                moveToNextScreen = { user ->
                    moveToNextScreen(user)
                    setUser(user)
                }
            )
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

    fun completeJoinUser(){

    }
}

enum class LoginCategory {
    WITHOUT_LOGIN,
    KAKAO_LOGIN,
    GOOGLE_LOGIN,
}