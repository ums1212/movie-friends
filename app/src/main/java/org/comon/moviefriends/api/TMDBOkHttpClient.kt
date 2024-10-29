package org.comon.moviefriends.api

import okhttp3.OkHttpClient

object TMDBOkHttpClient {
    private var okHttpClient: OkHttpClient? = null
    fun getClient() : OkHttpClient {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClient.Builder()
                // 고정적인 헤더값을 전달하기 위해 인터셉터 사용
                .addInterceptor(TMDBInterceptor())
                .build()
        }
        return okHttpClient!!
    }
}