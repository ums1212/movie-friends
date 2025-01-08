package org.comon.moviefriends.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.comon.moviefriends.data.datasource.ai.AnthropicInterceptor
import org.comon.moviefriends.data.datasource.ai.AnthropicService
import org.comon.moviefriends.data.datasource.ai.BASE_ANTHROPIC_URL
import org.comon.moviefriends.data.repo.AiRepositoryImpl
import org.comon.moviefriends.domain.repo.AiRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnthropicApiModule {

    @Singleton
    @Provides
    fun providesAnthropicInterceptor(): Interceptor =
        AnthropicInterceptor()

    @Named("anthropicOkHttpClient")
    @Singleton
    @Provides
    fun providesAnthropicOkHttpClient(
        anthropicInterceptor: AnthropicInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("Retrofit2", "CONNECTION INFO -> $message")
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            // 고정적인 헤더값을 전달하기 위해 인터셉터 사용
            .addInterceptor(anthropicInterceptor)
            // 로깅을 위한 인터셉터
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Named("anthropicRetrofit")
    @Singleton
    @Provides
    fun providesAnthropicRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        @Named("anthropicOkHttpClient") anthropicOkHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_ANTHROPIC_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(anthropicOkHttpClient)
            .build()

    @Singleton
    @Provides
    fun providesAnthropicApiService(@Named("anthropicRetrofit") retrofit: Retrofit): AnthropicService =
        retrofit.create(AnthropicService::class.java)

    @Singleton
    @Provides
    fun providesAiRepository(anthropicService: AnthropicService): AiRepository =
        AiRepositoryImpl(anthropicService)

}