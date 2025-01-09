package org.comon.moviefriends.data.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import org.comon.moviefriends.data.entity.tmdb.MovieActivityHistory
import java.time.LocalDateTime
import java.time.ZoneOffset


object DateTypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.toEpochSecond(ZoneOffset.UTC)
    }
}

@Database(entities = [MovieActivityHistory::class], version = 1)
@TypeConverters(DateTypeConverters::class)
abstract class MovieActivityHistoryDatabase: RoomDatabase() {
    abstract fun movieActivityHistoryDao(): MovieActivityHistoryDao
}