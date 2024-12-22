package org.comon.moviefriends.presenter.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.LikeInfo
import org.comon.moviefriends.data.model.firebase.PostInfo
import org.comon.moviefriends.data.model.firebase.ReplyInfo
import org.comon.moviefriends.domain.repo.PostRepository
import javax.inject.Inject

@HiltViewModel
class CommunityPostViewModel @Inject constructor (
    private val repository: PostRepository
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

    var postCategory = MutableStateFlow("")
    var postCategoryState = MutableStateFlow(false)

    var postTitle = MutableStateFlow("")
    fun setPostTitle(title: String){
        postTitle.value = title
    }
    var postTitleState = MutableStateFlow(false)

    var postContent = MutableStateFlow("")
    fun setPostContent(content: String){
        postContent.value = content
    }
    var postContentState = MutableStateFlow(false)

    var postImageUrl = MutableStateFlow("")

    private fun setPostInfo(postInfo: PostInfo){
        postCategory.value = postInfo.category
        postTitle.value = postInfo.title
        postContent.value = postInfo.content
        postImageUrl.value = postInfo.imageLink
    }

    private fun checkPostValidation(): Boolean {
        if(postCategory.value.isEmpty()){
            postCategoryState.value = true
        }
        if(postTitle.value.isEmpty()){
            postTitleState.value = true
        }
        if(postContent.value.isEmpty()){
            postContentState.value = true
        }
        return postCategoryState.value || postTitleState.value || postContentState.value
    }
    private fun resetPostValidation(){
        postCategoryState.value = false
        postTitleState.value = false
        postContentState.value = false
    }

    fun insertPost(
        navigateToPostDetail: (String) -> Unit,
    ) {
        resetPostValidation()
        if(checkPostValidation()) return
        viewModelScope.launch {
            if(_user != null){
                val post = PostInfo(
                    user = _user,
                    id = _postId.value,
                    category = postCategory.value,
                    title = postTitle.value,
                    content = postContent.value,
                    imageLink = postImageUrl.value
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
                    category = postCategory.value,
                    title = postTitle.value,
                    content = postContent.value,
                    imageLink = postImageUrl.value
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
                            setPostInfo(it)
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

    fun insertReply(reply: String) {
        viewModelScope.launch {
            val replyInfo = ReplyInfo(
                postId = _postId.value,
                user = _user?: return@launch,
                content = reply,
            )
            repository.insertReply(_postId.value, replyInfo).collectLatest {
                _insertReplyState.emit(it)
            }
        }
    }

    fun deleteReply(replyId: String) {
        viewModelScope.launch {
            repository.deleteReply(_postId.value, replyId).collectLatest {
                _deleteReplyState.emit(it)
            }
        }
    }

    fun getReplyList() {
        viewModelScope.launch {
            repository.getReplyList(_postId.value).collectLatest { result ->
                when(result){
                    APIResult.Loading -> _getAllReplyState.emit(APIResult.Loading)
                    is APIResult.NetworkError -> _getAllReplyState.emit(result)
                    is APIResult.Success -> {
                        result.resultData.addSnapshotListener { value, error ->
                            if(error!=null) _getAllReplyState.value = APIResult.NetworkError(error)
                            val post = value?.toObject(PostInfo::class.java)
                            post?.reply?.let { reply ->
                                _getAllReplyState.value =APIResult.Success(reply)
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun getALLReply() {
        viewModelScope.launch {
            repository.getALLReply(_postId.value).collectLatest {
//                _getAllReplyState.emit(it)
            }
        }
    }



}

