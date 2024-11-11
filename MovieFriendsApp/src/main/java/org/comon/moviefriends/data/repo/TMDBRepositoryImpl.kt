package org.comon.moviefriends.data.repo

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.comon.moviefriends.data.datasource.firebase.MovieDetailDataSource
import org.comon.moviefriends.data.datasource.firebase.MovieDetailDataSourceImpl
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.datasource.tmdb.TMDBService
import org.comon.moviefriends.data.model.tmdb.ResponseMovieDetailDto
import org.comon.moviefriends.data.model.firebase.UserInfo

class TMDBRepositoryImpl(
    private val fs: MovieDetailDataSource = MovieDetailDataSourceImpl()
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

    override fun getTrending() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(TMDBService.getInstance().getTrending()))
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

    override fun getMovieVideo(movieId: Int) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(TMDBService.getInstance().getMovieVideo(movieId)))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override suspend fun getStateWantThisMovie(movieId: Int, userInfo: UserInfo) =
        fs.getStateWantThisMovie(movieId, userInfo)

    override suspend fun changeStateWantThisMovie(
        movieInfo: ResponseMovieDetailDto,
        userInfo: UserInfo,
        nowLocation: List<Double>
    ) = fs.changeStateWantThisMovie(movieInfo, userInfo, nowLocation)

    override suspend fun getUserWantList(movieId: Int, userId: String) =
        fs.getUserWantList(movieId, userId)

    override fun requestWatchTogether(
        movieId: Int,
        sendUser: UserInfo,
        receiveUser: UserInfo
    ) = fs.requestWatchTogether(movieId, sendUser, receiveUser)

    override suspend fun voteUserMovieRating(movieId: Int, userInfo: UserInfo, rating: Int) =
        fs.voteUserMovieRating(movieId, userInfo, rating)

    override suspend fun getAllUserMovieRating(movieId: Int) =
        fs.getAllUserMovieRating(movieId)

    override suspend fun insertUserReview(movieId: Int, userInfo: UserInfo, review: String) =
        fs.insertUserReview(movieId, userInfo, review)

    override suspend fun getUserReview(movieId: Int, userId: String) =
        fs.getUserReview(movieId, userId)
}