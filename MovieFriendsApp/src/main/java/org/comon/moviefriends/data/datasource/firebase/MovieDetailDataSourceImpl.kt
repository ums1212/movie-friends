package org.comon.moviefriends.data.datasource.firebase

import com.google.firebase.Firebase
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.TMDBMovieDetail
import org.comon.moviefriends.data.model.UserInfo
import org.comon.moviefriends.data.model.UserRate
import org.comon.moviefriends.data.model.UserWantMovieInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MovieDetailDataSourceImpl: MovieDetailDataSource {
    private val db = Firebase.firestore

    private suspend fun getWantThisMovieInfo(movieId: Int, userInfo: UserInfo): QuerySnapshot {
        return db.collection("want_movie")
            .whereEqualTo("movieId", movieId)
            .whereEqualTo("userInfo.id", userInfo.id)
            .get()
            .await()
    }

    override suspend fun getStateWantThisMovie(movieId: Int, userInfo: UserInfo) = flow {
        emit(APIResult.Loading)
        val querySnapshot = getWantThisMovieInfo(movieId, userInfo)
        if(querySnapshot.documents.isEmpty()){
            emit(APIResult.Success(false))
        }else{
            emit(APIResult.Success(true))
        }
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override suspend fun changeStateWantThisMovie(movieDetail: TMDBMovieDetail, userInfo: UserInfo) = flow {
        emit(APIResult.Loading)
        val querySnapshot = getWantThisMovieInfo(movieDetail.id, userInfo)
        if(querySnapshot.documents.isEmpty()){
            val wantMovieInfo = UserWantMovieInfo(
                movieId = movieDetail.id,
                moviePosterPath = movieDetail.posterPath,
                userInfo = userInfo,
                userDistance = 0,
                createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
            db.collection("want_movie").add(wantMovieInfo).await()
            emit(APIResult.Success(true))
        }else{
            querySnapshot.first().reference.delete().await()
            emit(APIResult.Success(false))
        }
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override suspend fun voteUserMovieRating(
        movieId: Int,
        userInfo: UserInfo,
        rating: Int
    ): Flow<APIResult<Int>> = flow {
        emit(APIResult.Loading)
        val userRate = UserRate(
            movieId = movieId,
            rate = rating,
            user = userInfo,
        )
        db.collection("user_rate").document("${movieId}_${userInfo.id}").set(userRate).await()
        emit(APIResult.Success(rating))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override suspend fun getAllUserMovieRating(movieId: Int): Flow<APIResult<List<UserRate?>>> = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("user_rate").whereEqualTo("movieId", movieId).get().await()
        emit(APIResult.Success(querySnapshot.documents.map { it.toObject(UserRate::class.java) }))
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override suspend fun getUserMovieRating(
        movieId: Int,
        userInfo: UserInfo
    ): Flow<APIResult<Int>> = flow {
    }

    override suspend fun insertUserReview(
        movieId: Int,
        userInfo: UserInfo,
        review: String
    ): Flow<APIResult<Boolean>> = flow {
    }

    override suspend fun getUserReview(movieId: Int, userInfo: UserInfo): Flow<APIResult<String>> = flow {
    }

}