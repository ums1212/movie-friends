package org.comon.moviefriends.presenter.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.repo.LoginRepository

class LoginViewModel: ViewModel() {

    private val repository = LoginRepository()

    fun checkLogin() = false

    fun snsLogin(category: LoginCategory, moveToNextScreen: () -> Unit) {
        when(category){
            LoginCategory.WITHOUT_LOGIN -> {

            }
            LoginCategory.KAKAO_LOGIN -> kakaoLogin()
            LoginCategory.GOOGLE_LOGIN -> {}
        }
        moveToNextScreen()
    }

    fun kakaoLogin() {

    }

    fun googleLogin(
        context: Context,
        googleOAuth: String,
        moveToSubmitNickNameScreen: () -> Unit,
        loadingState: MutableState<Boolean>,
        showErrorMessage: () -> Unit,
    ){
        viewModelScope.launch {
            repository.googleLogin(context, googleOAuth).collectLatest { result ->
                when(result){
                    APIResult.Loading -> loadingState.value = true
                    is APIResult.Success -> {
                        loadingState.value = false
                        moveToSubmitNickNameScreen()
                    }
                    is APIResult.NetworkError -> {
                        loadingState.value = false
                        Log.d("test1234", "${result.exception.message}")
                        showErrorMessage()
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