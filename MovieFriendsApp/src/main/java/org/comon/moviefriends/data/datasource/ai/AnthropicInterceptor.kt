package org.comon.moviefriends.data.datasource.ai

import okhttp3.Interceptor
import okhttp3.Response
import org.comon.moviefriends.BuildConfig
import java.io.IOException
import javax.inject.Inject

class AnthropicInterceptor @Inject constructor(): Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("x-api-key", BuildConfig.ANTHROPIC_API_KEY)
            .addHeader("anthropic-version", "2023-06-01")
            .addHeader("content-type", "application/json")
            .build()
        return chain.proceed(request)
    }

}
