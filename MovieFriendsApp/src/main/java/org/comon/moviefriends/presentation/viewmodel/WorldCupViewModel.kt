package org.comon.moviefriends.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    private val selectDelayTime = 3000L

    private val _worldCupInfo = MutableStateFlow<APIResult<Response<ResponseMovieWorldCupInfoListDto>>>(APIResult.NoConstructor)
    val worldCupInfo = _worldCupInfo.asStateFlow()

    private val _worldCupItemList = MutableStateFlow<APIResult<Response<ResponseMovieWorldCupItemListDto>>>(APIResult.NoConstructor)
    val worldCupItemList = _worldCupItemList.asStateFlow()

    private val _playingItemList = MutableStateFlow<MutableList<ResponseMovieWorldCupItemListDto.ResponseMovieWorldCupItemDto>>(mutableListOf())
    val playingItemList = _playingItemList.asStateFlow()

    private val _nextPlayingItemList = MutableStateFlow<MutableList<ResponseMovieWorldCupItemListDto.ResponseMovieWorldCupItemDto>>(mutableListOf())
    val nextPlayingItemList = _nextPlayingItemList.asStateFlow()

    private val _selectedTotalRound = MutableStateFlow(0)
    val selectedTotalRound = _selectedTotalRound.asStateFlow()

    private val _currentRound = MutableStateFlow(0)
    val currentRound = _currentRound.asStateFlow()

    private val _matchCount = MutableStateFlow(1)
    val matchCount = _matchCount.asStateFlow()

    private val _topItemIndex = MutableStateFlow(0)
    val topItemIndex = _topItemIndex.asStateFlow()

    private val _bottomItemIndex = MutableStateFlow(1)
    val bottomItemIndex = _bottomItemIndex.asStateFlow()

    private val _gameWinner = MutableStateFlow<ResponseMovieWorldCupItemListDto.ResponseMovieWorldCupItemDto?>(null)
    val gameWinner = _gameWinner.asStateFlow()

    private val _selectedBoxPosition = MutableStateFlow(SelectedWorldCupItemPosition.NONE)
    val selectedBoxPosition = _selectedBoxPosition.asStateFlow()
    fun setSelectedBoxPosition(position: SelectedWorldCupItemPosition){
        viewModelScope.launch {
            _selectedBoxPosition.emit(position)
            delay(selectDelayTime)

            when(position){
                SelectedWorldCupItemPosition.TOP -> {
                    _nextPlayingItemList.value.add(_playingItemList.value[_topItemIndex.value])
                }
                SelectedWorldCupItemPosition.BOTTOM -> {
                    _nextPlayingItemList.value.add(_playingItemList.value[_bottomItemIndex.value])
                }
                SelectedWorldCupItemPosition.NONE -> {}
            }

            // 결승이 끝난 경우
            if(_currentRound.value==2 && _matchCount.value==1){
                _gameWinner.emit(_nextPlayingItemList.value.first())
                return@launch
            }

            // Round가 끝난 경우
            if(_currentRound.value / 2 <= _matchCount.value){
                _playingItemList.value = _nextPlayingItemList.value.slice(0..<_nextPlayingItemList.value.size).toMutableList()
                _nextPlayingItemList.value.clear()
                _matchCount.value = 1
                _currentRound.value = _playingItemList.value.size
                _topItemIndex.value = 0
                _bottomItemIndex.value = 1
            }else{
                _topItemIndex.emit(_topItemIndex.value + 2)
                _bottomItemIndex.emit(_bottomItemIndex.value + 2)
                _matchCount.emit(_matchCount.value + 1)
            }

            _selectedBoxPosition.emit(SelectedWorldCupItemPosition.NONE)
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
                if(it is APIResult.Success){
                    it.resultData.body()?.let { itemList ->
                        _playingItemList.emit(itemList)
                        _currentRound.emit(itemList.size)
                    }
                }
            }
        }
    }

}