package org.comon.moviefriends.data.repo

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.comon.moviefriends.data.datasource.firebase.MovieDetailDataSource
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.datasource.tmdb.TMDBService
import org.comon.moviefriends.data.model.firebase.RequestChatInfo
import org.comon.moviefriends.data.model.tmdb.ResponseMovieDetailDto
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.domain.repo.TMDBRepository
import javax.inject.Inject

class TMDBRepositoryImpl @Inject constructor (
    private val fs: MovieDetailDataSource,
    private val rest: TMDBService,
): TMDBRepository {

    override fun getNowPlaying() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(rest.getNowPlaying()))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override fun getPopular() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(rest.getPopular()))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override fun getTrending() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(rest.getTrending()))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override fun getUpcoming() = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(rest.getUpcoming()))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override fun getMovieDetail(movieId: Int) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(rest.getMovieDetail(movieId)))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override fun getMovieCredit(movieId: Int) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(rest.getMovieCredit(movieId)))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override fun getMovieVideo(movieId: Int) = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(rest.getMovieVideo(movieId)))
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

    override suspend fun getAllUserWantList(userId: String) =
        fs.getAllUserWantList(userId)

    override suspend fun getMyRequestList(userId: String) =
        fs.getMyRequestList(userId)

    override suspend fun getMyReceiveList(userId: String) =
        fs.getMyReceiveList(userId)

    override fun requestWatchTogether(
        requestChatInfo: RequestChatInfo
    ) = fs.requestWatchTogether(requestChatInfo)

    override suspend fun voteUserMovieRating(movieId: Int, userInfo: UserInfo, rating: Int) =
        fs.voteUserMovieRating(movieId, userInfo, rating)

    override suspend fun getAllUserMovieRating(movieId: Int) =
        fs.getAllUserMovieRating(movieId)

    override suspend fun insertUserReview(movieId: Int, userInfo: UserInfo, review: String) =
        fs.insertUserReview(movieId, userInfo, review)

    override suspend fun deleteUserReview(reviewId: String) =
        fs.deleteUserReview(reviewId)

    override suspend fun getUserReview(movieId: Int, userId: String) =
        fs.getUserReview(movieId, userId)

    override suspend fun getAllChatRequestCount(userId: String) =
        fs.getAllChatRequestCount(userId)

    override suspend fun confirmRequest(userInfo: UserInfo, requestChatInfo: RequestChatInfo) =
        fs.confirmRequest(userInfo, requestChatInfo)

    override suspend fun denyRequest(requestChatInfo: RequestChatInfo) =
        fs.denyRequest(requestChatInfo)
}