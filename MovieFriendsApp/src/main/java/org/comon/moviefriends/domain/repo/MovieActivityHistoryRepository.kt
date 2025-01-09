package org.comon.moviefriends.domain.repo

import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.entity.tmdb.MovieActivityHistory

interface MovieActivityHistoryRepository {

    fun getAllHistory(): Flow<List<MovieActivityHistory>>

    suspend fun insertHistory(movieActivityHistory: MovieActivityHistory)

}