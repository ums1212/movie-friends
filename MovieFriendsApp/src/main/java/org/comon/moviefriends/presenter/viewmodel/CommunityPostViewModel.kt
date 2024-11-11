package org.comon.moviefriends.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.repo.CommunityPostRepository
import org.comon.moviefriends.data.repo.CommunityPostRepositoryImpl

class CommunityPostViewModel(
    private val repository: CommunityPostRepository = CommunityPostRepositoryImpl()
): ViewModel() {

    private val _insertState = MutableStateFlow<APIResult<Boolean>>(APIResult.NoConstructor)
    val insertState = _insertState.asStateFlow()

    private val _updateState = MutableStateFlow<APIResult<Boolean>>(APIResult.NoConstructor)
    val updateState = _updateState.asStateFlow()

    private val _deleteState = MutableStateFlow<APIResult<Boolean>>(APIResult.NoConstructor)
    val deleteState = _deleteState.asStateFlow()

    private val _getPostState = MutableStateFlow<APIResult<Boolean>>(APIResult.NoConstructor)
    val getPostState = _getPostState.asStateFlow()

    private val _getAllPostState = MutableStateFlow<APIResult<Boolean>>(APIResult.NoConstructor)
    val getAllPostState = _getAllPostState.asStateFlow()

    fun insertPost() {
        viewModelScope.launch {
//            repository.insertPost()
        }
    }

    fun updatePost() {
        viewModelScope.launch {
//            repository.updatePost()
        }
    }


    fun deletePost() {
        viewModelScope.launch {
//            repository.deletePost(postId)
        }
    }


    fun getPost() {
        viewModelScope.launch {
//            repository.getPost(postId)
        }
    }

    fun getALLPost() {
        viewModelScope.launch {
//            repository.getALLPost()
        }
    }
}

