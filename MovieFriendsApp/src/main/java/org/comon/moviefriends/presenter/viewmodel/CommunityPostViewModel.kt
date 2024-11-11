package org.comon.moviefriends.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.PostInfo
import org.comon.moviefriends.data.model.firebase.ReplyInfo
import org.comon.moviefriends.data.repo.CommunityPostRepository
import org.comon.moviefriends.data.repo.CommunityPostRepositoryImpl

class CommunityPostViewModel(
    private val repository: CommunityPostRepository = CommunityPostRepositoryImpl()
): ViewModel() {

    private val _postId = MutableStateFlow("")
    val postId = _postId.asStateFlow()
    fun setPostId(postId: String) {
        _postId.value = postId
    }

    private val _insertState = MutableStateFlow<APIResult<String>>(APIResult.NoConstructor)
    val insertState get() = _insertState.asStateFlow()

    private val _updateState = MutableStateFlow<APIResult<String>>(APIResult.NoConstructor)
    val updateState get() = _updateState.asStateFlow()

    private val _deleteState = MutableStateFlow<APIResult<Boolean>>(APIResult.NoConstructor)
    val deleteState get() = _deleteState.asStateFlow()

    private val _getPostState = MutableStateFlow<APIResult<PostInfo?>>(APIResult.NoConstructor)
    val getPostState get() = _getPostState.asStateFlow()

    private val _getAllPostState = MutableStateFlow<APIResult<List<PostInfo?>>>(APIResult.NoConstructor)
    val getAllPostState get() = _getAllPostState.asStateFlow()

    private val _insertReplyState = MutableStateFlow<APIResult<Boolean>>(APIResult.NoConstructor)
    val insertReplyState get() = _insertReplyState.asStateFlow()

    private val _deleteReplyState = MutableStateFlow<APIResult<Boolean>>(APIResult.NoConstructor)
    val deleteReplyState get() = _deleteReplyState.asStateFlow()

    private val _getAllReplyState = MutableStateFlow<APIResult<List<ReplyInfo?>>>(APIResult.NoConstructor)
    val getAllReplyState get() = _getAllReplyState.asStateFlow()

    fun insertPost() {
        viewModelScope.launch {
            val post = PostInfo()
            repository.insertPost(post).collectLatest {
                _insertState.emit(it)
            }
        }
    }

    fun updatePost() {
        viewModelScope.launch {
            val post = PostInfo()
            repository.updatePost(post).collectLatest {
                _updateState.emit(it)
            }
        }
    }

    fun deletePost() {
        viewModelScope.launch {
            repository.deletePost(_postId.value).collectLatest {
                _deleteState.emit(it)
            }
        }
    }

    fun getPost() {
        viewModelScope.launch {
            repository.getPost(_postId.value).collectLatest {
                _getPostState.emit(it)
            }
        }
    }

    fun getALLPost() {
        viewModelScope.launch {
            repository.getALLPost().collectLatest {
                _getAllPostState.emit(it)
            }
        }
    }

    fun insertReply() {
        viewModelScope.launch {
            val reply = ReplyInfo()
            repository.insertReply(reply).collectLatest {
                _insertReplyState.emit(it)
            }
        }
    }

    fun deleteReply(replyId: String) {
        viewModelScope.launch {
            repository.deleteReply(replyId).collectLatest {
                _deleteReplyState.emit(it)
            }
        }
    }

    fun getALLReply() {
        viewModelScope.launch {
            repository.getALLReply(_postId.value).collectLatest {
                _getAllReplyState.emit(it)
            }
        }
    }

}

