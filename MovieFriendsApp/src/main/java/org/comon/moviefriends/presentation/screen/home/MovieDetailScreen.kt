package org.comon.moviefriends.presentation.screen.home

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.mahmoudalim.compose_rating_bar.RatingBarView
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.SimpleYouTubePlayerOptionsBuilder
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayer
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayerHostState
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayerState
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubeVideoId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.common.showSnackBar
import org.comon.moviefriends.data.datasource.tmdb.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.tmdb.ResponseCreditDto
import org.comon.moviefriends.data.entity.tmdb.ResponseMovieDetailDto
import org.comon.moviefriends.data.entity.tmdb.ResponseMovieVideoDto
import org.comon.moviefriends.presentation.common.clickableOnce
import org.comon.moviefriends.presentation.theme.FriendsBlack
import org.comon.moviefriends.presentation.theme.FriendsRed
import org.comon.moviefriends.presentation.viewmodel.MovieDetailViewModel
import org.comon.moviefriends.presentation.components.MFButton
import org.comon.moviefriends.presentation.components.MFButtonWantThisMovie
import org.comon.moviefriends.presentation.components.MFButtonWidthResizable
import org.comon.moviefriends.presentation.components.MFTitle
import org.comon.moviefriends.presentation.components.MFReviewBottomSheet
import org.comon.moviefriends.presentation.components.MFText
import org.comon.moviefriends.presentation.components.MFWantMovieBottomSheet
import org.comon.moviefriends.presentation.components.MovieCreditShimmer
import org.comon.moviefriends.presentation.components.MovieDetailShimmer
import org.comon.moviefriends.presentation.components.RateModal
import org.comon.moviefriends.presentation.components.ShimmerEffect
import org.comon.moviefriends.presentation.components.UserWantListItem
import retrofit2.Response

