package org.comon.moviefriends.data.datasource.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.comon.moviefriends.data.entity.tmdb.MovieActivityHistory

@Dao
interface MovieActivityHistoryDao {

    @Query("SELECT * FROM MovieActivityHistory")
    suspend fun getAllHistory(): List<MovieActivityHistory>

    @Insert
    suspend fun insertHistory(movieActivityHistory: MovieActivityHistory)

}