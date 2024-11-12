package org.comon.moviefriends.data.repo

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.LikeInfo
import org.comon.moviefriends.data.model.firebase.PostInfo
import org.comon.moviefriends.data.model.firebase.ReplyInfo
import org.comon.moviefriends.data.model.firebase.UserInfo

interface CommunityPostRepository {
    suspend fun insertPost(post: PostInfo): Flow<APIResult<String>>

    suspend fun uploadImage(imageUri: Uri, fileName: String): Flow<APIResult<Boolean>>

    suspend fun updatePost(post: PostInfo): Flow<APIResult<String>>

    suspend fun deletePost(postId: String): Flow<APIResult<Boolean>>

    suspend fun getPost(postId: String): Flow<APIResult<PostInfo?>>

    suspend fun getALLPost(): Flow<APIResult<List<PostInfo?>>>

    suspend fun addViewCount(postId: String)

    suspend fun getPostLikeState(postId: String, userId: String): Flow<APIResult<LikeInfo>>

    suspend fun changePostLikeState(postId: String, userInfo: UserInfo): Flow<APIResult<LikeInfo>>

    suspend fun insertReply(replyInfo: ReplyInfo): Flow<APIResult<Boolean>>

    suspend fun deleteReply(replyId: String): Flow<APIResult<Boolean>>

    suspend fun getALLReply(postId: String): Flow<APIResult<List<ReplyInfo?>>>
}