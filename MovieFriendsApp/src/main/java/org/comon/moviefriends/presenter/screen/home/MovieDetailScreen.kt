package org.comon.moviefriends.presenter.screen.home

import android.content.Context
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.data.datasource.tmdb.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.ResponseCreditDto
import org.comon.moviefriends.data.model.TMDBMovieDetail
import org.comon.moviefriends.data.model.UserInfo
import org.comon.moviefriends.data.model.UserWantMovieInfo
import org.comon.moviefriends.presenter.common.clickableOnce
import org.comon.moviefriends.presenter.theme.FriendsRed
import org.comon.moviefriends.presenter.viewmodel.MovieDetailViewModel
import org.comon.moviefriends.presenter.widget.DetailTopAppBar
import org.comon.moviefriends.presenter.widget.MFBottomSheet
import org.comon.moviefriends.presenter.widget.MFBottomSheetContent
import org.comon.moviefriends.presenter.widget.MFButton
import org.comon.moviefriends.presenter.widget.MFButtonWantThisMovie
import org.comon.moviefriends.presenter.widget.MFButtonWidthResizable
import org.comon.moviefriends.presenter.widget.MFPostTitle
import org.comon.moviefriends.presenter.widget.MFText
import org.comon.moviefriends.presenter.widget.MovieCreditShimmer
import org.comon.moviefriends.presenter.widget.MovieDetailShimmer
import org.comon.moviefriends.presenter.widget.RateModal
import org.comon.moviefriends.presenter.widget.UserWantListItem
import retrofit2.Response

@Composable
fun MovieDetailScreen(
    movieId: Int,
    viewModel: MovieDetailViewModel = viewModel(),
    navigatePop: () -> Unit
) {
    val context = LocalContext.current

    val scrollState = rememberScrollState()

    val movieItem by viewModel.movieDetail.collectAsStateWithLifecycle()
    val movieCredit by viewModel.movieCredit.collectAsStateWithLifecycle()
    val wantThisMovieState by viewModel.wantThisMovieState.collectAsStateWithLifecycle()
    val userWantBottomSheetState by viewModel.userWantBottomSheetState.collectAsStateWithLifecycle()
    val rateModalState by viewModel.rateModalState.collectAsStateWithLifecycle()
    val reviewBottomSheetState by viewModel.reviewBottomSheetState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getMovieId(movieId)
        viewModel.getUserInfo(MFPreferences.getUserInfo(context))
        viewModel.getAllMovieInfo()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { DetailTopAppBar(navigatePop) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(12.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            /** 영화 정보 */
            MovieDetailView(
                movieItem = movieItem,
                setMovieInfo = { movieInfo -> viewModel.setMovieInfo(movieInfo) },
                context = context
            )

            /** 주요 출연진 */
            Spacer(Modifier.padding(vertical = 12.dp))
            MFPostTitle(stringResource(R.string.title_credits))
            MovieCreditView(movieCredit, context)

            /** 이 영화를 보고 싶다 */
            Spacer(Modifier.padding(vertical = 4.dp))
            MFButtonWantThisMovie(
                { viewModel.changeStateWantThisMovie() },
                stringResource(R.string.button_want_this_movie),
                wantThisMovieState
            )
            Spacer(Modifier.padding(vertical = 12.dp))
            MFPostTitle(stringResource(R.string.title_user_want_this_movie))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LazyRow {
                    items(
                        listOf(
                            UserWantMovieInfo(userInfo = UserInfo(nickName = "유저1", profileImage = "", joinType = "")),
                            UserWantMovieInfo(userInfo = UserInfo(nickName = "유저2", profileImage = "", joinType = "")),
                        )
                    ) { item ->
                        UserWantListItem(item.userInfo, item.userDistance)
                    }
                }
                MFButtonWidthResizable(
                    { viewModel.toggleUserWantBottomSheetState() },
                    stringResource(R.string.button_more),
                    90.dp
                )
            }

            if(userWantBottomSheetState){
                MFBottomSheet(MFBottomSheetContent.UserWantList) {
                    viewModel.toggleUserWantBottomSheetState()
                }
            }

            /** 유저 평점 */
            Spacer(Modifier.padding(vertical = 12.dp))
            MFPostTitle(stringResource(R.string.title_user_rate))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RatingBarView(
                    isRatingEditable = false,
                    rating = viewModel.mfAllUserMovieRatingState,
                    ratedStarsColor = FriendsRed,
                )
            }

            MFButton({
                viewModel.toggleRateModalState()
            }, stringResource(R.string.button_user_rate))

            if (rateModalState) {
                RateModal(
                    dismissModal = { viewModel.toggleRateModalState() },
                    userRate = viewModel.userMovieRatingState,
                    voteUserRate = { star ->
                        viewModel.voteUserRate(star)
                    }
                )
            }

            /** 유저 리뷰 */
            Spacer(Modifier.padding(vertical = 12.dp))
            MFPostTitle(stringResource(R.string.title_user_review))
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                MFText("유저1: 너무 재밌어요")
                MFText("유저1: 너무 재밌어요")
                MFText("유저1: 너무 재밌어요")
                MFText("...")
            }
            MFButton({
                viewModel.toggleReviewBottomSheetState()
            }, stringResource(R.string.button_more_user_review))

            if(reviewBottomSheetState){
                MFBottomSheet(MFBottomSheetContent.UserReview) {
                    viewModel.toggleReviewBottomSheetState()
                }
            }
        }
    }
}

