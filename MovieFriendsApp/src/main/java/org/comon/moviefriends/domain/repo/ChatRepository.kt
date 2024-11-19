package org.comon.moviefriends.domain.repo

import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.RequestChatInfo

interface ChatRepository {

    suspend fun loadChatList(userId: String): Flow<APIResult<List<RequestChatInfo?>>>

}