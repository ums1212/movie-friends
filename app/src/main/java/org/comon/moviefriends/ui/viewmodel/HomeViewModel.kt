package org.comon.moviefriends.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.common.KakaoSdk.init
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.comon.moviefriends.api.MovieCategory
import org.comon.moviefriends.api.TMDBResult
import org.comon.moviefriends.model.MovieInfo
import org.comon.moviefriends.model.TMDBMovies
import org.comon.moviefriends.repo.TMDBRepository
import retrofit2.Response

class HomeViewModel: ViewModel() {

    private val _nowList = MutableStateFlow<List<MovieInfo>>(emptyList())
    val nowList get() = _nowList.asStateFlow()

    private val _popList = MutableStateFlow<List<MovieInfo>>(emptyList())
    val popList get() = _popList.asStateFlow()

    private val _topList = MutableStateFlow<List<MovieInfo>>(emptyList())
    val topList get() = _topList.asStateFlow()

    private val _upcomingList = MutableStateFlow<List<MovieInfo>>(emptyList())
    val upcomingList get() = _upcomingList.asStateFlow()

    fun sendList(category: MovieCategory) = when(category){
        MovieCategory.NOW_PLAYING -> nowList
        MovieCategory.POPULAR -> popList
        MovieCategory.TOP_RATED -> topList
        MovieCategory.UP_COMING -> upcomingList
    }

    init {
        getAllMovies()
    }

    fun getAllMovies(){
        getCategoryMovies(TMDBRepository().getNowPlaying(), MovieCategory.NOW_PLAYING)
        getCategoryMovies(TMDBRepository().getPopular(), MovieCategory.POPULAR)
        getCategoryMovies(TMDBRepository().getTopRated(), MovieCategory.TOP_RATED)
        getCategoryMovies(TMDBRepository().getUpcoming(), MovieCategory.UP_COMING)
    }

    private fun getCategoryMovies(flow: Flow<TMDBResult<Response<TMDBMovies>>>, category: MovieCategory){
        viewModelScope.launch {
            flow.collect { result ->
                when(result){
                    is TMDBResult.Success -> {
                        result.resultData.body()?.results?.let { addListByCategory(it, category) }
                    }
                    is TMDBResult.Loading -> {
                        Log.d("getCategoryMovies", "로딩 중")
                    }
                    is TMDBResult.NetworkError -> {
                        Log.e("getCategoryMovies", "${result.exception}")
                    }
                    else -> {
                        Log.e("getCategoryMovies", "$result")
                    }
                }
            }
        }
    }

    private suspend fun addListByCategory(list: List<MovieInfo>, category: MovieCategory){
        when(category){
            MovieCategory.NOW_PLAYING -> _nowList.emit(list)
            MovieCategory.POPULAR -> _popList.emit(list)
            MovieCategory.TOP_RATED -> _topList.emit(list)
            MovieCategory.UP_COMING -> _upcomingList.emit(list)
        }
    }
}