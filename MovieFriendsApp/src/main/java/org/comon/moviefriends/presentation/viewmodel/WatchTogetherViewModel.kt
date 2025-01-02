package org.comon.moviefriends.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.firebase.RequestChatInfo
import org.comon.moviefriends.data.entity.firebase.UserInfo
import org.comon.moviefriends.data.entity.firebase.UserWantMovieInfo
import org.comon.moviefriends.domain.repo.ChatRepository
import org.comon.moviefriends.domain.repo.MovieRepository
import javax.inject.Inject

@HiltViewModel
class WatchTogetherViewModel @Inject constructor (
    private val movieRepository: MovieRepository,
    private val chatRepository: ChatRepository,
): ViewModel() {

    private val _userInfo: MutableStateFlow<UserInfo?> = MutableStateFlow(null)
    val userInfo get() = _userInfo.asStateFlow()
    fun getUserInfo(userInfo: UserInfo?) {
        _userInfo.value = userInfo
    }

    private val _allChatRequestCount = MutableStateFlow<APIResult<Map<String, Int>>>(APIResult.NoConstructor)
    val allChatRequestCount = _allChatRequestCount.asStateFlow()

    private val _myChatRequestList = MutableStateFlow<APIResult<List<RequestChatInfo?>>>(APIResult.NoConstructor)
    val myChatRequestList = _myChatRequestList.asStateFlow()

    private val _myChatReceiveList = MutableStateFlow<APIResult<List<RequestChatInfo?>>>(APIResult.NoConstructor)
    val myChatReceiveList = _myChatReceiveList.asStateFlow()

    private val _allUserWantList = MutableStateFlow<APIResult<List<UserWantMovieInfo?>>>(APIResult.NoConstructor)
    val allUserWantList = _allUserWantList.asStateFlow()

    fun getAllUserWantList() = viewModelScope.launch {
        movieRepository.getAllUserWantListExceptMe(_userInfo.value?.id ?: "").collectLatest {
            _allUserWantList.emit(it)
        }
    }

    fun getAllChatRequestCount(){
        viewModelScope.launch {
            chatRepository.getAllChatRequestCount(_userInfo.value?.id ?: "").collectLatest {
                _allChatRequestCount.emit(it)
            }
        }
    }

    fun getMyRequestList() = viewModelScope.launch {
        chatRepository.getRequestChatList(_userInfo.value?.id ?: "").collectLatest {
            _myChatRequestList.emit(it)
        }
    }

    fun getMyReceiveList() = viewModelScope.launch {
        chatRepository.getReceiveChatList(_userInfo.value?.id ?: "").collectLatest {
            _myChatReceiveList.emit(it)
        }
    }

    suspend fun requestWatchTogether(requestChatInfo: RequestChatInfo) =
        chatRepository.requestWatchTogether(requestChatInfo)

    suspend fun confirmRequest(requestChatInfo: RequestChatInfo) =
        chatRepository.confirmRequest(requestChatInfo)

    suspend fun denyRequest(requestChatInfo: RequestChatInfo) =
        chatRepository.denyRequest(requestChatInfo)
}