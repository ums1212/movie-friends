package org.comon.moviefriends.presenter.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.tmdb.ResponseCreditDto
import org.comon.moviefriends.data.model.tmdb.ResponseMovieDetailDto
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.data.model.firebase.UserRate
import org.comon.moviefriends.data.model.firebase.UserWantMovieInfo
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

    private val _reviewBottomSheetState = MutableStateFlow(false)
    val reviewBottomSheetState get() = _reviewBottomSheetState.asStateFlow()
    fun toggleReviewBottomSheetState() {
        viewModelScope.launch {
            _reviewBottomSheetState.emit(!_reviewBottomSheetState.value)
        }
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

    fun getAllMovieInfo(){
        getMovieDetail()
        getMovieCredit()
        getStateWantThisMovie()
        getAllUserRate()
        getUserWantList()
//        getUserReview()
    }

    private fun getMovieDetail() {
        viewModelScope.launch {
            repository.getMovieDetail(movieId.value).collectLatest {
                _movieDetail.emit(it)
            }
        }
    }

    private fun getMovieCredit() {
        viewModelScope.launch {
            repository.getMovieCredit(movieId.value).collectLatest {
                _movieCredit.emit(it)
            }
        }
    }

    private val _movieInfo: MutableStateFlow<ResponseMovieDetailDto?> = MutableStateFlow(null)
    val movieInfo get() = _movieInfo.asStateFlow()
    fun setMovieInfo(movieInfo: ResponseMovieDetailDto) {
        _movieInfo.value = movieInfo
    }

    fun changeStateWantThisMovie(
        nowLocation: List<Double>,
        navigateToLogin: () -> Unit
    ) {
        if(_userInfo.value==null) {
            navigateToLogin()
            return
        }
        viewModelScope.launch {
            _movieInfo.value?.let {
                repository.changeStateWantThisMovie(it, _userInfo.value!!, nowLocation).collectLatest { result ->
                    _wantThisMovieState.emit(result)
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

    private fun getUserWantList() {
        viewModelScope.launch {
            repository.getUserWantList(_movieId.value).collectLatest { result ->
                when(result){
                    is APIResult.Success -> {
                        userWantListState.value = true
                        _userWantList.emit(result.resultData)
                    }
                    is APIResult.NetworkError -> {
                        Log.d("test1234", "${result.exception}")
                    }
                    else -> {
                        userWantListState.value = false
                    }
                }
            }
        }
    }

    private fun getAllUserRate(){
        viewModelScope.launch {
            repository.getAllUserMovieRating(_movieId.value).collectLatest {
                when(it){
                    is APIResult.Success -> {
                        mfAllUserMovieRatingState.intValue = getRateAverage(it.resultData)
                    }
                    else -> {}
                }
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

    private fun getUserReview(){

    }

}