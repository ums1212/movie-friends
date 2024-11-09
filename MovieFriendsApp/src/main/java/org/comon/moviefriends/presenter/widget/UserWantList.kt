package org.comon.moviefriends.presenter.widget

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
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
import kotlinx.coroutines.launch
import org.comon.moviefriends.R
import org.comon.moviefriends.common.COMMUNITY_MENU
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.datasource.tmdb.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.data.model.UserInfo
import org.comon.moviefriends.data.model.UserWantMovieInfo
import org.comon.moviefriends.presenter.common.clickableOnce

/** 이 영화를 보고 싶은 사람 목록 화면 */
@Composable
fun UserWantThisMovieList(
    screen: String,
    wantList: List<UserWantMovieInfo?> = listOf(
        UserWantMovieInfo(movieId = 1034541, userInfo = UserInfo(nickName = "유저1", profileImage = "", joinType = "")),
        UserWantMovieInfo(movieId = 912649, userInfo = UserInfo(nickName = "유저2", profileImage = "", joinType = "")),
        UserWantMovieInfo(movieId = 1184918, userInfo = UserInfo(nickName = "유저3", profileImage = "", joinType = "")),
        UserWantMovieInfo(movieId = 933260, userInfo = UserInfo(nickName = "유저4", profileImage = "", joinType = "")),
        UserWantMovieInfo(movieId = 698687, userInfo = UserInfo(nickName = "유저5", profileImage = "", joinType = "")),
        UserWantMovieInfo(movieId = 889737, userInfo = UserInfo(nickName = "유저6", profileImage = "", joinType = "")),
        UserWantMovieInfo(movieId = 1084736, userInfo = UserInfo(nickName = "유저7", profileImage = "", joinType = "")),
        UserWantMovieInfo(movieId = 976734, userInfo = UserInfo(nickName = "유저8", profileImage = "", joinType = "")),
        UserWantMovieInfo(movieId = 1079091, userInfo = UserInfo(nickName = "유저9", profileImage = "", joinType = "")),
        UserWantMovieInfo(movieId = 1196470, userInfo = UserInfo(nickName = "유저10", profileImage = "", joinType = "")),
    ),
    navigateToMovieDetail: ((id:Int) -> Unit)?,
    requestWatchTogether: (receiveUser: UserInfo) -> Flow<APIResult<Boolean>>,
    ){

    val localContext = LocalContext.current
    val snackBarHost = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    SnackbarHost(snackBarHost)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(wantList){ want ->
            if(want!=null){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        if(screen==COMMUNITY_MENU.WATCH_TOGETHER.route){
                            Card(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(120.dp)
                                    .padding(4.dp)
                                    .clickableOnce {
                                        navigateToMovieDetail!!(want.movieId)
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
                                        .data("$BASE_TMDB_IMAGE_URL${want.moviePosterPath}")
                                        .error(R.drawable.logo)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "작품 정보",
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }
                        UserWantListItem(want.userInfo, want.userLocation)
                    }

                    MFButtonWatchTogether(
                        coroutineScope = coroutineScope,
                        clickEvent = requestWatchTogether(want.userInfo),
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
                }
                Spacer(Modifier.padding(bottom = 8.dp))
            }
        }
    }
}