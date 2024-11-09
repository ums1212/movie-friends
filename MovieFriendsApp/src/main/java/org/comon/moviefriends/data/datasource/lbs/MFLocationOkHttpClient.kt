package org.comon.moviefriends.data.datasource.lbs

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.comon.moviefriends.data.datasource.tmdb.TMDBInterceptor

object MFLocationOkHttpClient {
    private var okHttpClient: OkHttpClient? = null

    fun getClient() : OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("Retrofit2", "CONNECTION INFO -> $message")
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        if (okHttpClient == null) {
            okHttpClient = OkHttpClient.Builder()
                // 고정적인 헤더값을 전달하기 위해 인터셉터 사용
                .addInterceptor(MFLocationInterceptor())
                .addInterceptor(loggingInterceptor)
                .build()
        }
        return okHttpClient!!
    }
}