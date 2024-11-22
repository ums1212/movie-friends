package org.comon.moviefriends.presenter.screen.community

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.common.showSnackBar
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.datasource.tmdb.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.presenter.common.clickableOnce
import org.comon.moviefriends.presenter.theme.FriendsBlack
import org.comon.moviefriends.presenter.viewmodel.MovieDetailViewModel
import org.comon.moviefriends.presenter.components.MFButtonWatchTogether
import org.comon.moviefriends.presenter.components.MFPostTitle
import org.comon.moviefriends.presenter.components.MFText
import org.comon.moviefriends.presenter.components.ShimmerEffect
import org.comon.moviefriends.presenter.components.UserWantListItem

@Composable
fun RequestListScreen(
    navigateToMovieDetail: (id:Int) -> Unit,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val user = MFPreferences.getUserInfo()

    LaunchedEffect(key1 = Unit) {
        viewModel.getUserInfo(user)
        viewModel.getMyRequestList()
    }

    val localContext = LocalContext.current
    val snackBarHost = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val requestList = viewModel.myChatRequestList.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(FriendsBlack),
    ) {
        MFPostTitle(text = stringResource(R.string.label_request_list), modifier = Modifier.padding(8.dp))
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        HorizontalDivider()
        SnackbarHost(hostState = snackBarHost)
        when(val list = requestList.value){
            APIResult.Loading -> RequestListShimmer()
            is APIResult.NetworkError -> {
                showSnackBar(coroutineScope, snackBarHost, localContext)
                Box(Modifier.fillMaxSize()){
                    MFText(modifier = Modifier.padding(8.dp), text = stringResource(id = R.string.network_error))
                }
            }
            APIResult.NoConstructor -> RequestListShimmer()
            is APIResult.Success -> {
                if(list.resultData.isEmpty()){
                    Box(Modifier.fillMaxSize()){
                        MFText(modifier = Modifier.padding(8.dp), text = stringResource(id = R.string.no_data))
                    }
                }else{
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(list.resultData.filterNotNull()){ item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .width(80.dp)
                                            .height(120.dp)
                                            .padding(4.dp)
                                            .clickableOnce {
                                                navigateToMovieDetail(item.movieId)
                                            },
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, Color.LightGray),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 8.dp,
                                            pressedElevation = 16.dp
                                        )
                                    ) {
                                        AsyncImage(
                                            modifier = Modifier.fillMaxWidth(),
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data("$BASE_TMDB_IMAGE_URL/${item.moviePosterPath}")
                                                .error(R.drawable.logo)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "작품 정보",
                                            contentScale = ContentScale.FillBounds
                                        )
                                    }
                                    UserWantListItem(item.receiveUser, item.receiveUserRegion)
                                }
                                val requestState = remember { mutableStateOf(true) }
                                MFButtonWatchTogether(
                                    clickEvent = { viewModel.requestWatchTogether(item.movieId, item.moviePosterPath, item.receiveUser, item.receiveUserRegion) },
                                    requestState = requestState,
                                    showErrorSnackBar = {},
                                    proposalFlag = item.proposalFlag
                                )
                            }
                            Spacer(Modifier.padding(vertical = 4.dp))
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RequestListShimmer(){
    Column {
        repeat(3){
            ShimmerEffect(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp))
        }
    }
}