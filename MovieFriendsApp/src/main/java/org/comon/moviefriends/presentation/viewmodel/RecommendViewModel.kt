package org.comon.moviefriends.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.ai.RequestAnthropicBodyDto
import org.comon.moviefriends.data.entity.ai.ResponseAnthropicDto
import org.comon.moviefriends.domain.repo.AiRepository
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor(
    private val aiRepository: AiRepository
): ViewModel() {

    private val _recommendState = MutableStateFlow<APIResult<Response<ResponseAnthropicDto>>>(APIResult.NoConstructor)
    val recommendState = _recommendState.asStateFlow()

    fun recommendMovie(){
        viewModelScope.launch {
            val messages = listOf(
                RequestAnthropicBodyDto.Message(content = "hello")
            )
            val requestBody = RequestAnthropicBodyDto(messages = messages)
            aiRepository.requestMessage(requestBody).collectLatest {
                _recommendState.emit(it)
            }
        }
    }
}