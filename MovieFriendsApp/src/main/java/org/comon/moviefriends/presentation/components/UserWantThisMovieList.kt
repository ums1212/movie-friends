package org.comon.moviefriends.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.common.ScaffoldNavRoute
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.datasource.tmdb.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.data.entity.firebase.RequestChatInfo
import org.comon.moviefriends.data.entity.firebase.UserWantMovieInfo
import org.comon.moviefriends.presentation.common.clickableOnce

/** 이 영화를 보고 싶은 사람 목록 화면 */
@Composable
fun UserWantThisMovieList(
    screen: String,
    wantList: List<UserWantMovieInfo?>,
    navigateToMovieDetail: ((id:Int) -> Unit)? = null,
    requestWatchTogether: suspend (requestChatInfo: RequestChatInfo) -> Flow<APIResult<Boolean>>,
    showErrorSnackBar: () -> Unit,
){

    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        wantList.forEach { wantMovieInfo ->
            if(wantMovieInfo==null) return@forEach
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        if(screen==ScaffoldNavRoute.WatchTogether.route){
                            Card(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(120.dp)
                                    .padding(4.dp)
                                    .clickableOnce {
                                        navigateToMovieDetail!!(wantMovieInfo.movieId)
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
                                        .data("$BASE_TMDB_IMAGE_URL${wantMovieInfo.moviePosterPath}")
                                        .error(R.drawable.logo)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "작품 정보",
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }
                        UserWantListItem(wantMovieInfo.userInfo, wantMovieInfo.userLocation)
                    }

                    val requestState = remember { mutableStateOf(false) }
                    val loadingState = remember { mutableStateOf(false) }
                    val createdRequestInfo = remember { RequestChatInfo(
                        wantMovieInfoId = wantMovieInfo.id,
                        movieId = wantMovieInfo.movieId,
                        moviePosterPath = wantMovieInfo.moviePosterPath,
                        sendUser = MFPreferences.getUserInfo()!!,
                        receiveUser = wantMovieInfo.userInfo,
                        receiveUserRegion = wantMovieInfo.userLocation
                    ) }
                    MFButtonWatchTogether(
                        clickEvent = {
                            coroutineScope.launch {
                                requestWatchTogether(createdRequestInfo).collectLatest {
                                    when(it){
                                        APIResult.Loading -> {
                                            loadingState.value = true
                                        }
                                        is APIResult.NetworkError -> {
                                            loadingState.value = false
                                            showErrorSnackBar()
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
                        requestState = requestState,
                        loadingState = loadingState,
                    )
                }
                Spacer(Modifier.padding(bottom = 8.dp))
            }
        }
    }
}