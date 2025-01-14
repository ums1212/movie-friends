package org.comon.moviefriends.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.worldcup.ResponseMovieWorldCupInfoListDto
import org.comon.moviefriends.data.entity.worldcup.ResponseMovieWorldCupItemListDto
import org.comon.moviefriends.domain.repo.WorldCupRepository
import org.comon.moviefriends.presentation.components.SelectedWorldCupItemPosition
import retrofit2.Response
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WorldCupViewModel @Inject constructor(
    private val worldCupRepository: WorldCupRepository
) : ViewModel() {

    private val _worldCupInfo = MutableStateFlow<APIResult<Response<ResponseMovieWorldCupInfoListDto>>>(APIResult.NoConstructor)
    val worldCupInfo = _worldCupInfo.asStateFlow()

    private val _worldCupItemList = MutableStateFlow<APIResult<Response<ResponseMovieWorldCupItemListDto>>>(APIResult.NoConstructor)
    val worldCupItemList = _worldCupItemList.asStateFlow()

    private val _selectedBoxPosition = MutableStateFlow(SelectedWorldCupItemPosition.NONE)
    val selectedBoxPosition = _selectedBoxPosition.asStateFlow()
    fun setSelectedBoxPosition(position: SelectedWorldCupItemPosition){
        viewModelScope.launch {
            _selectedBoxPosition.emit(position)
        }
    }

    private val _topBoxTriggerState = MutableStateFlow(false)
    val topBoxTriggerState = _topBoxTriggerState.asStateFlow()

    private val _bottomBoxTriggerState = MutableStateFlow(false)
    val bottomBoxTriggerState = _bottomBoxTriggerState.asStateFlow()

    fun getMonthlyWorldCupInfo(){
        viewModelScope.launch {
            val now = LocalDateTime.now()
            worldCupRepository.getMonthlyWorldCupInfo(now.year, now.monthValue).collectLatest {
                _worldCupInfo.emit(it)
            }
        }
    }

    fun getMonthlyWorldCupItemList(worldCupId: Int){
        viewModelScope.launch {
            worldCupRepository.getMonthlyWorldCupItemList(worldCupId).collectLatest {
                _worldCupItemList.emit(it)
            }
        }
    }

}