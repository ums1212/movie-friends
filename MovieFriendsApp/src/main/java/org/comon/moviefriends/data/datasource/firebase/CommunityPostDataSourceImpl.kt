package org.comon.moviefriends.data.datasource.firebase

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MovieFriendsApplication
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.PostInfo
import org.comon.moviefriends.data.model.firebase.ReplyInfo

class CommunityPostDataSourceImpl(
    private val db: FirebaseFirestore = Firebase.firestore
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