package org.comon.moviefriends.data.datasource.firebase

import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.tmdb.ResponseMovieDetailDto
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.data.model.firebase.UserRate
import org.comon.moviefriends.data.model.firebase.UserWantMovieInfo

interface MovieDetailDataSource {
    suspend fun getStateWantThisMovie(movieId: Int, userInfo: UserInfo): Flow<APIResult<Boolean>>
    suspend fun changeStateWantThisMovie(movieDetail: ResponseMovieDetailDto, userInfo: UserInfo, nowLocation: List<Double>): Flow<APIResult<Boolean>>
    suspend fun getUserWantList(movieId: Int): Flow<APIResult<List<UserWantMovieInfo?>>>
    suspend fun voteUserMovieRating(movieId: Int, userInfo: UserInfo, rating: Int): Flow<APIResult<Int>>
    suspend fun getAllUserMovieRating(movieId: Int): Flow<APIResult<List<UserRate?>>>
    suspend fun getUserMovieRating(movieId: Int, userInfo: UserInfo): Flow<APIResult<Int>>
    suspend fun insertUserReview(movieId: Int, userInfo: UserInfo, review: String): Flow<APIResult<Boolean>>
    suspend fun getUserReview(movieId: Int, userInfo: UserInfo): Flow<APIResult<String>>
}