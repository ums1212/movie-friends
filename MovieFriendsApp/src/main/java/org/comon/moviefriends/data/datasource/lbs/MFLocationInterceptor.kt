package org.comon.moviefriends.data.datasource.lbs

import okhttp3.Interceptor
import okhttp3.Response
import org.comon.moviefriends.BuildConfig
import java.io.IOException

class MFLocationInterceptor: Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "KakaoAK ${BuildConfig.KAKAO_REST_KEY}")
            .build()
        return chain.proceed(request)
    }

}