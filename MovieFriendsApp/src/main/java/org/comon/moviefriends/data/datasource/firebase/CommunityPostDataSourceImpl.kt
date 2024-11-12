package org.comon.moviefriends.data.datasource.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MovieFriendsApplication
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.LikeInfo
import org.comon.moviefriends.data.model.firebase.PostInfo
import org.comon.moviefriends.data.model.firebase.ReplyInfo
import org.comon.moviefriends.data.model.firebase.UserInfo

class CommunityPostDataSourceImpl(
    private val db: FirebaseFirestore = Firebase.firestore,
    private val storage: FirebaseStorage = Firebase.storage,
): CommunityPostDataSource {

    override suspend fun insertPost(post: PostInfo) = flow {
        emit(APIResult.Loading)
        val reference = db.collection("post").add(post).await()
        val addedPost = reference.get().await().toObject(PostInfo::class.java)
        if(addedPost==null){
            val msg = MovieFriendsApplication.getMovieFriendsApplication().getString(R.string.no_data)
            throw FirebaseFirestoreException(msg, FirebaseFirestoreException.Code.DATA_LOSS)
        }else{
            emit(APIResult.Success(addedPost.id))
        }
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun uploadImage(imageUri: Uri, fileName: String) = flow {
        emit(APIResult.Loading)
        storage.reference.child("post_image").child(fileName).putFile(imageUri).await()
        emit(APIResult.Success(true))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun updatePost(post: PostInfo) = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("post").whereEqualTo("id", post.id).get().await()
        querySnapshot.documents.first().reference.set(post).await()
        val updatedPost = querySnapshot.documents.first().toObject(PostInfo::class.java)
        if(updatedPost==null){
            val msg = MovieFriendsApplication.getMovieFriendsApplication().getString(R.string.no_data)
            throw FirebaseFirestoreException(msg, FirebaseFirestoreException.Code.DATA_LOSS)
        }else{
            emit(APIResult.Success(updatedPost.id))
        }
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun deletePost(postId: String) = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("post").whereEqualTo("id", postId).get().await()
        querySnapshot.documents.first().reference.delete().await()
        emit(APIResult.Success(true))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun getPost(postId: String) = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("post").whereEqualTo("id", postId).get().await()
        val post = querySnapshot.documents.first().toObject(PostInfo::class.java)
        emit(APIResult.Success(post))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun getALLPost() = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("post").orderBy("createdDate", Query.Direction.DESCENDING).get().await()
        val list = querySnapshot.toObjects(PostInfo::class.java)
        emit(APIResult.Success(list))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun addViewCount(postId: String) {
        runCatching {
            val querySnapshot = db.collection("post").whereEqualTo("id", postId).get().await()
            var viewCount = querySnapshot.documents.first().data?.get("viewCount") as Long
            querySnapshot.documents.first().reference.update("viewCount", ++viewCount).await()
        }.onSuccess {
            Log.i("addViewCount", "addViewCount Success")
        }.onFailure {
            Log.e("addViewCount", "addViewCount Fail : ${it.message}")
        }
    }

    override suspend fun getPostLikeState(postId: String, userId: String) = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("post").whereEqualTo("id", postId).get().await()
        val post = querySnapshot.documents.first().toObject(PostInfo::class.java)
            ?: throw FirebaseFirestoreException("getPostLikeState", FirebaseFirestoreException.Code.DATA_LOSS)
        val likeInfo = LikeInfo(
            postId = postId,
            userId = userId,
            likeCount = post.likes.size,
            isLiked = post.likes.find { it.id == userId } != null
        )
        emit(APIResult.Success(likeInfo))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun changePostLikeState(postId: String, userInfo: UserInfo) = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("post").whereEqualTo("id", postId).get().await()
        val post = querySnapshot.documents.first().toObject(PostInfo::class.java)
            ?: throw FirebaseFirestoreException("changePostLikeState", FirebaseFirestoreException.Code.DATA_LOSS)
        val reference = querySnapshot.documents.first().reference
        // 좋아요 체크 해제
        if(post.likes.find { it.id == userInfo.id } != null){
            val newList = post.likes.filter { it.id != userInfo.id }
            reference.update("likes", newList).await()
            val likeInfo = LikeInfo(
                postId = postId,
                userId = userInfo.id,
                likeCount = newList.size,
                isLiked = false
            )
            emit(APIResult.Success(likeInfo))
        }
        // 좋아요 체크
        else{
            val newList = post.likes + userInfo
            reference.update("likes", newList).await()
            val likeInfo = LikeInfo(
                postId = postId,
                userId = userInfo.id,
                likeCount = newList.size,
                isLiked = true
            )
            emit(APIResult.Success(likeInfo))
        }
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun insertReply(replyInfo: ReplyInfo) = flow {
        emit(APIResult.Loading)
        db.collection("post_reply").add(replyInfo).await()
        emit(APIResult.Success(true))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun deleteReply(replyId: String) = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("post_reply").whereEqualTo("id", replyId).get().await()
        querySnapshot.documents.first().reference.delete().await()
        emit(APIResult.Success(true))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    override suspend fun getALLReply(postId: String) = flow {
        emit(APIResult.Loading)
        val querySnapshot = db.collection("post_reply").orderBy("createdDate", Query.Direction.ASCENDING).get().await()
        val list = querySnapshot.toObjects(ReplyInfo::class.java)
        emit(APIResult.Success(list))
    }.catch {
        emit(APIResult.NetworkError(it))
    }
}