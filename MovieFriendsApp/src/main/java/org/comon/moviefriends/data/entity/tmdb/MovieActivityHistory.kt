package org.comon.moviefriends.data.entity.tmdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class MovieActivityHistory (
    @PrimaryKey(autoGenerate = true) val idx: Int = 0,
    @ColumnInfo(name="reg_date") val regDate: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name="movie_id") val movieId: Int,
    @ColumnInfo(name="movie_title") val movieTitle: String,
    @ColumnInfo(name="movie_release_date") val movieReleaseDate: String,
    @ColumnInfo(name="movie_poster_path") val moviePosterPath: String,
)