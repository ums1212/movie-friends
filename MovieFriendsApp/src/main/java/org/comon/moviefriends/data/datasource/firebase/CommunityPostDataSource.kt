package org.comon.moviefriends.data.datasource.firebase

import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.PostInfo

interface CommunityPostDataSource {
    suspend fun insertPost(post: PostInfo): Flow<APIResult<String>>

    suspend fun updatePost(post: PostInfo): Flow<APIResult<String>>

    suspend fun deletePost(postId: String): Flow<APIResult<Boolean>>

    suspend fun getPost(postId: String): Flow<APIResult<PostInfo?>>

    suspend fun getALLPost(): Flow<APIResult<List<PostInfo?>>>
}