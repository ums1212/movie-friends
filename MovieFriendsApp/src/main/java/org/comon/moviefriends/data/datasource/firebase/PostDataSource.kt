package org.comon.moviefriends.data.datasource.firebase

import android.net.Uri
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.LikeInfo
import org.comon.moviefriends.data.model.firebase.PostInfo
import org.comon.moviefriends.data.model.firebase.ReplyInfo
import org.comon.moviefriends.data.model.firebase.UserInfo

interface PostDataSource {
    suspend fun insertPost(post: PostInfo): Flow<APIResult<String>>

    suspend fun uploadImage(imageUri: Uri, fileName: String): Flow<APIResult<Boolean>>

    suspend fun updatePost(post: PostInfo): Flow<APIResult<String>>

    suspend fun deletePost(postId: String): Flow<APIResult<Boolean>>

    suspend fun getPost(postId: String): Flow<APIResult<PostInfo?>>

    suspend fun getALLPost(): Flow<APIResult<List<PostInfo?>>>

    suspend fun addViewCount(postId: String)

    suspend fun getPostLikeState(postId: String, userId: String): Flow<APIResult<LikeInfo>>

    suspend fun changePostLikeState(postId: String, userInfo: UserInfo): Flow<APIResult<LikeInfo>>

    suspend fun insertReply(postId: String, replyInfo: ReplyInfo): Flow<APIResult<Boolean>>

    suspend fun deleteReply(postId: String, replyId: String): Flow<APIResult<Boolean>>

    suspend fun getReplyList(postId: String): Flow<APIResult<DocumentReference>>

    suspend fun getALLReply(userId: String): Flow<APIResult<List<ReplyInfo?>>>
}