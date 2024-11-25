package org.comon.moviefriends.presenter.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.data.datasource.firebase.FirebaseAuthResult
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.domain.repo.LoginRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor  (
    private val repository: LoginRepository,
    private val auth: FirebaseAuth,
): ViewModel() {

    private val _splashScreenState = MutableStateFlow(true)
    val splashScreenState = _splashScreenState.asStateFlow()

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user = _user.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    fun checkLogin() = flow {
        emit(LoginResult.Loading)
        auth.currentUser.let {
            it?.reload()?.await()
            _user.value = it
            _splashScreenState.emit(false)
            val user = MFPreferences.getUserInfo()
            user?.let {
                repository.connectSendBird(user.sendBirdId, user.sendBirdToken)
            }
            emit(LoginResult.Success(it!=null))
        }
    }.catch {
        _splashScreenState.emit(false)
        emit(LoginResult.NetworkError(it))
    }

    fun kakaoLogin(
        context: Context,
        showError: () -> Unit,
        moveToScaffoldScreen: () -> Unit,
        moveToSubmitNickNameScreen: (user: FirebaseUser) -> Unit,
    ) {
        viewModelScope.launch {
            repository.kakaoLogin(context).collectLatest { result ->
                when(result){
                    FirebaseAuthResult.Loading -> _loadingState.emit(true)
                    is FirebaseAuthResult.NetworkError -> {
                        _loadingState.emit(false)
                        showError()
                    }
                    is FirebaseAuthResult.LoginSuccess -> {
                        moveToScaffoldScreen()
                    }
                    is FirebaseAuthResult.NewUser -> {
                        moveToSubmitNickNameScreen(result.resultData)
                    }
                }

            }
        }
    }

    fun googleLogin(
        context: Activity,
        showError: () -> Unit,
        moveToScaffoldScreen: () -> Unit,
        moveToSubmitNickNameScreen: (user: FirebaseUser) -> Unit,
    ) = viewModelScope.launch {
        repository.googleLogin(context).collectLatest { result ->
            when(result){
                FirebaseAuthResult.Loading -> _loadingState.emit(true)
                is FirebaseAuthResult.NetworkError -> {
                    _loadingState.emit(false)
                    showError()
                }
                is FirebaseAuthResult.LoginSuccess -> {
                    moveToScaffoldScreen()
                }
                is FirebaseAuthResult.NewUser -> {
                    moveToSubmitNickNameScreen(result.resultData)
                }
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
                    if(result.resultData){
                        moveToScaffoldScreen()
                    }else{
                        showErrorMessage()
                    }
                    loadingState.value = false
                }
                is LoginResult.NetworkError -> {
                    Log.e("test1234", "${result.exception}")
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