@Composable
fun MovieDetailScreen(
    movieId: Int,
    navigatePop: () -> Unit,
    navigateToLogin: () -> Unit,
    viewModel: MovieDetailViewModel = hiltViewModel(),
) {
    val localContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    val snackBarHost = remember { SnackbarHostState() }

    val movieItem by viewModel.movieDetail.collectAsStateWithLifecycle()
    val movieCredit by viewModel.movieCredit.collectAsStateWithLifecycle()
    val movieVideo by viewModel.movieVideo.collectAsStateWithLifecycle()
    val wantThisMovieState by viewModel.wantThisMovieState.collectAsStateWithLifecycle()
    val userWantBottomSheetState by viewModel.userWantBottomSheetState.collectAsStateWithLifecycle()
    val rateModalState by viewModel.rateModalState.collectAsStateWithLifecycle()
    val reviewBottomSheetState by viewModel.reviewBottomSheetState.collectAsStateWithLifecycle()
    val userWantList by viewModel.userWantList.collectAsStateWithLifecycle()
    val movieInfo by viewModel.movieInfo.collectAsStateWithLifecycle()
    val userReviewList by viewModel.userReviews.collectAsStateWithLifecycle()

    val isPlayerShown = remember { mutableStateOf(false) }
    val videoKey = remember { mutableStateOf("") }

    BackHandler {
        // 유튜브 플레이어가 떠있을 경우에는 플레이어를 종료
        if(isPlayerShown.value){
            isPlayerShown.value = false
            return@BackHandler
        }
        navigatePop()
    }

    LaunchedEffect(Unit) {
        viewModel.getMovieId(movieId)
        viewModel.getUserInfo(MFPreferences.getUserInfo())
        viewModel.getAllMovieInfo()
    }

    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        /** 영화 정보 */
        MovieDetailView(
            movieItem = movieItem,
            setMovieInfo = { movieInfo -> viewModel.setMovieInfo(movieInfo) },
            context = localContext
        )

        /** 이 영화를 보고 싶다 */
        Spacer(Modifier.padding(vertical = 4.dp))
        MFButtonWantThisMovie(
            clickEvent = {
                viewModel.changeWantThisMovieStateLoading()
                viewModel.changeStateWantThisMovie(localContext, navigateToLogin)
            },
            text = stringResource(R.string.button_want_this_movie),
            isChecked = wantThisMovieState,
            showErrorMessage = {
                coroutineScope.launch {
                    snackBarHost.showSnackbar(
                        localContext.getString(R.string.network_error),
                        null,
                        true,
                        SnackbarDuration.Short
                    )
                }
            }
        )

        /** 주요 출연진 */
        Spacer(Modifier.padding(vertical = 12.dp))
        MFTitle(stringResource(R.string.title_credits))
        MovieDetailListView(
            MovieDetailListViewType.CREDIT,
            movieCredit,
            localContext,
            null,
            null,
            null
        )

        /** 관련 영상 */
        Spacer(Modifier.padding(vertical = 12.dp))
        MFTitle(stringResource(R.string.title_videos))
        MovieDetailListView(
            MovieDetailListViewType.VIDEO,
            movieVideo,
            localContext,
            isPlayerShown,
            videoKey,
            movieInfo?.posterPath,
        )

        /** 이 영화를 보고 싶은 사람 */
        Spacer(Modifier.padding(vertical = 12.dp))
        MFTitle(stringResource(R.string.title_user_want_this_movie))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if(viewModel.userWantListState.value){
                if(userWantList.isEmpty()){
                    MFText(stringResource(id = R.string.no_data))
                }else{
                    val previewList = userWantList.take(2)
                    Row(
                        modifier = Modifier
                            .weight(0.7f),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        previewList.forEach { item ->
                            if(item != null){
                                UserWantListItem(item.userInfo, item.userLocation)
                            }
                        }
                    }
                    MFButtonWidthResizable(
                        { viewModel.toggleUserWantBottomSheetState(navigateToLogin) },
                        stringResource(R.string.button_more),
                        90.dp
                    )
                }
            }else{
                ShimmerEffect(modifier = Modifier.fillMaxWidth())
            }
        }

        if(userWantBottomSheetState){
            MFWantMovieBottomSheet(
                dismissSheet = { viewModel.toggleUserWantBottomSheetState(navigateToLogin) },
            )
        }

        /** 유저 평점 */
        Spacer(Modifier.padding(vertical = 12.dp))
        MFTitle(stringResource(R.string.title_user_rate))
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
            viewModel.toggleRateModalState(navigateToLogin)
        }, stringResource(R.string.button_user_rate))

        if (rateModalState) {
            RateModal(
                dismissModal = { viewModel.toggleRateModalState(navigateToLogin) },
                userRate = viewModel.userMovieRatingState,
                voteUserRate = { star ->
                    viewModel.voteUserRate(star, navigateToLogin)
                }
            )
        }

        /** 유저 리뷰 */
        Spacer(Modifier.padding(vertical = 12.dp))
        MFTitle(stringResource(R.string.title_user_review))
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .height(80.dp)
        ) {
            when(val list = userReviewList){
                APIResult.Loading -> CircularProgressIndicator()
                is APIResult.Success -> {
                    if(list.resultData.isEmpty()){
                        MFText(stringResource(R.string.no_data))
                    }else{
                        list.resultData.take(3).forEach { item ->
                            if(item != null)
                                MFText(
                                    text = "${item.user.nickName}: ${item.content}",
                                    overflow = TextOverflow.Ellipsis
                                )
                        }
                    }
                }
                else -> MFText(stringResource(R.string.no_data))
            }
        }
        MFButton({
            viewModel.toggleReviewBottomSheetState()
            viewModel.getUserReview()
        }, stringResource(R.string.button_more_user_review))

        if(reviewBottomSheetState){
            MFReviewBottomSheet(
                dismissSheet= { viewModel.toggleReviewBottomSheetState() },
            )
        }
    }
    if(isPlayerShown.value){
        MFYouTubePlayer(videoKey.value, snackBarHost, localContext, isPlayerShown)
    }
    SnackbarHost(snackBarHost)
}

