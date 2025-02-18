package org.comon.moviefriends.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.comon.moviefriends.R
import org.comon.moviefriends.common.FullScreenNavRoute
import org.comon.moviefriends.common.showSnackBar
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.presentation.viewmodel.MovieDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MFWantMovieBottomSheet(
    dismissSheet: () -> Unit,
    movieDetailViewModel: MovieDetailViewModel = hiltViewModel(),
) {

    val localContext = LocalContext.current
    val snackBarHost = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val userWantList = movieDetailViewModel.userWantList.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        movieDetailViewModel.getUserWantList()
    }

    ModalBottomSheet(onDismissRequest = dismissSheet) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            MFTitle(stringResource(R.string.title_user_want_this_movie))
            if(userWantList.value.isEmpty()){
                MFText(text = stringResource(id = R.string.no_data))
            }else{
                UserWantThisMovieList(
                    screen = FullScreenNavRoute.MovieDetail.route,
                    wantList = userWantList.value,
                    requestWatchTogether = { requestChatInfo ->
                        movieDetailViewModel.requestWatchTogether(requestChatInfo)
                    },
                    showErrorSnackBar = { showSnackBar(coroutineScope, snackBarHost, localContext) }
                )
            }
        }
        SnackbarHost(snackBarHost)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MFReviewBottomSheet(
    dismissSheet: () -> Unit,
    viewModel: MovieDetailViewModel = hiltViewModel(),
) {

    val userReviewList = viewModel.userReviews.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getUserReview()
    }

    ModalBottomSheet(onDismissRequest = dismissSheet) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            MFTitle(stringResource(R.string.title_user_review))
            when(val list = userReviewList.value){
                APIResult.NoConstructor -> MFTitle(text = stringResource(id = R.string.no_data))
                APIResult.Loading -> LinearProgressIndicator()
                is APIResult.NetworkError -> MFTitle(text = stringResource(id = R.string.network_error))
                is APIResult.Success -> {
                    UserReviewList(
                        reviewList = list.resultData,
                        insertUserReview = { viewModel.insertUserReview() },
                        deleteUserReview = { reviewId -> viewModel.deleteUserReview(reviewId) },
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}