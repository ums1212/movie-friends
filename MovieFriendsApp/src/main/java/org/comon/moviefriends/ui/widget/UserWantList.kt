package org.comon.moviefriends.ui.widget

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
import org.comon.moviefriends.R
import org.comon.moviefriends.api.tmdb.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.model.UserInfo
import org.comon.moviefriends.model.UserWantMovieInfo
import org.comon.moviefriends.ui.common.clickableOnce

/** 이 영화를 보고 싶은 사람 목록 화면 */
@Composable
fun UserWantThisMovieList(
    screen: String,
    wantList: List<UserWantMovieInfo> = listOf(
        UserWantMovieInfo(movieId = 1034541, userInfo = UserInfo(nickName = "유저1"), userDistance = 100),
        UserWantMovieInfo(movieId = 912649, userInfo = UserInfo(nickName = "유저2"), userDistance = 200),
        UserWantMovieInfo(movieId = 1184918, userInfo = UserInfo(nickName = "유저3"), userDistance = 220),
        UserWantMovieInfo(movieId = 933260, userInfo = UserInfo(nickName = "유저4"), userDistance = 300),
        UserWantMovieInfo(movieId = 698687, userInfo = UserInfo(nickName = "유저5"), userDistance = 400),
        UserWantMovieInfo(movieId = 889737, userInfo = UserInfo(nickName = "유저6"), userDistance = 500),
        UserWantMovieInfo(movieId = 1084736, userInfo = UserInfo(nickName = "유저7"), userDistance = 600),
        UserWantMovieInfo(movieId = 976734, userInfo = UserInfo(nickName = "유저8"), userDistance = 700),
        UserWantMovieInfo(movieId = 1079091, userInfo = UserInfo(nickName = "유저9"), userDistance = 800),
        UserWantMovieInfo(movieId = 1196470, userInfo = UserInfo(nickName = "유저10"), userDistance = 900),
    ),
    navigateToMovieDetail: (id:Int) -> Unit = {},
    ){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(wantList){ want ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val checked = remember { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    if(screen=="watch_together"){
                        Card(
                            modifier = Modifier
                                .width(80.dp)
                                .height(120.dp)
                                .padding(4.dp)
                                .clickableOnce {
                                    navigateToMovieDetail(want.movieId)
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
                    UserWantListItem(want.userInfo, want.userDistance)
                }

                MFButtonWatchTogether({
                    checked.value = !checked.value
                }, checked)
            }
            Spacer(Modifier.padding(bottom = 8.dp))
        }
    }
}