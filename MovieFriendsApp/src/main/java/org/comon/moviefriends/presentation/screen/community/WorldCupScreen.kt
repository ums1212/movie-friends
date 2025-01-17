package org.comon.moviefriends.presentation.screen.community

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.sendbird.uikit.compose.component.CircularProgressIndicator
import org.comon.moviefriends.BuildConfig
import org.comon.moviefriends.R
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.worldcup.ResponseMovieWorldCupItemListDto
import org.comon.moviefriends.presentation.components.MFButton
import org.comon.moviefriends.presentation.components.MFText
import org.comon.moviefriends.presentation.components.MFTitle
import org.comon.moviefriends.presentation.viewmodel.WorldCupViewModel

@Composable
fun WorldCupScreen(
    worldCupViewModel: WorldCupViewModel = hiltViewModel(),
    navigateToWorldCupGameScreen: (worldCupId: Int) -> Unit,
) {

    val worldCupInfo = worldCupViewModel.worldCupInfo.collectAsStateWithLifecycle()
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        worldCupViewModel.getMonthlyWorldCupInfo()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            when(val result = worldCupInfo.value){
                APIResult.NoConstructor -> {
                    MFTitle(text = "이 달의 영화 월드컵")
                    Spacer(modifier = Modifier.padding(vertical = 8.dp))
                    MFButton(
                        text = "영화 월드컵 불러오기",
                        clickEvent = {
                            worldCupViewModel.getMonthlyWorldCupInfo()
                        }
                    )
                }
                APIResult.Loading -> {
                    MFTitle(text = "이 달의 영화 월드컵")
                    Spacer(modifier = Modifier.padding(vertical = 8.dp))
                    CircularProgressIndicator()
                }
                is APIResult.NetworkError -> {
                    MFTitle(text = "이 달의 영화 월드컵")
                    Spacer(modifier = Modifier.padding(vertical = 8.dp))
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
                    val resultList = result.resultData.body()
                    if(resultList?.isNotEmpty() == true){
                        val info = resultList.first()
                        MFTitle(text = info.title)
                        Spacer(modifier = Modifier.padding(vertical = 8.dp))
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth(),
                            model = ImageRequest.Builder(context)
                                .data("${BuildConfig.BASE_WORLDCUP_URL}${info.image}")
                                .crossfade(true)
                                .error(R.drawable.logo)
                                .build(),
                            contentDescription = "영화 월드컵 이미지",
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.padding(vertical = 8.dp))
                        MFText(text = info.description)
                        Spacer(modifier = Modifier.padding(vertical = 8.dp))
                        MFButton(
                            text = "시작하기",
                            clickEvent = {
                                navigateToWorldCupGameScreen(info.worldCupId)
                            }
                        )
                    }else{
                        MFTitle(text = "현재 진행중인 월드컵이 없습니다.")
                    }
                }
            }
        }
    }
}