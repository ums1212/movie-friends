package org.comon.moviefriends.data.repo

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.tmdb.ResponseCreditDto
import org.comon.moviefriends.data.model.tmdb.ResponseMovieDetailDto
import org.comon.moviefriends.data.model.tmdb.ResponseMoviesDto
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.data.model.firebase.UserRate
import org.comon.moviefriends.data.model.firebase.UserReview
import org.comon.moviefriends.data.model.firebase.UserWantMovieInfo
import org.comon.moviefriends.data.model.tmdb.ResponseMovieVideoDto
import retrofit2.Response

interface TMDBRepository {

    fun getNowPlaying(): Flow<APIResult<Response<ResponseMoviesDto>>>

    fun getPopular(): Flow<APIResult<Response<ResponseMoviesDto>>>

    fun getTrending(): Flow<APIResult<Response<ResponseMoviesDto>>>

    fun getUpcoming(): Flow<APIResult<Response<ResponseMoviesDto>>>

    fun getMovieDetail(movieId: Int): Flow<APIResult<Response<ResponseMovieDetailDto>>>

    fun getMovieCredit(movieId: Int): Flow<APIResult<Response<ResponseCreditDto>>>

    fun getMovieVideo(movieId: Int): Flow<APIResult<Response<ResponseMovieVideoDto>>>

    suspend fun getStateWantThisMovie(movieId: Int, userInfo: UserInfo): Flow<APIResult<Boolean>>

    suspend fun changeStateWantThisMovie(movieInfo: ResponseMovieDetailDto, userInfo: UserInfo, nowLocation: List<Double>): Flow<APIResult<Boolean>>

    suspend fun getUserWantList(movieId: Int, userId: String): Flow<APIResult<List<UserWantMovieInfo?>>>

    fun requestWatchTogether(movieId: Int, sendUser: UserInfo, receiveUser: UserInfo): Result<Task<QuerySnapshot>>

    suspend fun voteUserMovieRating(movieId: Int, userInfo: UserInfo, rating: Int): Flow<APIResult<Int>>

    suspend fun getAllUserMovieRating(movieId: Int): Flow<APIResult<List<UserRate?>>>

    suspend fun insertUserReview(movieId: Int, userInfo: UserInfo, review: String): Flow<APIResult<Boolean>>

    suspend fun getUserReview(movieId: Int, userId: String): Flow<APIResult<List<UserReview?>>>
}