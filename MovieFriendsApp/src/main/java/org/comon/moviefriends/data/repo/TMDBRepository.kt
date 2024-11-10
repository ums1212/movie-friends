package org.comon.moviefriends.data.repo

import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.tmdb.ResponseCreditDto
import org.comon.moviefriends.data.model.tmdb.ResponseMovieDetailDto
import org.comon.moviefriends.data.model.tmdb.ResponseMoviesDto
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.data.model.firebase.UserRate
import org.comon.moviefriends.data.model.firebase.UserWantMovieInfo
import retrofit2.Response

interface TMDBRepository {

    fun getNowPlaying(): Flow<APIResult<Response<ResponseMoviesDto>>>

    fun getPopular(): Flow<APIResult<Response<ResponseMoviesDto>>>

    fun getTopRated(): Flow<APIResult<Response<ResponseMoviesDto>>>

    fun getUpcoming(): Flow<APIResult<Response<ResponseMoviesDto>>>

    fun getMovieDetail(movieId: Int): Flow<APIResult<Response<ResponseMovieDetailDto>>>

    fun getMovieCredit(movieId: Int): Flow<APIResult<Response<ResponseCreditDto>>>

    suspend fun getStateWantThisMovie(movieId: Int, userInfo: UserInfo): Flow<APIResult<Boolean>>

    suspend fun changeStateWantThisMovie(movieInfo: ResponseMovieDetailDto, userInfo: UserInfo, nowLocation: List<Double>): Flow<APIResult<Boolean>>

    suspend fun getUserWantList(movieId: Int): Flow<APIResult<List<UserWantMovieInfo?>>>

    suspend fun voteUserMovieRating(movieId: Int, userInfo: UserInfo, rating: Int): Flow<APIResult<Int>>

    suspend fun getAllUserMovieRating(movieId: Int): Flow<APIResult<List<UserRate?>>>
}