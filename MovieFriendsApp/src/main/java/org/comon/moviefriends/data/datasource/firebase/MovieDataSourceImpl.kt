package org.comon.moviefriends.data.datasource.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.data.model.firebase.UserRate
import org.comon.moviefriends.data.model.firebase.UserReview
import org.comon.moviefriends.data.model.firebase.UserWantMovieInfo
import javax.inject.Inject

class MovieDataSourceImpl @Inject constructor (
    private val fs: FirebaseFirestore,
): MovieDataSource {

    override suspend fun getWantThisMovieInfo(movieId: Int, userInfo: UserInfo): Result<UserWantMovieInfo>
        = runCatching {
            val querySnapshot = fs.collection("want_movie")
                .whereEqualTo("movieId", movieId)
                .whereEqualTo("userInfo.id", userInfo.id)
                .get()
                .await()
            // first()는 해당 querySnapshot 결과가 비어있을 경우 NoSuchElementException를 던짐
            querySnapshot.first().toObject(UserWantMovieInfo::class.java)
        }

    override suspend fun getStateWantThisMovie(movieId: Int, userInfo: UserInfo): Boolean {
        val result = getWantThisMovieInfo(movieId, userInfo)
        try {
            // 해당 객체가 있으면 true를 반환하고 없다면 NoSuchElementException를 받기 때문에 try-catch로 감싸준다.
            result.getOrThrow()
            return true
        }catch (e: NoSuchElementException) {
            return false
        }
    }

    override suspend fun addUserWantMovieInfo(userWantMovieInfo: UserWantMovieInfo): Result<Boolean>
        = runCatching {
            fs.collection("want_movie").add(userWantMovieInfo).await()
            true
        }

    override suspend fun deleteUserWantMovieInfo(movieId: Int, userInfo: UserInfo): Result<Boolean>
        = runCatching {
            fs.collection("want_movie")
                .whereEqualTo("movieId", movieId)
                .whereEqualTo("userInfo.id", userInfo.id)
                .get().await()
                .documents.first().reference.delete().await()
            true
        }

    override suspend fun getUserWantMovieListExceptMe(movieId: Int, userId: String): Result<List<UserWantMovieInfo>>
        = runCatching {
            val querySnapshot = fs.collection("want_movie")
                .whereEqualTo("movieId", movieId)
                .whereNotEqualTo("userInfo.id", userId)
                .get().await()
            querySnapshot.toObjects(UserWantMovieInfo::class.java)
        }

    override suspend fun getAllUserWantListExceptMe(userId: String): Result<List<UserWantMovieInfo>>
        = runCatching {
            val wantQuerySnapshot = fs.collection("want_movie")
                .whereNotEqualTo("userInfo.id", userId)
                .get().await()
            wantQuerySnapshot.toObjects(UserWantMovieInfo::class.java)
        }

    override suspend fun voteUserMovieRating(userRate: UserRate): Result<Boolean>
        = runCatching {
            fs.collection("user_rate").document("${userRate.movieId}_${userRate.user.id}").set(userRate).await()
            true
        }

    override suspend fun getAllUserMovieRating(movieId: Int): Result<List<UserRate>>
        = runCatching {
            val querySnapshot = fs.collection("user_rate")
                .whereEqualTo("movieId", movieId).get().await()
            querySnapshot.toObjects(UserRate::class.java)
        }

    override suspend fun getUserMovieRating(
        movieId: Int,
        userInfo: UserInfo
    ): Result<UserRate>
        = runCatching {
            val querySnapshot = fs.collection("user_rate")
                .whereEqualTo("movieId", movieId)
                .whereEqualTo("user.id", userInfo.id)
                .get().await()
            querySnapshot.first().toObject(UserRate::class.java)
        }

    override suspend fun insertUserReview(userReview: UserReview): Result<Boolean>
        = runCatching {
            fs.collection("user_review").add(userReview).await()
            true
        }

    override suspend fun deleteUserReview(reviewId: String): Result<Boolean>
        = runCatching {
            fs.collection("user_review")
                .whereEqualTo("id", reviewId)
                .get().await()
                .first().reference
                .delete().await()
            true
        }

    override suspend fun getUserReview(movieId: Int, userId: String): Result<List<UserReview>>
        = runCatching {
            val querySnapshot = fs.collection("user_review")
                .whereEqualTo("movieId", movieId)
                .orderBy("createdDate", Query.Direction.ASCENDING)
                .get().await()
            querySnapshot.toObjects(UserReview::class.java)
        }
}