package org.comon.moviefriends.presenter.viewmodel

import android.app.Activity
import android.content.Context
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

    private val _userUid = MutableStateFlow("")
    fun setUserUid(input: String){
        _userUid.value = input
    }

    private val _userPhotoUrl = MutableStateFlow("")
    fun setUserPhotoUrl(input: String){
        _userPhotoUrl.value = input
    }

    private val _userJoinType = MutableStateFlow("")
    fun setUserJoinType(input: String){
        _userJoinType.value = input
    }

    private val _userNickName = MutableStateFlow("")
    fun setUserNickName(input: String){
        _userNickName.value = input
    }

    private val _userGender = MutableStateFlow("")
    fun setUserGender(input: String){
        _userGender.value = input
    }

    private val _userAgeRange = MutableStateFlow(-1)
    fun setUserAgeRange(input: Int){
        _userAgeRange.value = input
    }

    fun checkLogin() = flow {
        emit(LoginResult.Loading)
        auth.currentUser.let { authUser ->
            authUser?.reload()?.await()
            _user.value = authUser
            _splashScreenState.emit(false)
            MFPreferences.getUserInfo()?.let { userInfo ->
                repository.connectSendBird(userInfo.sendBirdId, userInfo.sendBirdToken)
            }
            emit(LoginResult.Success(authUser!=null))
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
        moveToScaffoldScreen: () -> Unit,
        showErrorMessage: () -> Unit,
    ){
        _loadingState.value = true
        viewModelScope.launch {
            val userInfo = UserInfo(
                id = _userUid.value,
                profileImage = _userPhotoUrl.value,
                joinType = _userJoinType.value,
                nickName = _userNickName.value,
                gender = _userGender.value,
                ageRange = _userAgeRange.value
            )
            repository.insertUserInfoToFireStore(userInfo).collectLatest { result ->
                when(result){
                    is LoginResult.Loading -> _loadingState.value = true
                    is LoginResult.Success -> {
                        if(result.resultData){
                            moveToScaffoldScreen()
                        }else{
                            showErrorMessage()
                            _loadingState.value = false
                        }
                    }
                    is LoginResult.NetworkError -> {
                        _loadingState.value = false
                        showErrorMessage()
                    }
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