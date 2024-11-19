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
import org.comon.moviefriends.domain.repo.ChatRepository
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
): ViewModel() {

    private val _chatList = MutableStateFlow<APIResult<List<RequestChatInfo?>>>(APIResult.NoConstructor)
    val chatList = _chatList.asStateFlow()

    fun loadChatList(userId: String) {
        viewModelScope.launch {
            repository.loadChatList(userId).collectLatest {
                _chatList.value = it
            }
        }
    }
}