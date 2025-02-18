package org.comon.moviefriends.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.comon.moviefriends.data.datasource.ai.AnthropicService
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.ai.RequestRecommendationApiDto
import org.comon.moviefriends.data.entity.ai.ResponseRecommendationApiDto
import org.comon.moviefriends.domain.repo.AiRepository
import retrofit2.Response
import javax.inject.Inject

class AiRepositoryImpl @Inject constructor(
    private val anthropicService: AnthropicService
): AiRepository {

    override fun requestMessage(
        requestBody: RequestRecommendationApiDto
    ): Flow<APIResult<Response<ResponseRecommendationApiDto>>> = flow {
        emit(APIResult.Loading)
        emit(APIResult.Success(anthropicService.requestMessage(requestBody)))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

}