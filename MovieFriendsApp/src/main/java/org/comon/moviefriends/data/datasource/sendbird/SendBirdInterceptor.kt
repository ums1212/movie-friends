package org.comon.moviefriends.data.datasource.sendbird

import okhttp3.Interceptor
import okhttp3.Response
import org.comon.moviefriends.BuildConfig
import java.io.IOException

class SendBirdInterceptor: Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Api-Token", BuildConfig.SENDBIRD_API_TOKEN)
            .build()
        return chain.proceed(request)
    }

}