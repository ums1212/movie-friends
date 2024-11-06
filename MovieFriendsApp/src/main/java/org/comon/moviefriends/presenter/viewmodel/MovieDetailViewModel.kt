package org.comon.moviefriends.presenter.viewmodel

import android.util.Log
import androidx.compose.runtime.IntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.ResponseCreditDto
import org.comon.moviefriends.data.model.TMDBMovieDetail
import org.comon.moviefriends.data.model.UserInfo
import org.comon.moviefriends.data.model.UserRate
import org.comon.moviefriends.data.repo.TMDBRepositoryImpl
import retrofit2.Response

class MovieDetailViewModel(
    private val repository: TMDBRepositoryImpl = TMDBRepositoryImpl()
): ViewModel() {

    private val _movieId = MutableStateFlow(0)
    val movieId get() = _movieId.asStateFlow()
    fun getMovieId(movieId: Int) {
        _movieId.value = movieId
    }

    private val _userInfo = MutableStateFlow(UserInfo(joinType = "", nickName = "", profileImage = ""))
    val userInfo get() = _userInfo.asStateFlow()
    fun getUserInfo(userInfo: UserInfo) {
        _userInfo.value = userInfo
    }

    private val _movieDetail = MutableStateFlow<APIResult<Response<TMDBMovieDetail>>>(APIResult.NoConstructor)
    val movieDetail get() = _movieDetail.asStateFlow()

    private val _movieCredit = MutableStateFlow<APIResult<Response<ResponseCreditDto>>>(APIResult.NoConstructor)
    val movieCredit get() = _movieCredit.asStateFlow()

    private val _userMovieRating = MutableStateFlow(0)
    val userMovieRating get() = _userMovieRating.asStateFlow()

    private val _mfAllUserMovieRating = MutableStateFlow(0)
    val mfAllUserMovieRating get() = _mfAllUserMovieRating

    private val _rateModalState = MutableStateFlow(false)
    val rateModalState get() = _rateModalState.asStateFlow()
    fun toggleRateModalState() {
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
    fun toggleUserWantBottomSheetState() {
        viewModelScope.launch {
            _userWantBottomSheetState.emit(!_userWantBottomSheetState.value)
        }
    }

    private val _wantThisMovieState = MutableStateFlow<APIResult<Boolean>>(APIResult.NoConstructor)
    val wantThisMovieState get() = _wantThisMovieState.asStateFlow()

    fun getAllMovieInfo(){
        getMovieDetail()
        getMovieCredit()
        getStateWantThisMovie()
        getAllUserRate()
//        getUserWantList(movieId)
//        getUserReview(movieId)
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

    private val _movieInfo: MutableStateFlow<TMDBMovieDetail?> = MutableStateFlow(null)
    val movieInfo get() = _movieInfo.asStateFlow()
    fun setMovieInfo(movieInfo: TMDBMovieDetail) {
        _movieInfo.value = movieInfo
    }

    fun changeStateWantThisMovie() {
        if(_movieInfo.value==null) return

        viewModelScope.launch {
            _movieInfo.value?.let {
                repository.changeStateWantThisMovie(it, _userInfo.value).collectLatest { result ->
                    _wantThisMovieState.emit(result)
                }
            }
        }
    }

    private fun getStateWantThisMovie() {
        viewModelScope.launch {
            repository.getStateWantThisMovie(_movieId.value, _userInfo.value).collectLatest { result ->
                _wantThisMovieState.emit(result)
            }
        }
    }

    private fun getUserWantList() {

    }

    private fun getUserRate() {

    }

    fun getAllUserRate(){
        viewModelScope.launch {
            repository.getAllUserMovieRating(_movieId.value).collectLatest {
                when(it){
                    is APIResult.Success -> {
                        _mfAllUserMovieRating.emit(getRateAverage(it.resultData))
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

    fun voteUserRate(star: Int) {
        viewModelScope.launch {
            repository.voteUserMovieRating(_movieId.value, _userInfo.value, star).collectLatest {
                when(it){
                    is APIResult.Success -> {
                        _userMovieRating.emit(it.resultData)
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