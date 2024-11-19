package org.comon.moviefriends.data.repo

import org.comon.moviefriends.data.datasource.firebase.MovieDetailDataSource
import org.comon.moviefriends.domain.repo.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val dataSource: MovieDetailDataSource
): ChatRepository {

    override suspend fun loadChatList(userId: String) =
        dataSource.getConfirmedList(userId)

}