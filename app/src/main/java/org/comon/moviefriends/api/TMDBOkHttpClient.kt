package org.comon.moviefriends.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object TMDBOkHttpClient {
    private var okHttpClient: OkHttpClient? = null
    fun getClient() : OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("Retrofit2", "CONNECTION INFO -> $message")
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        if (okHttpClient == null) {
            okHttpClient = OkHttpClient.Builder()
                // 고정적인 헤더값을 전달하기 위해 인터셉터 사용
                .addInterceptor(TMDBInterceptor())
                .addInterceptor(loggingInterceptor)
                .build()
        }
        return okHttpClient!!
    }
}