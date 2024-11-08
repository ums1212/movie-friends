package org.comon.moviefriends.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.datasource.tmdb.TMDBService
import org.comon.moviefriends.data.model.ResponseCreditDto
import org.comon.moviefriends.data.model.TMDBMovieDetail
import org.comon.moviefriends.data.model.TMDBMovies
import org.comon.moviefriends.data.model.UserInfo
import org.comon.moviefriends.data.model.UserRate
import org.comon.moviefriends.data.model.UserWantMovieInfo
import retrofit2.Response

interface TMDBRepository {

    fun getNowPlaying(): Flow<APIResult<Response<TMDBMovies>>>

    fun getPopular(): Flow<APIResult<Response<TMDBMovies>>>

    fun getTopRated(): Flow<APIResult<Response<TMDBMovies>>>

    fun getUpcoming(): Flow<APIResult<Response<TMDBMovies>>>

    fun getMovieDetail(movieId: Int): Flow<APIResult<Response<TMDBMovieDetail>>>

    fun getMovieCredit(movieId: Int): Flow<APIResult<Response<ResponseCreditDto>>>

    suspend fun getStateWantThisMovie(movieId: Int, userInfo: UserInfo): Flow<APIResult<Boolean>>

    suspend fun changeStateWantThisMovie(movieInfo: TMDBMovieDetail, userInfo: UserInfo): Flow<APIResult<Boolean>>

    suspend fun getUserWantList(movieId: Int): Flow<APIResult<List<UserWantMovieInfo?>>>

    suspend fun voteUserMovieRating(movieId: Int, userInfo: UserInfo, rating: Int): Flow<APIResult<Int>>

    suspend fun getAllUserMovieRating(movieId: Int): Flow<APIResult<List<UserRate?>>>
}