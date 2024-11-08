package org.comon.moviefriends.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.comon.moviefriends.data.datasource.firebase.MovieDetailDataSourceImpl
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.datasource.tmdb.TMDBService
import org.comon.moviefriends.data.model.TMDBMovieDetail
import org.comon.moviefriends.data.model.UserInfo
import org.comon.moviefriends.data.model.UserWantMovieInfo

class TMDBRepositoryImpl(
    private val fs: MovieDetailDataSourceImpl = MovieDetailDataSourceImpl()
): TMDBRepository {

    override fun getNowPlaying() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(TMDBService.getInstance().getNowPlaying()))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override fun getPopular() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(TMDBService.getInstance().getPopular()))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override fun getTopRated() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(TMDBService.getInstance().getTopRated()))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override fun getUpcoming() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(TMDBService.getInstance().getUpcoming()))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override fun getMovieDetail(movieId: Int) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(TMDBService.getInstance().getMovieDetail(movieId)))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override fun getMovieCredit(movieId: Int) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(TMDBService.getInstance().getMovieCredit(movieId)))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override suspend fun getStateWantThisMovie(movieId: Int, userInfo: UserInfo) =
        fs.getStateWantThisMovie(movieId, userInfo)

    override suspend fun changeStateWantThisMovie(movieInfo: TMDBMovieDetail, userInfo: UserInfo) =
        fs.changeStateWantThisMovie(movieInfo, userInfo)

    override suspend fun getUserWantList(movieId: Int) =
        fs.getUserWantList(movieId)

    override suspend fun voteUserMovieRating(movieId: Int, userInfo: UserInfo, rating: Int) =
        fs.voteUserMovieRating(movieId, userInfo, rating)

    override suspend fun getAllUserMovieRating(movieId: Int) =
        fs.getAllUserMovieRating(movieId)
}