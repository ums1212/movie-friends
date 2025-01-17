package org.comon.moviefriends.presentation.screen.community

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.comon.moviefriends.R
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.worldcup.ResponseMovieWorldCupItemListDto
import org.comon.moviefriends.presentation.components.MFTitle
import org.comon.moviefriends.presentation.components.SelectedWorldCupItemPosition
import org.comon.moviefriends.presentation.components.WorldCupItemBox
import org.comon.moviefriends.presentation.components.WorldCupItemPosition
import org.comon.moviefriends.presentation.components.WorldCupTitleText
import org.comon.moviefriends.presentation.components.WorldCupVersusText
import org.comon.moviefriends.presentation.viewmodel.WorldCupViewModel

@Composable
fun WorldCupGameScreen(
    worldCupViewModel: WorldCupViewModel = hiltViewModel(),
    worldCupId: Int,
    navigateToWorldCupWinnerScreen: (winner: ResponseMovieWorldCupItemListDto.ResponseMovieWorldCupItemDto) -> Unit,
) {

    val worldCupItemList = worldCupViewModel.worldCupItemList.collectAsStateWithLifecycle()
    val selectedBoxPosition = worldCupViewModel.selectedBoxPosition.collectAsStateWithLifecycle()
    val playingItemList = worldCupViewModel.playingItemList.collectAsStateWithLifecycle()
    val topItemIndex = worldCupViewModel.topItemIndex.collectAsStateWithLifecycle()
    val bottomItemIndex = worldCupViewModel.bottomItemIndex.collectAsStateWithLifecycle()
    val currentRound = worldCupViewModel.currentRound.collectAsStateWithLifecycle()
    val matchCount = worldCupViewModel.matchCount.collectAsStateWithLifecycle()
    val gameWinner = worldCupViewModel.gameWinner.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        worldCupViewModel.getMonthlyWorldCupItemList(worldCupId)
    }

    LaunchedEffect(gameWinner) {
        Log.d("test1234", "${gameWinner.value}")
        if(gameWinner.value != null){
            navigateToWorldCupWinnerScreen(gameWinner.value!!)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ){
        WorldCupTitleText(if(currentRound.value==2) "결승" else "${currentRound.value}강 매치 ${matchCount.value}/${currentRound.value/2}")
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            when(val itemList = worldCupItemList.value){
                APIResult.NoConstructor -> CircularProgressIndicator()
                APIResult.Loading -> CircularProgressIndicator()
                is APIResult.NetworkError -> {
                    Column {
                        MFTitle(text = stringResource(id = R.string.network_error))
                        Spacer(modifier = Modifier.padding(vertical = 8.dp))
                        Image(painter = painterResource(id = R.drawable.yoshicat), contentDescription = "에러 이미지")
                    }
                }
                is APIResult.Success -> {
                    if(itemList.resultData.body()==null || itemList.resultData.body()?.isEmpty() == true){
                        MFTitle(text = "현재 진행중인 월드컵이 없습니다.")
                        return@Box
                    }
                    // 2개의 월드컵 아이템
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ){
                        WorldCupItemBox(
                            position = WorldCupItemPosition.TOP,
                            item = playingItemList.value[topItemIndex.value],
                            selectedPosition = selectedBoxPosition.value,
                            selectPosition = {
                                worldCupViewModel.setSelectedBoxPosition(it)
                            }
                        )
                        WorldCupItemBox(
                            position = WorldCupItemPosition.BOTTOM,
                            item = playingItemList.value[bottomItemIndex.value],
                            selectedPosition = selectedBoxPosition.value,
                            selectPosition = {
                                worldCupViewModel.setSelectedBoxPosition(it)
                            }
                        )
                    }
                    // 가운데 화면, 2개의 아이템 사이에 놓을 "VS" 표시
                    when(selectedBoxPosition.value){
                        SelectedWorldCupItemPosition.NONE -> WorldCupVersusText()
                        else -> {}
                    }
                }
            }
        }
    }
}