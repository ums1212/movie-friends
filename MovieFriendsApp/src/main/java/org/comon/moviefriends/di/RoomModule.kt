package org.comon.moviefriends.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.comon.moviefriends.data.datasource.room.MovieActivityHistoryDao
import org.comon.moviefriends.data.datasource.room.MovieActivityHistoryDatabase
import org.comon.moviefriends.data.repo.MovieActivityHistoryRepositoryImpl
import org.comon.moviefriends.domain.repo.MovieActivityHistoryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providesRoomDB(
        @ApplicationContext applicationContext: Context
    ): MovieActivityHistoryDatabase = Room
        .databaseBuilder(applicationContext, MovieActivityHistoryDatabase::class.java, "movie-friends")
        .build()

    @Singleton
    @Provides
    fun providesMovieActivityHistoryDao(
        database: MovieActivityHistoryDatabase
    ): MovieActivityHistoryDao = database.movieActivityHistoryDao()

    @Singleton
    @Provides
    fun providesMovieActivityHistoryRepository(
        dao: MovieActivityHistoryDao
    ): MovieActivityHistoryRepository = MovieActivityHistoryRepositoryImpl(dao)

}