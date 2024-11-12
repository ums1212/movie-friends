package org.comon.moviefriends.presenter.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.data.repo.LoginRepository
import org.comon.moviefriends.data.repo.LoginRepositoryImpl

class LoginViewModel(
    private val repository: LoginRepository = LoginRepositoryImpl(),
    private val auth: FirebaseAuth = Firebase.auth,
): ViewModel() {

    private val _splashScreenState = MutableStateFlow(true)
    val splashScreenState = _splashScreenState.asStateFlow()

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user = _user.asStateFlow()

    fun checkLogin() = flow {
        emit(LoginResult.Loading)
        auth.currentUser.let {
            it?.reload()?.await()
            _user.value = it
            _splashScreenState.emit(false)
            emit(LoginResult.Success(it != null))
        }
    }.catch {
        _splashScreenState.emit(false)
        emit(LoginResult.NetworkError(it))
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
                    _user.value = user
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
    ) = viewModelScope.launch {
        repository.googleLogin(context, googleOAuth)
            .collectLatest { result ->
            when(result){
                APIResult.Loading -> loadingState.value = true
                is APIResult.Success -> {
                    moveToSubmitNickNameScreen(result.resultData)
                }
                is APIResult.NetworkError -> {
                    loadingState.value = false
                    Log.e("googleLogin", "${result.exception}")
                    showErrorMessage()
                    moveToSubmitNickNameScreen(null)
                }
                else -> loadingState.value = false
            }
        }
    }

    fun completeJoinUser(
        userInfo: UserInfo,
        loadingState: MutableState<Boolean>,
        moveToScaffoldScreen: () -> Unit,
        showErrorMessage: () -> Unit,
    ) = viewModelScope.launch {
        repository.insertUserInfoToFireStore(userInfo).collectLatest { result ->
            when(result){
                is LoginResult.Loading -> loadingState.value = true
                is LoginResult.Success -> {
                    loadingState.value = false
                    if(result.resultData){
                        moveToScaffoldScreen()
                    }else{
                        showErrorMessage()
                    }
                }
                is LoginResult.NetworkError -> {
                    loadingState.value = false
                    showErrorMessage()
                }
            }
        }
    }
}

enum class JoinType(val str: String) {
    KAKAO("kakao"),
    GOOGLE("google"),
}
sealed class LoginResult<out T> {
    data object Loading : LoginResult<Nothing>()
    data class Success<T>(val resultData: T) : LoginResult<T>()
    data class NetworkError(val exception: Throwable) : LoginResult<Nothing>()
}