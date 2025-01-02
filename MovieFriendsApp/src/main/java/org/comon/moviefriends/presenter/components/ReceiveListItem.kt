package org.comon.moviefriends.presenter.components

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.R
import org.comon.moviefriends.common.getDateString
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.datasource.tmdb.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.data.entity.firebase.ProposalFlag
import org.comon.moviefriends.data.entity.firebase.RequestChatInfo
import org.comon.moviefriends.presenter.common.clickableOnce

@Composable
fun ReceiveListItem(
    navigateToMovieDetail: (Int) -> Unit,
    confirmRequest: suspend () -> Flow<APIResult<Boolean>>,
    denyRequest: suspend () -> Flow<APIResult<Boolean>>,
    requestState: MutableState<RequestState>,
    showSnackBar: () -> Unit,
    item: RequestChatInfo,
){
    val coroutineScope = rememberCoroutineScope()
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
                UserWantListItem(item.sendUser, item.receiveUserRegion)
            }
            when(requestState.value){
                RequestState.NoConstructor -> {
                    if(item.proposalFlag== ProposalFlag.WAITING.str){
                        Column {
                            MFButtonReceive(stringResource(R.string.label_receive_confirm)){
                                requestCollect(true, coroutineScope, confirmRequest, requestState, showSnackBar)
                            }
                            MFButtonReceive(stringResource(R.string.label_receive_deny)){
                                requestCollect(false, coroutineScope, denyRequest, requestState, showSnackBar)
                            }
                        }
                    }else{
                        MFText(text = item.proposalFlag)
                    }
                }
                RequestState.Loading -> CircularProgressIndicator()
                RequestState.Denied -> MFText(stringResource(R.string.button_request_denied))
                RequestState.Confirmed -> MFText(stringResource(R.string.button_request_confirmed))
            }
        }
        MFPostDate(getDateString(item.createdDate.seconds))
    }
    Spacer(Modifier.padding(vertical = 4.dp))
    HorizontalDivider()
}

fun requestCollect(
    isRequestConfirmed: Boolean,
    coroutineScope: CoroutineScope,
    request: suspend () -> Flow<APIResult<Boolean>>,
    requestState: MutableState<RequestState>,
    showSnackBar: () -> Unit
){
    coroutineScope.launch {
        request().collectLatest {
            when(it){
                APIResult.NoConstructor -> requestState.value = RequestState.NoConstructor
                APIResult.Loading -> requestState.value = RequestState.Loading
                is APIResult.NetworkError -> showSnackBar()
                is APIResult.Success -> {
                    if(isRequestConfirmed){
                        requestState.value = RequestState.Confirmed
                    }else{
                        requestState.value = RequestState.Denied
                    }
                }
            }
        }
    }
}

sealed class RequestState {
    data object NoConstructor : RequestState()
    data object Loading : RequestState()
    data object Denied : RequestState()
    data object Confirmed : RequestState()
}