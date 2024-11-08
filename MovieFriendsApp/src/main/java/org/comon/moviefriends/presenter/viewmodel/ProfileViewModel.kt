package org.comon.moviefriends.presenter.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.comon.moviefriends.data.model.UserInfo

class ProfileViewModel: ViewModel() {

    private val _user: MutableStateFlow<UserInfo?> = MutableStateFlow(null)
    val user = _user.asStateFlow()
    fun getUserInfo(user: UserInfo?){
        _user.value = user
    }
}