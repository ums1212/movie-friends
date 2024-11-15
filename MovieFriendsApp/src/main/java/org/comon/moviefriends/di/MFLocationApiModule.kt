package org.comon.moviefriends.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.comon.moviefriends.data.datasource.lbs.BASE_LOCATION_URL
import org.comon.moviefriends.data.datasource.lbs.MFLocationInterceptor
import org.comon.moviefriends.data.datasource.lbs.MFLocationManager
import org.comon.moviefriends.data.datasource.lbs.MFLocationService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MFLocationApiModule {

    @Singleton
    @Provides
    fun providesMFLocationInterceptor(): Interceptor =
        MFLocationInterceptor()

    @Singleton
    @Provides
    fun providesMFLocationManager(): MFLocationManager =
        MFLocationManager()

    @Named("locationOkHttpClient")
    @Singleton
    @Provides
    fun providesMFLocationOkHttpClient(
        mfLocationInterceptor: MFLocationInterceptor,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("Retrofit2", "CONNECTION INFO -> $message")
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            // 고정적인 헤더값을 전달하기 위해 인터셉터 사용
            .addInterceptor(mfLocationInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Named("locationRetrofit")
    @Singleton
    @Provides
    fun providesMFLocationRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        @Named("locationOkHttpClient") mfLocationOkHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_LOCATION_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(mfLocationOkHttpClient)
            .build()

    @Singleton
    @Provides
    fun providesMFLocationService(@Named("locationRetrofit") retrofit: Retrofit): MFLocationService =
        retrofit.create(MFLocationService::class.java)

}