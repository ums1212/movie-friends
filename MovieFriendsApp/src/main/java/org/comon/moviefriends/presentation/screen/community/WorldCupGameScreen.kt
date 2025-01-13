package org.comon.moviefriends.presentation.screen.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.comon.moviefriends.presentation.components.SelectedWorldCupItemPosition
import org.comon.moviefriends.presentation.components.WorldCupItemBox
import org.comon.moviefriends.presentation.components.WorldCupItemPosition
import org.comon.moviefriends.presentation.viewmodel.WorldCupViewModel

@Composable
fun WorldCupGameScreen(
    worldCupViewModel: WorldCupViewModel = hiltViewModel(),
    worldCupId: Int,
) {

    val worldCupItemList = worldCupViewModel.worldCupItemList.collectAsStateWithLifecycle()
    val selectedBoxPosition = worldCupViewModel.selectedBoxPosition.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        worldCupViewModel.getMonthlyWorldCupItemList(worldCupId)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        // 2개의 월드컵 아이템
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            WorldCupItemBox(
                position = WorldCupItemPosition.TOP,
                selectedPosition = selectedBoxPosition.value,
                selectPosition = {
                    worldCupViewModel.setSelectedBoxPosition(it)
                }
            )
            WorldCupItemBox(
                position = WorldCupItemPosition.BOTTOM,
                selectedPosition = selectedBoxPosition.value,
                selectPosition = {
                    worldCupViewModel.setSelectedBoxPosition(it)
                }
            )
        }
        // 가운데 화면, 2개의 아이템 사이에 놓을 "VS" 표시
        when(selectedBoxPosition.value){
            SelectedWorldCupItemPosition.NONE -> Text(text = "VS", style = TextStyle(fontSize = 32.sp))
            else -> {}
        }
    }
}