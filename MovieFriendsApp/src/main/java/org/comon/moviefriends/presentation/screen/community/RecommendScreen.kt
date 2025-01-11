package org.comon.moviefriends.presentation.screen.community

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import coil3.request.placeholder
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import org.comon.moviefriends.R
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.datasource.tmdb.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.data.entity.ai.ResponseRecommendationApiDto
import org.comon.moviefriends.presentation.components.MFButton
import org.comon.moviefriends.presentation.components.MFText
import org.comon.moviefriends.presentation.components.MFTitle
import org.comon.moviefriends.presentation.viewmodel.RecommendViewModel

@Composable
fun RecommendScreen(
    recommendViewModel: RecommendViewModel = hiltViewModel(),
) {
//    OnDevelopMark()
    val recommendState = recommendViewModel.recommendState.collectAsStateWithLifecycle()
    val posterPathState = recommendViewModel.recommendMoviePoster.collectAsStateWithLifecycle()
    val localContext = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        recommendViewModel.getViewingHistory()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        when(val response = recommendState.value){
            APIResult.NoConstructor -> {
                MFTitle(text = "당신이 좋아할 만한 영화를 \nAI가 추천해드립니다.")
                AiLottie()
                MFButton(
                    text = "영화 추천 받기",
                    clickEvent = {
                        recommendViewModel.recommendMovie()
                    }
                )
            }
            APIResult.Loading -> {
                MFTitle(text = "AI가 당신을 위해 영화 정보를 수집하고 있어요...")
                AiLottie()
            }
            is APIResult.NetworkError -> {
                Log.e("RecommendScreen", "${response.exception}")
                MFTitle(text = "네트워크 에러! \n잠시 후 다시 시도해주세요.")
                AiLottie()
                MFButton(
                    text = "돌아가기",
                    clickEvent = {
                        recommendViewModel.resetRecommendState()
                    }
                )
            }
            is APIResult.Success -> {
                val responseData = response.resultData.body()
                if(responseData != null){
                    recommendViewModel.findPosterPath(responseData.recommendation)
                    MFTitle(text = "AI가 당신을 위해 추천한 작품은...")
                    Spacer(modifier = Modifier.padding(bottom = 12.dp))
                    RecommendResult(
                        responseData = responseData,
                        context = localContext,
                        posterPath = posterPathState.value,
                        resetRecommend = {
                            recommendViewModel.resetRecommendState()
                        }
                    )
                }else{
                    MFTitle(text = "응답 없음")
                    MFButton(
                        text = "돌아가기",
                        clickEvent = {
                            recommendViewModel.resetRecommendState()
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun AiLottie(){
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

@Composable
fun RecommendResult(
    responseData: ResponseRecommendationApiDto,
    posterPath: String,
    context: Context,
    resetRecommend: () -> Unit,
){
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        if(posterPath!=""){
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                model = ImageRequest.Builder(context)
                    .data("$BASE_TMDB_IMAGE_URL/$posterPath")
                    .crossfade(true)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.yoshicat)
                    .build(),
                contentDescription = "작품 이미지",
                contentScale = ContentScale.Fit
            )
        }
        Spacer(Modifier.padding(vertical = 12.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            MFTitle(responseData.recommendation.title.korean)
            Spacer(Modifier.padding(vertical = 6.dp))
            MFText(responseData.recommendation.year)
            MFText(responseData.recommendation.genre.joinToString(","))
            MFText("${responseData.recommendation.duration}분")
            MFText(responseData.recommendation.director)
            MFText(responseData.recommendation.mainCast.joinToString(", "))
        }
        Spacer(Modifier.padding(vertical = 6.dp))
        MFText("작품 요약 : ${responseData.summary}")
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        MFText("AI 예측 예상 점수 : ${responseData.recommendation.predictedScore}점")
        MFText("AI 추천 이유 : ${responseData.recommendation.recommendationReason}")
        MFButton(
            text = "돌아가기",
            clickEvent = {
                resetRecommend()
            }
        )
    }
}