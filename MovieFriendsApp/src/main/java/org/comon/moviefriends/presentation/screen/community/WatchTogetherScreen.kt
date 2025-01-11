package org.comon.moviefriends.presentation.screen.community

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.common.ScaffoldNavRoute
import org.comon.moviefriends.common.WATCH_TOGETHER_MENU
import org.comon.moviefriends.common.showSnackBar
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.presentation.common.clickableOnce
import org.comon.moviefriends.presentation.theme.FriendsBlack
import org.comon.moviefriends.presentation.components.MFBadge
import org.comon.moviefriends.presentation.components.MFTitle
import org.comon.moviefriends.presentation.components.MFText
import org.comon.moviefriends.presentation.components.ShimmerEffect
import org.comon.moviefriends.presentation.components.UserWantThisMovieList
import org.comon.moviefriends.presentation.viewmodel.WatchTogetherViewModel

@Composable
fun WatchTogetherScreen(
    navigateToRequestList: () -> Unit,
    navigateToReceiveList: () -> Unit,
    navigateToChatRoomList: () -> Unit,
    navigateToMovieDetail: (id:Int) -> Unit,
    viewModel: WatchTogetherViewModel = hiltViewModel()
) {

    val requestCountMap = viewModel.allChatRequestCount.collectAsStateWithLifecycle()
    val allUserWantList = viewModel.allUserWantList.collectAsStateWithLifecycle()
    val myRequestList = viewModel.myChatRequestList.collectAsStateWithLifecycle()

    val localContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackBarHost = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        viewModel.getUserInfo(MFPreferences.getUserInfo())
        viewModel.getAllChatRequestCount()
        viewModel.getAllUserWantList()
        viewModel.getMyRequestList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FriendsBlack),
    ) {
        MFTitle(text = stringResource(R.string.label_menu_watch_together), modifier = Modifier.padding(8.dp))
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        HorizontalDivider()
        WATCH_TOGETHER_MENU.entries.forEach { menu ->
            Column (
                Modifier
                    .clickableOnce {
                        when(menu){
                            WATCH_TOGETHER_MENU.REQUEST_LIST -> navigateToRequestList()
                            WATCH_TOGETHER_MENU.RECEIVE_LIST -> navigateToReceiveList()
                            WATCH_TOGETHER_MENU.CHAT_LIST -> navigateToChatRoomList()
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
        MFText(text = stringResource(R.string.title_user_want_movies), modifier = Modifier.padding(8.dp))
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
                if(list.resultData.isEmpty()) {
                    MFText(stringResource(R.string.no_data), modifier = Modifier.padding(8.dp))
                    return@Column
                }
                UserWantThisMovieList(
                    screen = ScaffoldNavRoute.WatchTogether.route,
                    wantList = list.resultData,
                    navigateToMovieDetail = { movieId ->
                        navigateToMovieDetail(movieId)
                    },
                    requestWatchTogether = { requestChatInfo ->
                        viewModel.requestWatchTogether(requestChatInfo)
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