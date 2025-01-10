package org.comon.moviefriends.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.ai.RequestRecommendationApiDto
import org.comon.moviefriends.data.entity.ai.ResponseRecommendationApiDto
import org.comon.moviefriends.domain.repo.AiRepository
import org.comon.moviefriends.domain.repo.MovieActivityHistoryRepository
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor(
    private val aiRepository: AiRepository,
    private val movieActivityHistoryRepository: MovieActivityHistoryRepository,
): ViewModel() {

    private val _recommendState = MutableStateFlow<APIResult<Response<ResponseRecommendationApiDto>>>(APIResult.NoConstructor)
    val recommendState = _recommendState.asStateFlow()

    private val _viewingHistory = MutableStateFlow<List<String>>(emptyList())

    fun getViewingHistory(){
        viewModelScope.launch {
            movieActivityHistoryRepository.getAllHistory().collect { history ->
                _viewingHistory.emit(
                    history.map { item ->
                        item.movieTitle
                    }
                )
            }
        }
    }

    fun recommendMovie(){
        viewModelScope.launch {
            if(_viewingHistory.value.isEmpty()){
                _recommendState.emit(APIResult.NetworkError(Exception("활동 기록이 없습니다.")))
                return@launch
            }
            val requestBody = RequestRecommendationApiDto(viewingHistory = _viewingHistory.value.joinToString(","))
            aiRepository.requestMessage(requestBody).collectLatest {
                _recommendState.emit(it)
            }
        }
    }
}