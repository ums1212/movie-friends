package org.comon.moviefriends.data.datasource.firebase

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.data.datasource.lbs.MFLocationService
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.RequestChatInfo
import org.comon.moviefriends.data.model.tmdb.ResponseMovieDetailDto
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.data.model.firebase.UserRate
import org.comon.moviefriends.data.model.firebase.UserWantMovieInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MovieDetailDataSourceImpl: MovieDetailDataSource {
    private val db = Firebase.firestore

    private suspend fun getWantThisMovieInfo(movieId: Int, userInfo: UserInfo)
        = db.collection("want_movie")
            .whereEqualTo("movieId", movieId)
            .whereEqualTo("userInfo.id", userInfo.id)
            .get()
            .await()

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

    override suspend fun changeStateWantThisMovie(movieDetail: ResponseMovieDetailDto, userInfo: UserInfo, nowLocation: List<Double>) = flow {
        emit(APIResult.Loading)
        // 위치 정보 가져오기
        val region = MFLocationService.getInstance()
            .getCurrentRegion(nowLocation[1], nowLocation[0])
            .body()?.documents?.find {
                it.regionType == "B"
            }?.region3depthName

        // 기존 데이터가 있는지 확인하기
        val querySnapshot = getWantThisMovieInfo(movieDetail.id, userInfo)
        if(querySnapshot.documents.isEmpty()){
            val wantMovieInfo = UserWantMovieInfo(
                movieId = movieDetail.id,
                moviePosterPath = movieDetail.posterPath,
                userInfo = userInfo,
                userLocation = region ?: "위치 없음",
                createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
            db.collection("want_movie").add(wantMovieInfo).await()
            emit(APIResult.Success(true))
        }else{
            // 기존 데이터가 있다면 삭제
            querySnapshot.first().reference.delete().await()
            emit(APIResult.Success(false))
        }
    }.catch{
        error -> emit(APIResult.NetworkError(error))
    }

    override suspend fun getUserWantList(movieId: Int) = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("want_movie").whereEqualTo("movieId", movieId).get().await()
        val userWantList = querySnapshot.documents.map { userWant ->
            userWant.toObject(UserWantMovieInfo::class.java)
        }
        emit(APIResult.Success(userWantList))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override fun requestWatchTogether(
        movieId: Int,
        sendUser: UserInfo,
        receiveUser: UserInfo
    ) = runCatching {
        val requestChatInfo = RequestChatInfo(
            movieId = movieId,
            sendUser = sendUser,
            receiveUser = receiveUser,
            proposalFlag = false,
            createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
        db.collection("request_chat")
            .whereEqualTo("movieId", movieId)
            .whereEqualTo("sendUser", sendUser)
            .whereEqualTo("receiveUser", receiveUser).get()
            .addOnSuccessListener { result ->
                if(result.isEmpty){
                    db.collection("request_chat").add(requestChatInfo)
                }else{
                    result.documents.first().reference.delete()
                }
            }
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