package org.comon.moviefriends.repo

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import org.comon.moviefriends.api.firebase.FirestoreDataSource
import org.comon.moviefriends.api.tmdb.APIResult
import org.comon.moviefriends.api.tmdb.TMDBService
import org.comon.moviefriends.model.UserInfo

class TMDBRepository {

    private val fs = FirestoreDataSource()

    fun getNowPlaying() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(TMDBService.getInstance().getNowPlaying()))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    fun getPopular() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(TMDBService.getInstance().getPopular()))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    fun getTopRated() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(TMDBService.getInstance().getTopRated()))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    fun getUpcoming() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(TMDBService.getInstance().getUpcoming()))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    fun getMovieDetail(movieId: Int) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(TMDBService.getInstance().getMovieDetail(movieId)))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    fun getMovieCredit(movieId: Int) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(TMDBService.getInstance().getMovieCredit(movieId)))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    fun getStateWantThisMovie(movieId: Int, userInfo: UserInfo) = flow {
        fs.getStateWantThisMovie(movieId, userInfo).collectLatest {
            emit(it)
        }
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    fun changeStateWantThisMovie(movieId: Int, userInfo: UserInfo) = flow {
        fs.changeStateWantThisMovie(movieId, userInfo).collectLatest {
            emit(it)
        }
    }.catch{
            error -> emit(APIResult.NetworkError(error))
    }

}