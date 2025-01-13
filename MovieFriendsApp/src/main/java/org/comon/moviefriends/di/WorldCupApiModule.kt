package org.comon.moviefriends.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.comon.moviefriends.BuildConfig
import org.comon.moviefriends.data.datasource.worldcup.WorldCupService
import org.comon.moviefriends.data.repo.WorldCupRepositoryImpl
import org.comon.moviefriends.domain.repo.WorldCupRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorldCupApiModule {

    @Named("worldCupOkHttpClient")
    @Singleton
    @Provides
    fun providesWorldCupOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("Retrofit2", "CONNECTION INFO -> $message")
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            // 로깅을 위한 인터셉터
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Named("worldCupRetrofit")
    @Singleton
    @Provides
    fun providesWorldCupRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        @Named("worldCupOkHttpClient") worldCupOkHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_WORLDCUP_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(worldCupOkHttpClient)
            .build()

    @Singleton
    @Provides
    fun providesWorldCupService(@Named("worldCupRetrofit") retrofit: Retrofit): WorldCupService =
        retrofit.create(WorldCupService::class.java)

    @Singleton
    @Provides
    fun providesWorldCupRepository(worldCupService: WorldCupService): WorldCupRepository =
        WorldCupRepositoryImpl(worldCupService)
}