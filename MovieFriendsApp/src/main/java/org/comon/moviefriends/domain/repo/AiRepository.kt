package org.comon.moviefriends.domain.repo

import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.ai.RequestRecommendationApiDto
import org.comon.moviefriends.data.entity.ai.ResponseAnthropicDto
import org.comon.moviefriends.data.entity.ai.ResponseRecommendationApiDto
import retrofit2.Response

interface AiRepository {

    fun requestMessage(requestBody: RequestRecommendationApiDto): Flow<APIResult<Response<ResponseRecommendationApiDto>>>

}