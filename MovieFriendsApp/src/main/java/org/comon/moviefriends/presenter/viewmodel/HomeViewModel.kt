package org.comon.moviefriends.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.tmdb.MovieCategory
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.tmdb.ResponseMoviesDto
import org.comon.moviefriends.domain.repo.MovieRepository
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor  (
    private val repository: MovieRepository
): ViewModel() {

    private val _nowList = MutableStateFlow<List<ResponseMoviesDto.MovieInfo>>(emptyList())
    val nowList get() = _nowList.asStateFlow()

    private val _popList = MutableStateFlow<List<ResponseMoviesDto.MovieInfo>>(emptyList())
    val popList get() = _popList.asStateFlow()

    private val _trendingList = MutableStateFlow<List<ResponseMoviesDto.MovieInfo>>(emptyList())
    val trendingList get() = _trendingList.asStateFlow()

    private val _upcomingList = MutableStateFlow<List<ResponseMoviesDto.MovieInfo>>(emptyList())
    val upcomingList get() = _upcomingList.asStateFlow()

    fun sendList(category: MovieCategory) = when(category){
        MovieCategory.NOW_PLAYING -> nowList
        MovieCategory.POPULAR -> popList
        MovieCategory.TRENDING -> trendingList
        MovieCategory.UP_COMING -> upcomingList
    }

    fun getAllMovies(){
        getCategoryMovies(repository.getNowPlaying(), MovieCategory.NOW_PLAYING)
        getCategoryMovies(repository.getPopular(), MovieCategory.POPULAR)
        getCategoryMovies(repository.getTrending(), MovieCategory.TRENDING)
        getCategoryMovies(repository.getUpcoming(), MovieCategory.UP_COMING)
    }

    private fun getCategoryMovies(flow: Flow<APIResult<Response<ResponseMoviesDto>>>, category: MovieCategory){
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

    private suspend fun addListByCategory(list: List<ResponseMoviesDto.MovieInfo>, category: MovieCategory){
        when(category){
            MovieCategory.NOW_PLAYING -> _nowList.emit(list)
            MovieCategory.POPULAR -> _popList.emit(list)
            MovieCategory.TRENDING -> _trendingList.emit(list)
            MovieCategory.UP_COMING -> _upcomingList.emit(list)
        }
    }
}