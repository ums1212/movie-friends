package org.comon.moviefriends.presenter.screen.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import org.comon.moviefriends.R
import org.comon.moviefriends.common.COMMUNITY_MENU
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.common.WATCH_TOGETHER_MENU
import org.comon.moviefriends.presenter.common.clickableOnce
import org.comon.moviefriends.presenter.theme.FriendsBlack
import org.comon.moviefriends.presenter.viewmodel.MovieDetailViewModel
import org.comon.moviefriends.presenter.viewmodel.WatchTogetherViewModel
import org.comon.moviefriends.presenter.widget.MFPostTitle
import org.comon.moviefriends.presenter.widget.MFText
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

    Column(
        modifier = Modifier.fillMaxWidth().background(FriendsBlack),
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
                }
                HorizontalDivider()
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        MFText(text = stringResource(R.string.title_user_want_movies))
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        UserWantThisMovieList(
            screen = COMMUNITY_MENU.WATCH_TOGETHER.route,
            navigateToMovieDetail = { movieId ->
                navigateToMovieDetail(movieId)
            },
            requestWatchTogether = null
//            requestWatchTogether = { receiveUser ->
//                viewModel.requestWatchTogether(receiveUser)
//            }
        )
    }
}