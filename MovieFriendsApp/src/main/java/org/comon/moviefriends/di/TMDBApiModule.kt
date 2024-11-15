package org.comon.moviefriends.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.comon.moviefriends.data.datasource.tmdb.BASE_TMDB_URL
import org.comon.moviefriends.data.datasource.tmdb.TMDBInterceptor
import org.comon.moviefriends.data.datasource.tmdb.TMDBService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TMDBApiModule {

    @Singleton
    @Provides
    fun providesTMDBInterceptor(): Interceptor =
        TMDBInterceptor()

    @Named("tmdbOkHttpClient")
    @Singleton
    @Provides
    fun providesTMDBOkHttpClient(
        tmdbInterceptor: TMDBInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("Retrofit2", "CONNECTION INFO -> $message")
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            // 고정적인 헤더값을 전달하기 위해 인터셉터 사용
            .addInterceptor(tmdbInterceptor)
            // 로깅을 위한 인터셉터
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Named("tmdbRetrofit")
    @Singleton
    @Provides
    fun providesTMDBRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        @Named("tmdbOkHttpClient") tmdbOkHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_TMDB_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(tmdbOkHttpClient)
            .build()

    @Singleton
    @Provides
    fun providesTMDBApiService(@Named("tmdbRetrofit") retrofit: Retrofit): TMDBService =
        retrofit.create(TMDBService::class.java)
}