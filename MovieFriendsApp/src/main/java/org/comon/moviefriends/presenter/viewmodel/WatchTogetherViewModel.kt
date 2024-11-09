package org.comon.moviefriends.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.UserInfo
import org.comon.moviefriends.data.model.UserWantMovieInfo

class WatchTogetherViewModel: ViewModel() {
    private val _userWantList = MutableStateFlow<List<UserWantMovieInfo>>(emptyList())
    val userWantList get() = _userWantList.asStateFlow()

    fun requestWatchTogether(sendUser: UserInfo?, receiveUser: UserInfo) = flow {
        emit(APIResult.Loading)
        if(sendUser==null) throw Exception("user값이 없습니다.")
        emit(APIResult.Success(true))
    }.catch {
        emit(APIResult.NetworkError(it))
    }
}