package org.comon.moviefriends.presenter.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.lbs.MFLocationManager
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.RequestChatInfo
import org.comon.moviefriends.data.model.tmdb.ResponseCreditDto
import org.comon.moviefriends.data.model.tmdb.ResponseMovieDetailDto
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.data.model.firebase.UserRate
import org.comon.moviefriends.data.model.firebase.UserReview
import org.comon.moviefriends.data.model.firebase.UserWantMovieInfo
import org.comon.moviefriends.data.model.tmdb.ResponseMovieVideoDto
import org.comon.moviefriends.data.repo.TMDBRepository
import org.comon.moviefriends.data.repo.TMDBRepositoryImpl
import retrofit2.Response

class MovieDetailViewModel(
    private val repository: TMDBRepository = TMDBRepositoryImpl()
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
        }
    }

    private val _userReview = MutableStateFlow<APIResult<List<UserReview?>>>(APIResult.NoConstructor)
    val userReview = _userReview.asStateFlow()

    private val _reviewBottomSheetState = MutableStateFlow(false)
    val reviewBottomSheetState get() = _reviewBottomSheetState.asStateFlow()
    fun toggleReviewBottomSheetState() = viewModelScope.launch {
        _reviewBottomSheetState.emit(!_reviewBottomSheetState.value)
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

    private val _allChatRequestCount = MutableStateFlow<APIResult<Map<String, Int>>>(APIResult.NoConstructor)
    val allChatRequestCount = _allChatRequestCount.asStateFlow()

    private val _myChatRequestList = MutableStateFlow<APIResult<List<RequestChatInfo?>>>(APIResult.NoConstructor)
    val myChatRequestList = _myChatRequestList.asStateFlow()

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
        repository.getMovieDetail(movieId.value).collectLatest {
            _movieDetail.emit(it)
        }
    }

    private fun getMovieCredit() = viewModelScope.launch {
        repository.getMovieCredit(movieId.value).collectLatest {
            _movieCredit.emit(it)
        }
    }

    private fun getMovieVideo() = viewModelScope.launch {
        repository.getMovieVideo(movieId.value).collectLatest {
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
            MFLocationManager().getCurrentLocation(context).collectLatest { result ->
                when(result){
                    is APIResult.Success -> {
                        _movieInfo.value?.let {
                            repository.changeStateWantThisMovie(it, _userInfo.value!!, result.resultData).collectLatest { result ->
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
            repository.getStateWantThisMovie(_movieId.value, _userInfo.value!!).collectLatest { result ->
                _wantThisMovieState.emit(result)
            }
        }
    }

    private fun getUserWantList() = viewModelScope.launch {
        repository.getUserWantList(_movieId.value, _userInfo.value?.id ?: "").collectLatest { result ->
            when(result){
                is APIResult.Success -> {
                    userWantListState.value = true
                    _userWantList.emit(result.resultData)
                }
                is APIResult.NetworkError -> {
                    Log.e("getUserWantList", "${result.exception}")
                }
                else -> {
                    userWantListState.value = false
                }
            }
        }
    }

    private val _allUserWantList = MutableStateFlow<APIResult<List<UserWantMovieInfo?>>>(APIResult.NoConstructor)
    val allUserWantList = _allUserWantList.asStateFlow()

    fun getAllUserWantList() = viewModelScope.launch {
        repository.getAllUserWantList(_userInfo.value?.id ?: "").collectLatest {
            _allUserWantList.emit(it)
        }
    }

    fun getMyRequestList() = viewModelScope.launch {
        repository.getMyRequestList(_userInfo.value?.id ?: "").collectLatest {
            _myChatRequestList.emit(it)
        }
    }

    fun requestWatchTogether(movieId: Int, moviePosterPath: String, receiveUser: UserInfo, receiveUserRegion: String) =
        repository.requestWatchTogether(movieId, moviePosterPath, _userInfo.value!!, receiveUser, receiveUserRegion)

    private fun getAllUserRate() = viewModelScope.launch {
        repository.getAllUserMovieRating(_movieId.value).collectLatest {
            when(it){
                is APIResult.Success -> {
                    mfAllUserMovieRatingState.intValue = getRateAverage(it.resultData)
                }
                else -> {}
            }
        }
    }

    private fun getRateAverage(list: List<UserRate?>): Int {
        var result = 0
        if(list.isNotEmpty()){
            list.forEach { rateInfo ->
                rateInfo?.rate.let { userRate ->
                    result += userRate!!
                }
            }
            result /= list.size
        }
        return result
    }

    fun voteUserRate(
        star: Int,
        navigateToLogin: () -> Unit
    ) {
        if(userInfo.value == null) {
            navigateToLogin()
            return
        }
        viewModelScope.launch {
            repository.voteUserMovieRating(_movieId.value, _userInfo.value!!, star).collectLatest {
                when(it){
                    is APIResult.Success -> {
                        userMovieRatingState.intValue = it.resultData
                        getAllUserRate()
                    }
                    else -> {}
                }
            }
        }
    }

    fun insertUserReview(review: String){
        viewModelScope.launch {
            _userInfo.value?.let {
                repository.insertUserReview(_movieId.value, it, review).collectLatest { result ->
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
    }

    fun deleteUserReview(reviewId: String){
        viewModelScope.launch {
            repository.deleteUserReview(reviewId).collectLatest { result ->
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
            repository.getUserReview(_movieId.value, _userInfo.value?.id ?: "").collectLatest {
                _userReview.emit(it)
            }
        }
    }

    fun getAllWantMovieRequest(){
        viewModelScope.launch {
            repository.getAllChatRequestCount(_userInfo.value?.id ?: "").collectLatest {
                _allChatRequestCount.emit(it)
            }
        }
    }

}