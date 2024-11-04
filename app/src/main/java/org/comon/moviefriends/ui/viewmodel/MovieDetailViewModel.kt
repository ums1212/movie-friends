package org.comon.moviefriends.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.api.TMDBResult
import org.comon.moviefriends.model.ResponseCreditDto
import org.comon.moviefriends.model.TMDBMovieDetail
import org.comon.moviefriends.repo.TMDBRepository
import retrofit2.Response

class MovieDetailViewModel(): ViewModel() {

    private val repository = TMDBRepository()

    private val _movieDetail = MutableStateFlow<TMDBResult<Response<TMDBMovieDetail>>>(TMDBResult.NoConstructor)
    val movieDetail get() = _movieDetail.asStateFlow()

    private val _movieCredit = MutableStateFlow<TMDBResult<Response<ResponseCreditDto>>>(TMDBResult.NoConstructor)
    val movieCredit get() = _movieCredit.asStateFlow()

    fun getAllMovieInfo(movieId: Int){
        getMovieDetail(movieId)
        getMovieCredit(movieId)
    }

    fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            repository.getMovieDetail(movieId).collectLatest {
                _movieDetail.emit(it)
            }
        }
    }

    fun getMovieCredit(movieId: Int) {
        viewModelScope.launch {
            repository.getMovieCredit(movieId).collectLatest {
                Log.d("test1234", "${it}")
                _movieCredit.emit(it)
            }
        }
    }

    fun checkWantThisMovie(movieId: Int, userId: String) {

    }

    fun getUserWantList(movieId: Int) {

    }

    fun getUserRate(movieId: Int) {

    }

    fun voteUserRate(star:Int, movieId: Int) {

    }

    fun getUserReview(movieId: Int){

    }

}