@Composable
private fun MovieDetailView(
    movieItem: APIResult<Response<TMDBMovieDetail>>,
    setMovieInfo: (TMDBMovieDetail) -> Unit,
    context: Context
) {
    when (movieItem) {
        is APIResult.Success -> {
            val item = movieItem.resultData.body()
            item?.let(setMovieInfo)
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                model = ImageRequest.Builder(context)
                    .data("$BASE_TMDB_IMAGE_URL/${item?.posterPath}")
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
                MFPostTitle(item?.title ?: "")
                MFText("${item?.releaseDate} / ${item?.genres?.joinToString(",") { it.name }} / ${item?.runtime}분")
                Spacer(Modifier.padding(vertical = 4.dp))
                MFText(item?.overview ?: "")
            }

        }

        is APIResult.Loading -> {
            MovieDetailShimmer()
        }

        is APIResult.NetworkError -> {
            MovieDetailShimmer()
        }

        else -> {
            MovieDetailShimmer()
        }
    }
}

@Composable
private fun MovieCreditView(
    creditList: APIResult<Response<ResponseCreditDto>>,
    context: Context
) {
    val mutableList = remember { listOf<ResponseCreditDto.Cast>() }
    val stateLists = remember { mutableStateOf(mutableList) }

    when (creditList) {
        is APIResult.Success -> {
            creditList.resultData.body()?.cast?.let {
                stateLists.value = it.filter { cast ->
                    cast.order < 8
                }
                LazyRow {
                    items(stateLists.value){ item ->
                        Column(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .height(180.dp)
                                .clickableOnce { },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(128.dp)
                                    .padding(end = 4.dp),
                                model = ImageRequest.Builder(context)
                                    .data("$BASE_TMDB_IMAGE_URL${item.profilePath}")
                                    .crossfade(true)
                                    .error(R.drawable.yoshicat)
                                    .build(),
                                contentDescription = "회원 프로필 사진",
                            )
                            MFText(
                                "${item.character} 역",
                                Modifier.width(128.dp),
                                TextStyle(
                                    fontSize = 12.sp
                                )
                            )
                            MFText(
                                item.name,
                                Modifier.width(128.dp),
                                TextStyle(
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        is APIResult.Loading -> {
            MovieCreditShimmer()
        }

        is APIResult.NetworkError -> {
            MovieCreditShimmer()
        }

        else -> {
            MovieCreditShimmer()
        }
    }
}