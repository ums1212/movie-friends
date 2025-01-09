package org.comon.moviefriends.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.tmdb.MovieCategory
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.tmdb.MovieActivityHistory
import org.comon.moviefriends.data.entity.tmdb.ResponseMoviesDto
import org.comon.moviefriends.domain.repo.MovieActivityHistoryRepository
import org.comon.moviefriends.domain.repo.MovieRepository
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor  (
    private val movieRepository: MovieRepository,
    private val movieActivityHistoryRepository: MovieActivityHistoryRepository
): ViewModel() {

    private val _nowList = MutableStateFlow<APIResult<Response<ResponseMoviesDto>>>(APIResult.NoConstructor)

    private val _popList = MutableStateFlow<APIResult<Response<ResponseMoviesDto>>>(APIResult.NoConstructor)

    private val _trendingList = MutableStateFlow<APIResult<Response<ResponseMoviesDto>>>(APIResult.NoConstructor)

    private val _upcomingList = MutableStateFlow<APIResult<Response<ResponseMoviesDto>>>(APIResult.NoConstructor)

    fun getAllMovies() = combine(
        movieRepository.getNowPlaying(),
        movieRepository.getPopular(),
        movieRepository.getTrending(),
        movieRepository.getUpcoming()
    ) { now, pop, trend, up ->
        _nowList.emit(now)
        _popList.emit(pop)
        _trendingList.emit(trend)
        _upcomingList.emit(up)
    }

    fun sendList(category: MovieCategory) = when(category){
        MovieCategory.NOW_PLAYING -> _nowList
        MovieCategory.POPULAR -> _popList
        MovieCategory.TRENDING -> _trendingList
        MovieCategory.UP_COMING -> _upcomingList
    }

    fun insertMovieActivityHistory(movieInfo: ResponseMoviesDto.MovieInfo){
        viewModelScope.launch {
            movieActivityHistoryRepository.insertHistory(
                MovieActivityHistory(
                    movieId = movieInfo.id,
                    movieTitle = movieInfo.title,
                    movieReleaseDate = movieInfo.releaseDate,
                    moviePosterPath = movieInfo.posterPath,
                )
            )
        }
    }

}