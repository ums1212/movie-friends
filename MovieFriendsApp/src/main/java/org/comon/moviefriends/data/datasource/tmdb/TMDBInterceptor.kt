package org.comon.moviefriends.data.datasource.tmdb

import okhttp3.Interceptor
import okhttp3.Response
import org.comon.moviefriends.BuildConfig
import java.io.IOException

class TMDBInterceptor: Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}")
            .build()
        return chain.proceed(request)
    }

}