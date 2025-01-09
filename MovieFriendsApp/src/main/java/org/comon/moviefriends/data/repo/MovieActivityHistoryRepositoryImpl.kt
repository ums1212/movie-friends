package org.comon.moviefriends.data.repo

import kotlinx.coroutines.flow.flow
import org.comon.moviefriends.data.datasource.room.MovieActivityHistoryDao
import org.comon.moviefriends.data.entity.tmdb.MovieActivityHistory
import org.comon.moviefriends.domain.repo.MovieActivityHistoryRepository
import javax.inject.Inject

class MovieActivityHistoryRepositoryImpl @Inject constructor(
    private val movieActivityHistoryDao: MovieActivityHistoryDao
): MovieActivityHistoryRepository {

    override fun getAllHistory() = flow {
        emit(movieActivityHistoryDao.getAllHistory())
    }

    override suspend fun insertHistory(movieActivityHistory: MovieActivityHistory) =
        movieActivityHistoryDao.insertHistory(movieActivityHistory)

}