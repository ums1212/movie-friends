package org.comon.moviefriends.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.comon.moviefriends.data.datasource.firebase.AuthenticationDataSource
import org.comon.moviefriends.data.datasource.firebase.AuthenticationDataSourceImpl
import org.comon.moviefriends.data.datasource.firebase.CommunityPostDataSource
import org.comon.moviefriends.data.datasource.firebase.CommunityPostDataSourceImpl
import org.comon.moviefriends.data.datasource.firebase.MovieDetailDataSource
import org.comon.moviefriends.data.datasource.firebase.MovieDetailDataSourceImpl
import org.comon.moviefriends.data.datasource.lbs.MFLocationService
import org.comon.moviefriends.data.datasource.tmdb.TMDBService
import org.comon.moviefriends.data.repo.ChatRepositoryImpl
import org.comon.moviefriends.domain.repo.CommunityPostRepository
import org.comon.moviefriends.data.repo.CommunityPostRepositoryImpl
import org.comon.moviefriends.domain.repo.LoginRepository
import org.comon.moviefriends.data.repo.LoginRepositoryImpl
import org.comon.moviefriends.domain.repo.TMDBRepository
import org.comon.moviefriends.data.repo.TMDBRepositoryImpl
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
    fun providesAuthenticationDataSource(auth: FirebaseAuth, db: FirebaseFirestore): AuthenticationDataSource =
        AuthenticationDataSourceImpl(auth, db)

    @Singleton
    @Provides
    fun providesCommunityPostDataSource(db: FirebaseFirestore, storage: FirebaseStorage): CommunityPostDataSource =
        CommunityPostDataSourceImpl(db, storage)

    @Singleton
    @Provides
    fun providesMovieDetailDataSource(db: FirebaseFirestore, mfLocationService: MFLocationService): MovieDetailDataSource =
        MovieDetailDataSourceImpl(db, mfLocationService)

    @Singleton
    @Provides
    fun providesCommunityPostRepository(dataSource: CommunityPostDataSource): CommunityPostRepository =
        CommunityPostRepositoryImpl(dataSource)

    @Singleton
    @Provides
    fun providesLoginRepository(dataSource: AuthenticationDataSource): LoginRepository =
        LoginRepositoryImpl(dataSource)

    @Singleton
    @Provides
    fun providesTMDBRepository(
        dataSource: MovieDetailDataSource,
        rest: TMDBService,
    ): TMDBRepository =
        TMDBRepositoryImpl(dataSource, rest)

    @Singleton
    @Provides
    fun providesChatRepository(dataSource: MovieDetailDataSource): ChatRepository =
        ChatRepositoryImpl(dataSource)

}