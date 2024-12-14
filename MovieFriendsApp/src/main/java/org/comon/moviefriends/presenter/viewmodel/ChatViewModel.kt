package org.comon.moviefriends.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.RequestChatInfo
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.domain.repo.ChatRepository
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
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

    private val _requestState = MutableStateFlow<APIResult<Boolean>>(APIResult.NoConstructor)
    val requestState = _requestState.asStateFlow()

    private val _chatList = MutableStateFlow<APIResult<List<RequestChatInfo?>>>(APIResult.NoConstructor)
    val chatList = _chatList.asStateFlow()

    fun loadChatList(userId: String) {
        viewModelScope.launch {
            chatRepository.loadChatList(userId).collectLatest {
                _chatList.value = it
            }
        }
    }

    fun getAllChatRequestCount(){
        viewModelScope.launch {
            chatRepository.getAllChatRequestCount(_userInfo.value?.id ?: "").collectLatest {
                _allChatRequestCount.emit(it)
            }
        }
    }

    fun confirmRequest(requestChatInfo: RequestChatInfo){
        viewModelScope.launch {
            if(userInfo.value!=null){
                chatRepository.confirmRequest(requestChatInfo).collectLatest {
                    _requestState.emit(it)
                }
            }
        }
    }

    fun denyRequest(requestChatInfo: RequestChatInfo){
        viewModelScope.launch {
            chatRepository.denyRequest(requestChatInfo).collectLatest {
                _requestState.emit(it)
            }
        }
    }

    fun getMyRequestList() = viewModelScope.launch {
        chatRepository.getRequestChatList(_userInfo.value?.id ?: "").collectLatest {
            _myChatRequestList.emit(it)
//            when(it){
//                is APIResult.Success -> _myRequestList.emit(it.resultData)
//                else -> {}
//            }
        }
    }

    fun getMyReceiveList() = viewModelScope.launch {
        chatRepository.getReceiveChatList(_userInfo.value?.id ?: "").collectLatest {
            _myChatReceiveList.emit(it)
        }
    }
}