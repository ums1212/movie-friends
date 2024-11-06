package org.comon.moviefriends.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.tmdb.MovieCategory
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.TMDBMovies
import org.comon.moviefriends.data.repo.TMDBRepositoryImpl
import retrofit2.Response

class HomeViewModel: ViewModel() {

    private val _nowList = MutableStateFlow<List<TMDBMovies.MovieInfo>>(emptyList())
    val nowList get() = _nowList.asStateFlow()

    private val _popList = MutableStateFlow<List<TMDBMovies.MovieInfo>>(emptyList())
    val popList get() = _popList.asStateFlow()

    private val _topList = MutableStateFlow<List<TMDBMovies.MovieInfo>>(emptyList())
    val topList get() = _topList.asStateFlow()

    private val _upcomingList = MutableStateFlow<List<TMDBMovies.MovieInfo>>(emptyList())
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
        getCategoryMovies(TMDBRepositoryImpl().getNowPlaying(), MovieCategory.NOW_PLAYING)
        getCategoryMovies(TMDBRepositoryImpl().getPopular(), MovieCategory.POPULAR)
        getCategoryMovies(TMDBRepositoryImpl().getTopRated(), MovieCategory.TOP_RATED)
        getCategoryMovies(TMDBRepositoryImpl().getUpcoming(), MovieCategory.UP_COMING)
    }

    private fun getCategoryMovies(flow: Flow<APIResult<Response<TMDBMovies>>>, category: MovieCategory){
        viewModelScope.launch {
            flow.collect { result ->
                when(result){
                    is APIResult.Success -> {
                        result.resultData.body()?.results?.let { addListByCategory(it, category) }
                    }
                    is APIResult.Loading -> {
                        Log.d("getCategoryMovies", "로딩 중")
                    }
                    is APIResult.NetworkError -> {
                        Log.e("getCategoryMovies", "${result.exception}")
                    }
                    else -> {
                        Log.e("getCategoryMovies", "$result")
                    }
                }
            }
        }
    }

    private suspend fun addListByCategory(list: List<TMDBMovies.MovieInfo>, category: MovieCategory){
        when(category){
            MovieCategory.NOW_PLAYING -> _nowList.emit(list)
            MovieCategory.POPULAR -> _popList.emit(list)
            MovieCategory.TOP_RATED -> _topList.emit(list)
            MovieCategory.UP_COMING -> _upcomingList.emit(list)
        }
    }
}