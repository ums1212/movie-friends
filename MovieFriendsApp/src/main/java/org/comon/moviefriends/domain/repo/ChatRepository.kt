package org.comon.moviefriends.domain.repo

import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.RequestChatInfo
import org.comon.moviefriends.data.model.firebase.UserInfo

interface ChatRepository {

    suspend fun getAllChatRequestCount(userId: String): Flow<APIResult<Map<String, Int>>>

    suspend fun getRequestChatList(userId: String): Flow<APIResult<List<RequestChatInfo?>>>

    suspend fun getReceiveChatList(userId: String): Flow<APIResult<List<RequestChatInfo?>>>

    suspend fun loadChatList(userId: String): Flow<APIResult<List<RequestChatInfo?>>>

    suspend fun requestWatchTogether(requestChatInfo: RequestChatInfo): Flow<APIResult<Boolean>>

    suspend fun confirmRequest(userInfo: UserInfo, requestChatInfo: RequestChatInfo): Flow<APIResult<Boolean>>

    suspend fun denyRequest(requestChatInfo: RequestChatInfo): Flow<APIResult<Boolean>>

}