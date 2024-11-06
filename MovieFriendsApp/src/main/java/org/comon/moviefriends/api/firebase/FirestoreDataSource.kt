package org.comon.moviefriends.api.firebase

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.api.tmdb.APIResult
import org.comon.moviefriends.model.TMDBMovieDetail
import org.comon.moviefriends.model.UserInfo
import org.comon.moviefriends.model.UserWantMovieInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FirestoreDataSource {
    private val db = Firebase.firestore

    suspend fun getStateWantThisMovie(movieId: Int, userInfo: UserInfo) = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("want_movie")
            .whereEqualTo("movieId", movieId)
            .whereEqualTo("userInfo.id", userInfo.id)
            .get()
            .await()
        if(querySnapshot.documents.isEmpty()){
            emit(APIResult.Success(false))
        }else{
            emit(APIResult.Success(true))
        }
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    suspend fun changeStateWantThisMovie(movieDetail: TMDBMovieDetail, userInfo: UserInfo) = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("want_movie")
            .whereEqualTo("movieId", movieDetail.id)
            .whereEqualTo("userInfo.id", userInfo.id)
            .get()
            .await()
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

}