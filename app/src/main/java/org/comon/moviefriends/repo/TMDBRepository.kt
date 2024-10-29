package org.comon.moviefriends.repo

import kotlinx.coroutines.flow.flow
import org.comon.moviefriends.api.TMDBResult
import org.comon.moviefriends.api.TMDBService

class TMDBRepository {

    fun getNowPlaying() = flow {
        emit(TMDBResult.Loading)
        emit(TMDBResult.Success(TMDBService.getInstance().getNowPlaying()))
    }

    fun getPopular() = flow {
        emit(TMDBResult.Loading)
        emit(TMDBResult.Success(TMDBService.getInstance().getPopular()))
    }

    fun getTopRated() = flow {
        emit(TMDBResult.Loading)
        emit(TMDBResult.Success(TMDBService.getInstance().getTopRated()))
    }

    fun getUpcoming() = flow {
        emit(TMDBResult.Loading)
        emit(TMDBResult.Success(TMDBService.getInstance().getUpcoming()))
    }

}