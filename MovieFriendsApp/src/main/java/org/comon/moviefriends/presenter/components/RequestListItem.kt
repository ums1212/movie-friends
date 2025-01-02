package org.comon.moviefriends.presenter.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.R
import org.comon.moviefriends.common.getDateString
import org.comon.moviefriends.common.showSnackBar
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.datasource.tmdb.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.data.entity.firebase.ProposalFlag
import org.comon.moviefriends.data.entity.firebase.RequestChatInfo
import org.comon.moviefriends.presenter.common.clickableOnce
import org.comon.moviefriends.presenter.viewmodel.WatchTogetherViewModel

@Composable
fun RequestListItem(
    coroutineScope: CoroutineScope,
    snackBarHost: SnackbarHostState,
    localContext: Context,
    navigateToMovieDetail: (id:Int) -> Unit,
    watchTogetherViewModel: WatchTogetherViewModel,
    item: RequestChatInfo
){
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.End
    ){
        Spacer(Modifier.padding(vertical = 8.dp))
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
            val loadingState = remember { mutableStateOf(false) }
            MFButtonWatchTogether(
                clickEvent = {
                    coroutineScope.launch {
                        watchTogetherViewModel.requestWatchTogether(item).collectLatest {
                            when(it){
                                APIResult.Loading -> {
                                    loadingState.value = true
                                }
                                is APIResult.NetworkError -> {
                                    loadingState.value = false
                                    showSnackBar(coroutineScope, snackBarHost, localContext)
                                }
                                is APIResult.Success -> {
                                    requestState.value = it.resultData
                                    loadingState.value = false
                                }
                                else -> loadingState.value = false
                            }
                        }
                    }
                },
                loadingState = loadingState,
                requestState = requestState,
                proposalFlag = when(item.proposalFlag){
                    ProposalFlag.DENIED.str -> ProposalFlag.DENIED
                    ProposalFlag.CONFIRMED.str -> ProposalFlag.CONFIRMED
                    else -> ProposalFlag.WAITING
                }
            )
        }
        MFPostDate(getDateString(item.createdDate.seconds))
    }
    Spacer(Modifier.padding(vertical = 4.dp))
    HorizontalDivider()
}