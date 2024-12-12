package org.comon.moviefriends.presenter.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import org.comon.moviefriends.data.datasource.tmdb.MovieCategory
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.tmdb.ResponseMoviesDto
import org.comon.moviefriends.domain.repo.TMDBRepository
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor  (
    private val repository: TMDBRepository
): ViewModel() {

    private val _nowList = MutableStateFlow<APIResult<Response<ResponseMoviesDto>>>(APIResult.NoConstructor)

    private val _popList = MutableStateFlow<APIResult<Response<ResponseMoviesDto>>>(APIResult.NoConstructor)

    private val _trendingList = MutableStateFlow<APIResult<Response<ResponseMoviesDto>>>(APIResult.NoConstructor)

    private val _upcomingList = MutableStateFlow<APIResult<Response<ResponseMoviesDto>>>(APIResult.NoConstructor)

    fun getAllMovies() = combine(
        repository.getNowPlaying(),
        repository.getPopular(),
        repository.getTrending(),
        repository.getUpcoming()
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
}