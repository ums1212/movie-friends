package org.comon.moviefriends.data.datasource.firebase

import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.PostInfo
import org.comon.moviefriends.data.model.firebase.ReplyInfo

interface CommunityPostDataSource {
    suspend fun insertPost(post: PostInfo): Flow<APIResult<String>>

    suspend fun updatePost(post: PostInfo): Flow<APIResult<String>>

    suspend fun deletePost(postId: String): Flow<APIResult<Boolean>>

    suspend fun getPost(postId: String): Flow<APIResult<PostInfo?>>

    suspend fun getALLPost(): Flow<APIResult<List<PostInfo?>>>

    suspend fun insertReply(replyInfo: ReplyInfo): Flow<APIResult<Boolean>>

    suspend fun deleteReply(replyId: String): Flow<APIResult<Boolean>>

    suspend fun getALLReply(postId: String): Flow<APIResult<List<ReplyInfo?>>>
}