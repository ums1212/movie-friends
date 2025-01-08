package org.comon.moviefriends.data.datasource.ai

import org.comon.moviefriends.data.entity.ai.RequestAnthropicBodyDto
import org.comon.moviefriends.data.entity.ai.ResponseAnthropicDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AnthropicService {

    // 기본 요청
    @POST("messages")
    suspend fun requestMessage(
        @Body requestBody: RequestAnthropicBodyDto
    ): Response<ResponseAnthropicDto>

}