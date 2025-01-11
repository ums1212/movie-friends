package org.comon.moviefriends.data.datasource.ai

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AnthropicInterceptor @Inject constructor(): Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("content-type", "application/json")
            .build()
        return chain.proceed(request)
    }

}
