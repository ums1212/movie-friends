package org.comon.moviefriends.presentation.screen.community

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.Lottie
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import org.comon.moviefriends.R
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.presentation.components.MFButton
import org.comon.moviefriends.presentation.components.MFPostTitle
import org.comon.moviefriends.presentation.components.OnDevelopMark
import org.comon.moviefriends.presentation.viewmodel.RecommendViewModel

@Composable
fun RecommendScreen(
    recommendViewModel: RecommendViewModel = hiltViewModel(),
) {
//    OnDevelopMark()
    val scrollState = rememberScrollState()
    val recommendState = recommendViewModel.recommendState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        recommendViewModel.getViewingHistory()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
            .scrollable(
                state = scrollState,
                orientation = Orientation.Vertical,
                enabled = true
            )
    ) {
        when(val response = recommendState.value){
            APIResult.NoConstructor -> {
                MFPostTitle(text = "당신이 좋아할 만한 영화를 \nAI가 추천해드립니다.")
                LottieAnimation()
                MFButton(
                    text = "영화 추천 받기",
                    clickEvent = {
                        recommendViewModel.recommendMovie()
                    }
                )
            }
            APIResult.Loading -> {
                MFPostTitle(text = "AI가 당신을 위해 영화 정보를 수집하고 있어요...")
                LottieAnimation()
            }
            is APIResult.NetworkError -> {
                Log.d("test1234", "${response.exception}")
                MFPostTitle(text = "네트워크 에러! \n잠시 후 다시 시도해주세요.")
                LottieAnimation()
            }
            is APIResult.Success -> {
                MFPostTitle(text = response.resultData.body()?.recommendation.toString() ?: "값 없음")
            }
        }
    }

}

@Composable
fun LottieAnimation(){
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.ai_lottie)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )
    LottieAnimation(
        modifier = Modifier.height(350.dp),
        composition = composition,
        progress = { progress },
    )
}