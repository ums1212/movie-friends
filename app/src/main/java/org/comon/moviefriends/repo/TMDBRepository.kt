package org.comon.moviefriends.repo

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.comon.moviefriends.api.TMDBResult
import org.comon.moviefriends.api.TMDBService

class TMDBRepository {

    fun getNowPlaying() = flow {
        emit(TMDBResult.Loading)
        emit(TMDBResult.Success(TMDBService.getInstance().getNowPlaying()))
    }.catch{
            error -> emit(TMDBResult.NetworkError(error))
    }

    fun getPopular() = flow {
        emit(TMDBResult.Loading)
        emit(TMDBResult.Success(TMDBService.getInstance().getPopular()))
    }.catch{
            error -> emit(TMDBResult.NetworkError(error))
    }

    fun getTopRated() = flow {
        emit(TMDBResult.Loading)
        emit(TMDBResult.Success(TMDBService.getInstance().getTopRated()))
    }.catch{
            error -> emit(TMDBResult.NetworkError(error))
    }

    fun getUpcoming() = flow {
        emit(TMDBResult.Loading)
        emit(TMDBResult.Success(TMDBService.getInstance().getUpcoming()))
    }.catch{
            error -> emit(TMDBResult.NetworkError(error))
    }

    fun getMovieDetail(movieId: Int) = flow {
        emit(TMDBResult.Loading)
        emit(TMDBResult.Success(TMDBService.getInstance().getMovieDetail(movieId)))
    }.catch{
            error -> emit(TMDBResult.NetworkError(error))
    }

    fun getMovieCredit(movieId: Int) = flow {
        emit(TMDBResult.Loading)
        emit(TMDBResult.Success(TMDBService.getInstance().getMovieCredit(movieId)))
    }.catch{
            error -> emit(TMDBResult.NetworkError(error))
    }

}