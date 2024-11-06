package org.comon.moviefriends.api.firebase

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.api.tmdb.APIResult
import org.comon.moviefriends.model.UserInfo
import org.comon.moviefriends.model.UserWantMovieInfo

class FirestoreDataSource {
    private val db = Firebase.firestore

    fun getStateWantThisMovie(movieId: Int, userInfo: UserInfo) = flow {
        Log.d("getStateWantThisMovie", "이 영화를 보고싶다 가져오기")
        emit(APIResult.Loading)
        val querySnapshot = db.collection("want_movie")
            .whereEqualTo("movie_id", movieId)
            .whereEqualTo("user", userInfo)
            .get()
            .await()
        if(querySnapshot.isEmpty){
            emit(APIResult.Success(null))
        }else{
            emit(APIResult.Success(querySnapshot.first().reference))
        }
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    suspend fun changeStateWantThisMovie(movieId: Int, userInfo: UserInfo) = flow {
            getStateWantThisMovie(movieId, userInfo).collectLatest { result ->
                when(result){
                    is APIResult.Success -> {
                        if(result.resultData != null){
                            result.resultData.delete().await()
                            emit(APIResult.Success(false))
                        }else{
                            db.collection("want_movie").add(UserWantMovieInfo()).await()
                            emit(APIResult.Success(true))
                        }
                    }
                    is APIResult.Loading -> {
                        emit(APIResult.Loading)
                    }
                    is APIResult.NetworkError -> {
                        emit(APIResult.NetworkError(result.exception))
                    }
                    else -> {
                        emit(APIResult.NoConstructor)
                    }
                }
            }
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

}