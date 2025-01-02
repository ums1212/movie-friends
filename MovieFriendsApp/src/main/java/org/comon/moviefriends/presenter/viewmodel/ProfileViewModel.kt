package org.comon.moviefriends.presenter.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.comon.moviefriends.data.entity.firebase.UserInfo
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor (): ViewModel() {

    private val _user: MutableStateFlow<UserInfo?> = MutableStateFlow(null)
    val user = _user.asStateFlow()
    fun getUserInfo(user: UserInfo?){
        _user.value = user
    }
}