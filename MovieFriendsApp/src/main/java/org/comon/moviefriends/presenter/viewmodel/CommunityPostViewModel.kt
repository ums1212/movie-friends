package org.comon.moviefriends.presenter.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.LikeInfo
import org.comon.moviefriends.data.model.firebase.PostInfo
import org.comon.moviefriends.data.model.firebase.ReplyInfo
import org.comon.moviefriends.data.repo.CommunityPostRepository
import org.comon.moviefriends.data.repo.CommunityPostRepositoryImpl
import org.comon.moviefriends.presenter.viewmodel.uistate.PostUiState

class CommunityPostViewModel(
    private val repository: CommunityPostRepository = CommunityPostRepositoryImpl()
): ViewModel() {

    private val _user = MFPreferences.getUserInfo()

    private val _postId = MutableStateFlow("")
    val postId = _postId.asStateFlow()
    fun setPostId(postId: String) {
        _postId.value = postId
    }

    private val _isUpdate = MutableStateFlow(false)
    val isUpdate = _isUpdate.asStateFlow()
    fun setIsUpdate(isUpdate: Boolean) {
        _isUpdate.value = isUpdate
    }

    private val _isLoading = mutableStateOf(false)
    val isLoading get() = _isLoading

    private val _errorState = MutableStateFlow(false)
    val errorState get() = _errorState.asStateFlow()

    private val _postState = MutableStateFlow<APIResult<PostInfo?>>(APIResult.NoConstructor)
    val postState get() = _postState.asStateFlow()

    private val _uploadImageState = MutableStateFlow<APIResult<Boolean>>(APIResult.NoConstructor)
    val uploadImageState get() = _uploadImageState.asStateFlow()

    private val _updateState = MutableStateFlow<APIResult<String>>(APIResult.NoConstructor)
    val updateState get() = _updateState.asStateFlow()

    private val _deleteState = MutableStateFlow<APIResult<Boolean>>(APIResult.NoConstructor)
    val deleteState get() = _deleteState.asStateFlow()

    private val _postLikeState = MutableStateFlow<APIResult<LikeInfo>>(APIResult.NoConstructor)
    val postLikeState get() = _postLikeState.asStateFlow()

    private val _getAllPostState = MutableStateFlow<APIResult<List<PostInfo?>>>(APIResult.NoConstructor)
    val getAllPostState get() = _getAllPostState.asStateFlow()

    private val _insertReplyState = MutableStateFlow<APIResult<Boolean>>(APIResult.NoConstructor)
    val insertReplyState get() = _insertReplyState.asStateFlow()

    private val _deleteReplyState = MutableStateFlow<APIResult<Boolean>>(APIResult.NoConstructor)
    val deleteReplyState get() = _deleteReplyState.asStateFlow()

    private val _getAllReplyState = MutableStateFlow<APIResult<List<ReplyInfo?>>>(APIResult.NoConstructor)
    val getAllReplyState get() = _getAllReplyState.asStateFlow()

    val postUiState = PostUiState()
    private fun setPostUiState(postInfo: PostInfo) {
        postUiState.postTitle.value = postInfo.title
        postUiState.postContent.value = postInfo.content
        postUiState.postCategory.value = postInfo.category
        postUiState.postImageUrI.value = postInfo.imageLink
    }

    fun insertPost(
        navigateToPostDetail: (String) -> Unit,
    ) {
        viewModelScope.launch {
            if(_user != null){
                val post = PostInfo(
                    user = _user,
                    id = _postId.value,
                    category = postUiState.postCategory.value,
                    title = postUiState.postTitle.value,
                    content = postUiState.postContent.value,
                    imageLink = postUiState.postImageUrI.value
                )
                repository.insertPost(post).collectLatest { result ->
                    when(result){
                        APIResult.Loading -> isLoading.value = true
                        is APIResult.NetworkError -> {
                            _isLoading.value = false
                            _errorState.emit(true)
                        }
                        is APIResult.Success -> {
                            _isLoading.value = false
                            navigateToPostDetail(_postId.value)
                        }
                        else -> isLoading.value = false
                    }
                }
            }
        }
    }

    fun uploadImage(){
        viewModelScope.launch {
            val imageUri = Uri.EMPTY
            val fileName = ""
            repository.uploadImage(imageUri, fileName).collectLatest {
                _uploadImageState.emit(it)
            }
        }
    }

    fun updatePost() {
        viewModelScope.launch {
            if(_user != null){
                val post = PostInfo(
                    user = _user,
                    id = _postId.value,
                    category = postUiState.postCategory.value,
                    title = postUiState.postTitle.value,
                    content = postUiState.postContent.value,
                    imageLink = postUiState.postImageUrI.value
                )
                repository.updatePost(post).collectLatest {
                    _updateState.emit(it)
                }
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
            repository.getPost(_postId.value).collectLatest { result ->
                _postState.emit(result)
                when(result){
                    APIResult.Loading -> isLoading.value = true
                    is APIResult.NetworkError -> {
                        isLoading.value = false
                        _errorState.emit(true)
                    }
                    is APIResult.Success -> {
                        isLoading.value = false
                        result.resultData?.let {
                            setPostUiState(it)
                        }
                    }
                    else -> {
                        isLoading.value = false
                    }
                }
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

    fun addViewCount() {
        viewModelScope.launch {
            repository.addViewCount(_postId.value)
        }
    }

    fun getPostLikeState() {
        viewModelScope.launch {
            repository.getPostLikeState(_postId.value, _user?.id ?: "").collectLatest {
                _postLikeState.emit(it)
            }
        }
    }

    fun changePostLikeState() {
        viewModelScope.launch {
            if(_user == null){
                _postLikeState.emit(APIResult.NetworkError(Exception("로그인 후 이용해주세요")))
            }else{
                repository.changePostLikeState(_postId.value, _user).collectLatest {
                    _postLikeState.emit(it)
                }
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

