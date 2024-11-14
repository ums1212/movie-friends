package org.comon.moviefriends.presenter.screen.community

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.comon.moviefriends.R
import org.comon.moviefriends.common.COMMUNITY_MENU
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.common.WATCH_TOGETHER_MENU
import org.comon.moviefriends.common.showSnackBar
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.presenter.common.clickableOnce
import org.comon.moviefriends.presenter.theme.FriendsBlack
import org.comon.moviefriends.presenter.viewmodel.MovieDetailViewModel
import org.comon.moviefriends.presenter.widget.MFBadge
import org.comon.moviefriends.presenter.widget.MFPostTitle
import org.comon.moviefriends.presenter.widget.MFText
import org.comon.moviefriends.presenter.widget.ShimmerEffect
import org.comon.moviefriends.presenter.widget.UserWantThisMovieList

@Composable
fun WatchTogetherScreen(
    navigateToRequestList: () -> Unit,
    navigateToReceiveList: () -> Unit,
    navigateToChatRoomList: () -> Unit,
    navigateToMovieDetail: (id:Int) -> Unit,
    viewModel: MovieDetailViewModel = viewModel()
) {

    val user = MFPreferences.getUserInfo()

    val requestCountMap = viewModel.allChatRequestCount.collectAsStateWithLifecycle()
    val allUserWantList = viewModel.allUserWantList.collectAsStateWithLifecycle()
    val myRequestList = viewModel.myRequestList.collectAsStateWithLifecycle()

    val localContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackBarHost = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        viewModel.getUserInfo(user)
        viewModel.getAllWantMovieRequest()
        viewModel.getAllUserWantList()
        viewModel.getMyRequestList()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(FriendsBlack),
    ) {
        MFPostTitle(text = stringResource(R.string.label_menu_watch_together))
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        HorizontalDivider()
        WATCH_TOGETHER_MENU.entries.forEach { menu ->
            Column (
                Modifier
                    .clickableOnce {
                        when(menu){
                            WATCH_TOGETHER_MENU.REQUEST_LIST -> navigateToRequestList()
                            WATCH_TOGETHER_MENU.RECEIVE_LIST -> navigateToReceiveList()
                            WATCH_TOGETHER_MENU.CHAT_ROOM_LIST -> navigateToChatRoomList()
                        }
                    },
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MFText(menu.description)
                    when(val map = requestCountMap.value){
                        is APIResult.Success -> {
                            when(menu){
                                WATCH_TOGETHER_MENU.REQUEST_LIST -> {
                                    map.resultData["sendCount"]?.let { sendCount ->
                                        if(sendCount > 0) MFBadge(count = sendCount)
                                    }
                                }
                                WATCH_TOGETHER_MENU.RECEIVE_LIST -> {
                                    map.resultData["receiveCount"]?.let { receiveCount ->
                                        if(receiveCount > 0) MFBadge(count = receiveCount)
                                    }
                                }
                                else -> {}
                            }
                        }
                        else -> {}
                    }

                }
                HorizontalDivider()
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        MFText(text = stringResource(R.string.title_user_want_movies))
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        when(val list = allUserWantList.value){
            APIResult.Loading -> {
                AllUserWantListShimmer()
            }
            is APIResult.NetworkError -> {
                Box(modifier = Modifier.fillMaxSize()){
                    MFText(modifier = Modifier.padding(4.dp), text = stringResource(R.string.network_error))
                }
            }
            is APIResult.Success -> {
                UserWantThisMovieList(
                    screen = COMMUNITY_MENU.WATCH_TOGETHER.route,
                    wantList = list.resultData,
                    myRequestList = myRequestList.value,
                    navigateToMovieDetail = { movieId ->
                        navigateToMovieDetail(movieId)
                    },
                    requestWatchTogether = { movieId, moviePosterPath, receiveUser, receiveUserRegion ->
                        viewModel.requestWatchTogether(movieId, moviePosterPath, receiveUser, receiveUserRegion)
                    },
                    showErrorSnackBar = { showSnackBar(coroutineScope, snackBarHost, localContext) }
                )
            }
            else -> {}
        }
    }
    SnackbarHost(hostState = snackBarHost)
}

@Composable
fun AllUserWantListShimmer(){
    Column(modifier = Modifier.fillMaxSize()) {
        repeat(3){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShimmerEffect(
                    modifier = Modifier
                        .width(80.dp)
                        .height(120.dp)
                        .padding(4.dp)
                )
                ShimmerEffect(
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp)
                        .padding(4.dp)
                )
            }
        }
    }
}