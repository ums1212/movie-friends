package org.comon.moviefriends.data.datasource.ai

import org.comon.moviefriends.data.entity.ai.RequestRecommendationApiDto
import org.comon.moviefriends.data.entity.ai.ResponseRecommendationApiDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AnthropicService {

    // 기본 요청
    @POST("recommendation/recommendation/")
    suspend fun requestMessage(
        @Body requestBody: RequestRecommendationApiDto
    ): Response<ResponseRecommendationApiDto>

}