@Composable
private fun MovieDetailView(
    movieItem: APIResult<Response<ResponseMovieDetailDto>>,
    setMovieInfo: (ResponseMovieDetailDto) -> Unit,
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
                MFTitle(item?.title ?: "")
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
enum class MovieDetailListViewType {
    CREDIT,
    VIDEO,
}
@Composable
private fun <T> MovieDetailListView(
    listViewType: MovieDetailListViewType,
    creditList: APIResult<Response<T>>,
    context: Context,
    isPlayerShown: MutableState<Boolean>?,
    videoKey: MutableState<String>?,
    posterLink: String?,
) {
    val stateLists = remember { mutableStateOf(emptyList<Any>()) }

    when (creditList) {
        is APIResult.Success -> {
            val resultBody = creditList.resultData.body()
            when (listViewType) {
                MovieDetailListViewType.CREDIT -> {
                    stateLists.value = (resultBody as ResponseCreditDto).cast.filter { cast ->
                        cast.order < 8
                    }
                }
                MovieDetailListViewType.VIDEO -> {
                    stateLists.value = (resultBody as ResponseMovieVideoDto).results
                }
            }
            if(stateLists.value.isEmpty()){
                MFText(stringResource(R.string.no_data))
            }
            LazyRow {
                items(stateLists.value){ item ->
                    Column(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .height(180.dp)
                            .clickableOnce {
                                when (listViewType) {
                                    MovieDetailListViewType.CREDIT -> {}
                                    MovieDetailListViewType.VIDEO -> {
                                        isPlayerShown?.value = true
                                        videoKey?.value = (item as ResponseMovieVideoDto.Result).key
                                    }
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (listViewType) {
                            MovieDetailListViewType.CREDIT -> {
                                CreditItemView(item as ResponseCreditDto.Cast, context)
                            }
                            MovieDetailListViewType.VIDEO -> {
                                VideoItemView(item as ResponseMovieVideoDto.Result, posterLink, context)
                            }
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

@Composable
fun CreditItemView(item: ResponseCreditDto.Cast, context: Context) {
    AsyncImage(
        modifier = Modifier
            .size(128.dp)
            .padding(end = 4.dp),
        model = ImageRequest.Builder(context)
            .data("$BASE_TMDB_IMAGE_URL${item.profilePath}")
            .crossfade(true)
            .error(R.drawable.yoshicat)
            .build(),
        contentDescription = "사진",
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

fun loadVideo(scope: CoroutineScope, hostState: YouTubePlayerHostState, videoKey: String){
    scope.launch {
        hostState.loadVideo(YouTubeVideoId(videoKey))
    }
}

@Composable
fun MFYouTubePlayer(
    videoKey: String,
    snackBarHost: SnackbarHostState,
    localContext: Context,
    isPlayerShown: MutableState<Boolean>
){
    val hostState = remember { YouTubePlayerHostState() }
    val coroutineScope = rememberCoroutineScope()

    when(hostState.currentState) {
        is YouTubePlayerState.Error -> showSnackBar(coroutineScope, snackBarHost, localContext){
            isPlayerShown.value = false
        }
        YouTubePlayerState.Idle -> {
            // Do nothing, waiting for initialization
        }
        is YouTubePlayerState.Playing -> {
            // Update UI button states
        }
        YouTubePlayerState.Ready -> loadVideo(coroutineScope, hostState, videoKey)
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(FriendsBlack.copy(alpha = 0.7f))
            .clickableOnce {
                isPlayerShown.value = false
            }
    ){
        YouTubePlayer(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(300.dp),
            hostState = hostState,
            options = SimpleYouTubePlayerOptionsBuilder.builder {
                autoplay(false)
                controls(false)
                rel(false)
                ivLoadPolicy(false)
                ccLoadPolicy(false)
                fullscreen = true
            },
        )
    }
}

@Composable
fun VideoItemView(item: ResponseMovieVideoDto.Result, posterLink: String?, context: Context) {

    Box(
        modifier = Modifier.size(128.dp)
    ){
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 4.dp),
            model = ImageRequest.Builder(context)
                .data("$BASE_TMDB_IMAGE_URL${posterLink}")
                .crossfade(true)
                .error(R.drawable.yoshicat)
                .build(),
            contentDescription = "사진",
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FriendsBlack.copy(alpha = 0.7f)),
        ){
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = "재생"
            )
        }
    }

    MFText(
        item.name,
        Modifier.width(128.dp),
        TextStyle(
            fontSize = 12.sp
        )
    )
//    MFText(
//        item.name,
//        Modifier.width(128.dp),
//        TextStyle(
//            fontSize = 12.sp
//        )
//    )
}