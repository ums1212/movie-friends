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
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import org.comon.moviefriends.R
import org.comon.moviefriends.common.ScaffoldNavRoute
import org.comon.moviefriends.data.datasource.tmdb.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.data.model.firebase.RequestChatInfo
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.data.model.firebase.UserWantMovieInfo
import org.comon.moviefriends.presenter.common.clickableOnce

/** 이 영화를 보고 싶은 사람 목록 화면 */
@Composable
fun UserWantThisMovieList(
    screen: String,
    wantList: List<UserWantMovieInfo?>,
    myRequestList: List<RequestChatInfo?>,
    navigateToMovieDetail: ((id:Int) -> Unit)?,
    requestWatchTogether: ((Int, String, UserInfo, String) -> Result<Task<QuerySnapshot>>)?,
    showErrorSnackBar: () -> Unit,
){

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        wantList.forEachIndexed { index, wantMovieInfo ->
            if(wantMovieInfo==null) return@forEachIndexed
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

                    if (requestWatchTogether != null) {
                        val requestState = remember {
                            mutableStateOf(myRequestList.find { it?.receiveUser?.id == wantMovieInfo.userInfo.id } != null)
                        }
                        MFButtonWatchTogether(
                            clickEvent = { requestWatchTogether(wantMovieInfo.movieId, wantMovieInfo.moviePosterPath, wantMovieInfo.userInfo, wantMovieInfo.userLocation) },
                            requestState = requestState,
                            showErrorSnackBar = showErrorSnackBar
                        )
                    }
                }
                Spacer(Modifier.padding(bottom = 8.dp))
            }
        }
    }
}