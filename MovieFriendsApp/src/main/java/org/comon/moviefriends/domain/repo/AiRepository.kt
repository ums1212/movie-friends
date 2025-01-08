package org.comon.moviefriends.domain.repo

import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.ai.RequestAnthropicBodyDto
import org.comon.moviefriends.data.entity.ai.ResponseAnthropicDto
import retrofit2.Response

interface AiRepository {

    fun requestMessage(requestBody: RequestAnthropicBodyDto): Flow<APIResult<Response<ResponseAnthropicDto>>>

}