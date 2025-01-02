package org.comon.moviefriends.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.lbs.MFLocationManager
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.firebase.RequestChatInfo
import org.comon.moviefriends.data.entity.tmdb.ResponseCreditDto
import org.comon.moviefriends.data.entity.tmdb.ResponseMovieDetailDto
import org.comon.moviefriends.data.entity.firebase.UserInfo
import org.comon.moviefriends.data.entity.firebase.UserReview
import org.comon.moviefriends.data.entity.firebase.UserWantMovieInfo
import org.comon.moviefriends.data.entity.tmdb.ResponseMovieVideoDto
import org.comon.moviefriends.domain.repo.ChatRepository
import org.comon.moviefriends.domain.repo.MovieRepository
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor (
    private val movieRepository: MovieRepository,
    private val chatRepository: ChatRepository,
    private val locationManager: MFLocationManager
): ViewModel() {

    private val _movieId = MutableStateFlow(0)
    val movieId get() = _movieId.asStateFlow()
    fun getMovieId(movieId: Int) {
        _movieId.value = movieId
    }

    private val _userInfo: MutableStateFlow<UserInfo?> = MutableStateFlow(null)
    val userInfo get() = _userInfo.asStateFlow()
    fun getUserInfo(userInfo: UserInfo?) {
        _userInfo.value = userInfo
    }

    private val _movieDetail = MutableStateFlow<APIResult<Response<ResponseMovieDetailDto>>>(APIResult.NoConstructor)
    val movieDetail get() = _movieDetail.asStateFlow()

    private val _movieCredit = MutableStateFlow<APIResult<Response<ResponseCreditDto>>>(APIResult.NoConstructor)
    val movieCredit get() = _movieCredit.asStateFlow()

    private val _movieVideo = MutableStateFlow<APIResult<Response<ResponseMovieVideoDto>>>(APIResult.NoConstructor)
    val movieVideo get() = _movieVideo.asStateFlow()

    val userMovieRatingState = mutableIntStateOf(0)
    var userMovieRatingErrorMessage by mutableStateOf<UserRateState>(UserRateState.NoConstructor)
    fun resetUserMovieRatingErrorMessage(){
        userMovieRatingErrorMessage = UserRateState.NoConstructor
    }
    val mfAllUserMovieRatingState = mutableIntStateOf(0)

    private val _rateModalState = MutableStateFlow(false)
    val rateModalState get() = _rateModalState.asStateFlow()
    fun toggleRateModalState(
        navigateToLogin: () -> Unit
    ) {
        if(userInfo.value==null){
            navigateToLogin()
            return
        }
        viewModelScope.launch {
            _rateModalState.emit(!_rateModalState.value)
            if(!_rateModalState.value){
                getAllUserRate()
            }
        }
    }

    private val _userReviews = MutableStateFlow<APIResult<List<UserReview?>>>(APIResult.NoConstructor)
    val userReviews = _userReviews.asStateFlow()

    private val _reviewBottomSheetState = MutableStateFlow(false)
    val reviewBottomSheetState get() = _reviewBottomSheetState.asStateFlow()
    fun toggleReviewBottomSheetState() = viewModelScope.launch {
        _reviewBottomSheetState.emit(!_reviewBottomSheetState.value)
    }
    var reviewContent by mutableStateOf("")
        private set
    var reviewHasErrors by mutableStateOf(false)
        private set
    fun updateReview(input: String) {
        reviewContent = input
    }

    private val _userWantBottomSheetState = MutableStateFlow(false)
    val userWantBottomSheetState get() = _userWantBottomSheetState.asStateFlow()
    fun toggleUserWantBottomSheetState(
        navigateToLogin: () -> Unit
    ) {
        if(userInfo.value==null){
            navigateToLogin()
            return
        }
        viewModelScope.launch {
            _userWantBottomSheetState.emit(!_userWantBottomSheetState.value)
            // 이 영화를 보고 싶은 사람 새로고침
            getUserWantList()
        }
    }

    private val _wantThisMovieState = MutableStateFlow<APIResult<Boolean>>(APIResult.NoConstructor)
    val wantThisMovieState get() = _wantThisMovieState.asStateFlow()
    fun changeWantThisMovieStateLoading(){
        _wantThisMovieState.value = APIResult.Loading
    }

    private val _userWantList = MutableStateFlow<List<UserWantMovieInfo?>>(emptyList())
    val userWantList get() = _userWantList.asStateFlow()
    val userWantListState = mutableStateOf(false)

    private val _movieInfo = MutableStateFlow<ResponseMovieDetailDto?>(null)
    val movieInfo get() = _movieInfo.asStateFlow()
    fun setMovieInfo(movieInfo: ResponseMovieDetailDto) {
        _movieInfo.value = movieInfo
    }

    fun getAllMovieInfo(){
        getMovieDetail()
        getMovieCredit()
        getMovieVideo()
        getStateWantThisMovie()
        getAllUserRate()
        getUserWantList()
        getUserReview()
    }

    private fun getMovieDetail() = viewModelScope.launch {
        movieRepository.getMovieDetail(movieId.value).collectLatest {
            _movieDetail.emit(it)
        }
    }

    private fun getMovieCredit() = viewModelScope.launch {
        movieRepository.getMovieCredit(movieId.value).collectLatest {
            _movieCredit.emit(it)
        }
    }

    private fun getMovieVideo() = viewModelScope.launch {
        movieRepository.getMovieVideo(movieId.value).collectLatest {
            _movieVideo.emit(it)
        }
    }

    fun changeStateWantThisMovie(
        context: Context,
        navigateToLogin: () -> Unit
    ) {
        viewModelScope.launch {
            if(_userInfo.value==null) {
                navigateToLogin()
                return@launch
            }
            locationManager.getCurrentLocation(context).collectLatest { result ->
                when(result){
                    is APIResult.Success -> {
                        _movieInfo.value?.let {
                            movieRepository.changeStateWantThisMovie(it, _userInfo.value!!, result.resultData).collectLatest { result ->
                                _wantThisMovieState.emit(result)
                            }
                        }
                    }
                    is APIResult.NetworkError -> {

                    }
                    else -> {}
                }
            }

        }
    }

    private fun getStateWantThisMovie() {
        if(userInfo.value == null) return
        viewModelScope.launch {
            movieRepository.getStateWantThisMovie(_movieId.value, _userInfo.value!!).collectLatest { result ->
                _wantThisMovieState.emit(result)
            }
        }
    }

    fun getUserWantList() = viewModelScope.launch {
        movieRepository.getUserWantList(_movieId.value, _userInfo.value?.id ?: "").collectLatest { result ->
            when(result){
                is APIResult.Success -> {
                    userWantListState.value = true
                    _userWantList.emit(result.resultData)
                }
                is APIResult.NetworkError -> {
                    Log.e("getUserWantList", "${result.exception}")
                    userWantListState.value = false
                }
                else -> {
                    userWantListState.value = false
                }
            }
        }
    }

    suspend fun requestWatchTogether(requestChatInfo: RequestChatInfo) =
        chatRepository.requestWatchTogether(requestChatInfo)

    private fun getAllUserRate() = viewModelScope.launch {
        movieRepository.getAllUserMovieRating(_movieId.value).collectLatest {
            when(it){
                is APIResult.Success -> {
                    mfAllUserMovieRatingState.intValue = it.resultData
                }
                else -> {}
            }
        }
    }

    fun voteUserRate(
        star: Int,
        navigateToLogin: () -> Unit
    ) {
        if(userInfo.value == null) {
            navigateToLogin()
            return
        }

        if(userMovieRatingState.intValue == 0){
            userMovieRatingErrorMessage = UserRateState.NotVote
            return
        }
        userMovieRatingErrorMessage = UserRateState.NoConstructor

        viewModelScope.launch {
            movieRepository.voteUserMovieRating(_movieId.value, _userInfo.value!!, star).collectLatest {
                when(it){
                    is APIResult.Success -> {
                        toggleRateModalState(navigateToLogin)
                        userMovieRatingState.intValue = star
                        userMovieRatingErrorMessage = UserRateState.NoConstructor
                    }
                    is APIResult.NetworkError -> {
                        userMovieRatingErrorMessage = UserRateState.Error
                    }
                    is APIResult.Loading -> {
                        userMovieRatingErrorMessage = UserRateState.Loading
                    }
                    else -> {}
                }
            }
        }
    }

    fun insertUserReview(){
        if(reviewContent.isEmpty()){
            reviewHasErrors = true
            return
        }else{
            reviewHasErrors = false
        }
        viewModelScope.launch {
            _userInfo.value?.let {
                movieRepository.insertUserReview(_movieId.value, it, reviewContent).collectLatest { result ->
                    when(result){
                        is APIResult.Success -> {
                            reviewContent = ""
                            if(result.resultData){
                                getUserReview()
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    fun deleteUserReview(reviewId: String){
        viewModelScope.launch {
            movieRepository.deleteUserReview(reviewId).collectLatest { result ->
                when(result){
                    is APIResult.Success -> {
                        if(result.resultData){
                            getUserReview()
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun getUserReview(){
        viewModelScope.launch {
            movieRepository.getUserReview(_movieId.value, _userInfo.value?.id ?: "").collectLatest {
                _userReviews.emit(it)
            }
        }
    }

}

sealed class UserRateState {
    data object NoConstructor : UserRateState()
    data object Loading : UserRateState()
    data object NotVote : UserRateState()
    data object Error : UserRateState()
}