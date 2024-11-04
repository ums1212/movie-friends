package org.comon.moviefriends.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.mahmoudalim.compose_rating_bar.RatingBarView
import org.comon.moviefriends.R
import org.comon.moviefriends.api.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.api.TMDBResult
import org.comon.moviefriends.common.showToast
import org.comon.moviefriends.model.ResponseCreditDto
import org.comon.moviefriends.model.TMDBMovieDetail
import org.comon.moviefriends.ui.theme.FriendsBlack
import org.comon.moviefriends.ui.theme.FriendsRed
import org.comon.moviefriends.ui.viewmodel.MovieDetailViewModel
import org.comon.moviefriends.ui.widget.MFBottomSheet
import org.comon.moviefriends.ui.widget.MFBottomSheetContent
import org.comon.moviefriends.ui.widget.MFButton
import org.comon.moviefriends.ui.widget.MFButtonWidthResizable
import org.comon.moviefriends.ui.widget.MFText
import org.comon.moviefriends.ui.widget.MovieCreditShimmer
import org.comon.moviefriends.ui.widget.MovieDetailShimmer
import org.comon.moviefriends.ui.widget.RateModal
import org.comon.moviefriends.ui.widget.UserWantListItem
import retrofit2.Response

@Composable
fun MovieDetailScreen(
    movieId: Int,
    viewModel: MovieDetailViewModel = viewModel()
) {

    val context = LocalContext.current

    val movieRating = remember { mutableIntStateOf(4) }
    val scrollState = rememberScrollState()

    val showRateModal = remember { mutableIntStateOf(0) }
    val showReviewBottomSheet = remember { mutableStateOf(false) }
    val showUserWantBottomSheet = remember { mutableStateOf(false) }

    val movieItem by viewModel.movieDetail.collectAsStateWithLifecycle()
    val movieCredit by viewModel.movieCredit.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllMovieInfo(movieId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        /** 영화 정보 */
        MovieDetailView(movieItem, context)

        /** 주요 출연진 */
        Spacer(Modifier.padding(vertical = 12.dp))
        MFText(stringResource(R.string.title_credits))
        MovieCreditView(movieCredit, context)

        /** 이 영화를 보고 싶다 */
        Spacer(Modifier.padding(vertical = 12.dp))
        MFButton({
            showToast(context, "이 영화를 보고 싶다")
        }, stringResource(R.string.button_want_this_movie))
        Spacer(Modifier.padding(vertical = 12.dp))
        MFText(stringResource(R.string.title_user_want_this_movie))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LazyRow {
                items(listOf("유저1", "유저2")) { item ->
                    UserWantListItem()
                }
            }
            MFButtonWidthResizable({
                showUserWantBottomSheet.value = true
            }, stringResource(R.string.button_more), 90.dp)
        }

        if(showUserWantBottomSheet.value){
            MFBottomSheet(MFBottomSheetContent.UserWantList) {
                showUserWantBottomSheet.value = false
            }
        }

        /** 유저 평점 */
        Spacer(Modifier.padding(vertical = 12.dp))
        MFText(stringResource(R.string.title_user_rate))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RatingBarView(
                isRatingEditable = false,
                rating = movieRating,
                ratedStarsColor = FriendsRed,
            )
        }

        MFButton({
            showRateModal.intValue = 1
        }, stringResource(R.string.button_user_rate))

        if (showRateModal.intValue == 1) {
            RateModal {
                showRateModal.intValue = 0
            }
        }

        /** 유저 리뷰 */
        Spacer(Modifier.padding(vertical = 12.dp))
        MFText(stringResource(R.string.title_user_review))
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .height(80.dp)
        ) {
            MFText("유저1: 너무 재밌어요")
            MFText("유저1: 너무 재밌어요")
            MFText("유저1: 너무 재밌어요")
        }
        MFButton({
            showReviewBottomSheet.value = true
        }, stringResource(R.string.button_more_user_review))

        if(showReviewBottomSheet.value){
            MFBottomSheet(MFBottomSheetContent.UserReview) {
                showReviewBottomSheet.value = false
            }
        }
    }
}

@Composable
private fun MovieDetailView(
    movieItem: TMDBResult<Response<TMDBMovieDetail>>,
    context: Context
) {
    when (movieItem) {
        is TMDBResult.Success -> {
            val item = movieItem.resultData.body()
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                model = ImageRequest.Builder(context)
                    .data("${BASE_TMDB_IMAGE_URL}/${item?.posterPath}")
                    .crossfade(true)
                    .error(R.drawable.yoshicat)
                    .build(),
                contentDescription = "작품 이미지",
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.padding(vertical = 12.dp))
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                MFText(item?.title ?: "")
                MFText("${item?.releaseDate} / ${item?.genres?.joinToString(",") { it.name }} / ${item?.runtime}분")
                Spacer(Modifier.padding(vertical = 4.dp))
                MFText(item?.overview ?: "")
            }

        }

        is TMDBResult.Loading -> {
            MovieDetailShimmer()
        }

        is TMDBResult.NetworkError -> {
            MovieDetailShimmer()
        }

        else -> {
            MovieDetailShimmer()
        }
    }
}

@Composable
private fun MovieCreditView(
    creditList: TMDBResult<Response<ResponseCreditDto>>,
    context: Context
) {
    val mutableList = remember { listOf<ResponseCreditDto.Cast>() }
    val stateLists = remember { mutableStateOf(mutableList) }

    when (creditList) {
        is TMDBResult.Success -> {
            creditList.resultData.body()?.cast?.let {
                stateLists.value = it
                LazyRow {
                    items(stateLists.value){ item ->
                        Column(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable { },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(128.dp)
                                    .padding(end = 4.dp),
                                model = ImageRequest.Builder(context)
                                    .data("${BASE_TMDB_IMAGE_URL}${item.profilePath}")
                                    .crossfade(true)
                                    .error(R.drawable.yoshicat)
                                    .build(),
                                contentDescription = "회원 프로필 사진",
                            )
                            MFText(
                                "${item.character} 역",
                                Modifier.width(128.dp),
                                TextStyle(
                                    fontSize = 14.sp
                                )
                            )
                            MFText(
                                item.name,
                                Modifier.width(128.dp),
                                TextStyle(
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        is TMDBResult.Loading -> {
            MovieCreditShimmer()
        }

        is TMDBResult.NetworkError -> {
            MovieCreditShimmer()
        }

        else -> {
            MovieCreditShimmer()
        }
    }
}