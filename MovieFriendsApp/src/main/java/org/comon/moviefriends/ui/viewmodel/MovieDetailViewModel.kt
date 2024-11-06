package org.comon.moviefriends.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.IntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.api.tmdb.APIResult
import org.comon.moviefriends.model.ResponseCreditDto
import org.comon.moviefriends.model.TMDBMovieDetail
import org.comon.moviefriends.model.UserInfo
import org.comon.moviefriends.repo.TMDBRepository
import retrofit2.Response

class MovieDetailViewModel: ViewModel() {

    private val repository = TMDBRepository()

    private val _movieId = MutableStateFlow(0)
    val movieId get() = _movieId.asStateFlow()
    fun getMovieId(movieId: Int) {
        _movieId.value = movieId
    }

    private val _userInfo = MutableStateFlow(UserInfo())
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

    private val _mfAllUserMovieRating = mutableIntStateOf(0)
    val mfAllUserMovieRating: IntState get() = _mfAllUserMovieRating

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

    private val _wantThisMovieState = MutableStateFlow<APIResult<DocumentReference?>>(APIResult.NoConstructor)
    val wantThisMovieState get() = _wantThisMovieState.asStateFlow()

    fun getAllMovieInfo(){
        getMovieDetail()
        getMovieCredit()
        getStateWantThisMovie(movieId.value)
//        getUserWantList(movieId)
//        getUserRate(movieId)
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

    fun changeStateWantThisMovie() {
        viewModelScope.launch {
            repository.changeStateWantThisMovie(movieId.value, userInfo.value).collectLatest { result ->
                when(result){
                    is APIResult.Success -> TODO()
                    else ->_wantThisMovieState.emit(result)
                }

            }
        }
    }

    private fun getStateWantThisMovie(movieId: Int) {
        viewModelScope.launch {
            repository.getStateWantThisMovie(movieId, userInfo.value).collectLatest { result ->
                _wantThisMovieState.emit(result)
            }
        }
    }

    private fun getUserWantList() {

    }

    private fun getUserRate() {

    }

    fun voteUserRate(star: Int) {
        Log.d("voteUserRate", "유저 평점 : $star")
    }

    private fun getUserReview(){

    }

}