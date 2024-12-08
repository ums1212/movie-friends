package org.comon.moviefriends.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.comon.moviefriends.data.datasource.firebase.UserDataSource
import org.comon.moviefriends.data.datasource.firebase.UserDataSourceImpl
import org.comon.moviefriends.data.datasource.firebase.ChatDataSource
import org.comon.moviefriends.data.datasource.firebase.ChatDataSourceImpl
import org.comon.moviefriends.data.datasource.firebase.PostDataSource
import org.comon.moviefriends.data.datasource.firebase.PostDataSourceImpl
import org.comon.moviefriends.data.datasource.firebase.MovieDataSource
import org.comon.moviefriends.data.datasource.firebase.MovieDataSourceImpl
import org.comon.moviefriends.data.datasource.lbs.MFLocationService
import org.comon.moviefriends.data.datasource.tmdb.TMDBService
import org.comon.moviefriends.data.repo.ChatRepositoryImpl
import org.comon.moviefriends.domain.repo.PostRepository
import org.comon.moviefriends.data.repo.PostRepositoryImpl
import org.comon.moviefriends.domain.repo.UserRepository
import org.comon.moviefriends.data.repo.UserRepositoryImpl
import org.comon.moviefriends.domain.repo.MovieRepository
import org.comon.moviefriends.data.repo.MovieRepositoryImpl
import org.comon.moviefriends.domain.repo.ChatRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Singleton
    @Provides
    fun providesFireStore(): FirebaseFirestore =
        Firebase.firestore

    @Singleton
    @Provides
    fun providesFirebaseAuthentication(): FirebaseAuth =
        Firebase.auth

    @Singleton
    @Provides
    fun providesFirebaseStorage(): FirebaseStorage =
        Firebase.storage

    @Singleton
    @Provides
    fun providesFirebaseMessaging(): FirebaseMessaging =
        FirebaseMessaging.getInstance()

    @Singleton
    @Provides
    fun providesUserDataSource(db: FirebaseFirestore): UserDataSource =
        UserDataSourceImpl(db)

    @Singleton
    @Provides
    fun providesCommunityPostDataSource(db: FirebaseFirestore, storage: FirebaseStorage): PostDataSource =
        PostDataSourceImpl(db, storage)

    @Singleton
    @Provides
    fun providesCommunityChatDataSource(db: FirebaseFirestore): ChatDataSource =
        ChatDataSourceImpl(db)

    @Singleton
    @Provides
    fun providesMovieDetailDataSource(db: FirebaseFirestore): MovieDataSource =
        MovieDataSourceImpl(db)

    @Singleton
    @Provides
    fun providesCommunityPostRepository(dataSource: PostDataSource): PostRepository =
        PostRepositoryImpl(dataSource)

    @Singleton
    @Provides
    fun providesUserRepository(
        userDataSource: UserDataSource,
        auth: FirebaseAuth,
        fcm: FirebaseMessaging
    ): UserRepository =
        UserRepositoryImpl(userDataSource, auth, fcm)

    @Singleton
    @Provides
    fun providesMovieRepository(
        movieDataSource: MovieDataSource,
        chatDataSource: ChatDataSource,
        location: MFLocationService,
        rest: TMDBService,
    ): MovieRepository =
        MovieRepositoryImpl(movieDataSource, chatDataSource, location, rest)

    @Singleton
    @Provides
    fun providesChatRepository(chatDataSource: ChatDataSource): ChatRepository =
        ChatRepositoryImpl(chatDataSource)

}