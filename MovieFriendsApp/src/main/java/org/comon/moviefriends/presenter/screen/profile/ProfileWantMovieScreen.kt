package org.comon.moviefriends.presenter.screen.profile

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import kotlinx.coroutines.launch
import org.comon.moviefriends.R
import org.comon.moviefriends.presenter.common.clickableOnce
import org.comon.moviefriends.presenter.theme.FriendsTextGrey
import org.comon.moviefriends.presenter.viewmodel.MovieDetailViewModel
import org.comon.moviefriends.presenter.widget.MFButtonWantThisMovie
import org.comon.moviefriends.presenter.widget.MFPostTitle
import org.comon.moviefriends.presenter.widget.MFText

@Composable
fun ProfileWantMovieScreen(
    viewModel: MovieDetailViewModel = viewModel(),
    navigateToLogin: () -> Unit,
) {
    val pagerState = rememberPagerState(0, pageCount = { 10 })
    val localContext = LocalContext.current
    val animationScope = rememberCoroutineScope()
    val snackBarHost = remember { SnackbarHostState() }

    val wantThisMovieState by viewModel.wantThisMovieState.collectAsStateWithLifecycle()

//    OnDevelopMark()
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        MFPostTitle(stringResource(R.string.button_profile_want_movie))
        Spacer(Modifier.padding(vertical = 16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            IconButton(
                onClick = {
                    animationScope.launch {
                        if(pagerState.currentPage!=0){
                            pagerState.animateScrollToPage(pagerState.currentPage-1)
                        }
                    }
                          },
            ) {
                Icon(
                    modifier = Modifier.size(64.dp),
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "왼쪽 스크롤",
                    tint = FriendsTextGrey
                )
            }
            HorizontalPager(
                modifier = Modifier.width(200.dp),
                state = pagerState,
                pageSize = PageSize.Fixed(200.dp)
            ) {
                Card(
                    modifier = Modifier
                        .width(200.dp)
                        .height(300.dp)
                        .padding(4.dp)
                        .clickableOnce {},
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 16.dp
                    )
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("")
                            .error(R.drawable.logo)
                            .crossfade(true)
                            .build(),
                        contentDescription = "작품 정보",
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
            IconButton(
                onClick = {
                    animationScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage+1)
                    }
                },
            ) {
                Icon(
                    modifier = Modifier.size(64.dp),
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "오른쪽 스크롤",
                    tint = FriendsTextGrey
                )
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MFPostTitle("영화 제목")
            MFText("개봉연도 / 장르 / 상영시간")
        }

        MFButtonWantThisMovie(
            { viewModel.changeStateWantThisMovie(emptyList(), navigateToLogin) },
            stringResource(R.string.button_want_this_movie),
            wantThisMovieState,
            showErrorMessage = {
                animationScope.launch {
                    snackBarHost.showSnackbar(
                        localContext.getString(R.string.network_error),
                        null,
                        true,
                        SnackbarDuration.Short
                    )
                }
            }
        )
    }
    SnackbarHost(snackBarHost)
}