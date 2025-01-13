package org.comon.moviefriends.presentation.screen.community

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sendbird.uikit.compose.component.CircularProgressIndicator
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.presentation.components.MFButton
import org.comon.moviefriends.presentation.components.MFTitle
import org.comon.moviefriends.presentation.viewmodel.WorldCupViewModel

@Composable
fun WorldCupScreen(
    worldCupViewModel: WorldCupViewModel = hiltViewModel(),
    navigateToWorldCupGameScreen: (worldCupId: Int) -> Unit,
) {

    val worldCupInfo = worldCupViewModel.worldCupInfo.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        worldCupViewModel.getMonthlyWorldCupInfo()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        MFTitle(text = "이 달의 영화 월드컵")
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            when(val worldCupInfo = worldCupInfo.value){
                APIResult.NoConstructor -> {
                    MFButton(
                        text = "영화 월드컵 불러오기",
                        clickEvent = {
                            worldCupViewModel.getMonthlyWorldCupInfo()
                        }
                    )
                }
                APIResult.Loading -> {
                    CircularProgressIndicator()
                }
                is APIResult.NetworkError -> {
                    MFTitle(text = "네트워크 에러! \n잠시 후 다시 시도해주세요.")
                    Spacer(modifier = Modifier.padding(vertical = 16.dp))
                    MFButton(
                        text = "영화 월드컵 불러오기",
                        clickEvent = {
                            worldCupViewModel.getMonthlyWorldCupInfo()
                        }
                    )
                }
                is APIResult.Success -> {
                    MFButton(
                        text = "시작하기",
                        clickEvent = {
                            navigateToWorldCupGameScreen(0)
                            worldCupInfo.resultData.body()?.let { info ->
                                navigateToWorldCupGameScreen(info.worldCupId)
                            }
                        }
                    )
                }
            }
        }
    }
}