package org.comon.moviefriends.ui.screen.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import org.comon.moviefriends.R
import org.comon.moviefriends.common.WATCH_TOGETHER_MENU
import org.comon.moviefriends.ui.theme.FriendsBlack
import org.comon.moviefriends.ui.widget.MFText
import org.comon.moviefriends.ui.widget.UserWantThisMovieList

@Composable
fun WatchTogetherScreen(
    navigateToRequestList: () -> Unit,
    navigateToReceiveList: () -> Unit,
    navigateToChatRoomList: () -> Unit,
    navigateToMovieDetail: (id:Int) -> Unit,
) {
    val context = LocalContext.current

    val watchTogetherList = remember { mutableStateListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10) }

    Column(
        modifier = Modifier.fillMaxWidth().background(FriendsBlack),
    ) {
        MFText(text = stringResource(R.string.label_menu_watch_together))
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        HorizontalDivider()
        WATCH_TOGETHER_MENU.entries.forEach { menu ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        when(menu){
                            WATCH_TOGETHER_MENU.REQUEST_LIST -> navigateToRequestList()
                            WATCH_TOGETHER_MENU.RECEIVE_LIST -> navigateToReceiveList()
                            WATCH_TOGETHER_MENU.CHAT_ROOM_LIST -> navigateToChatRoomList()
                        }
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MFText(menu.description)
            }
            HorizontalDivider()
        }

        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        MFText(text = stringResource(R.string.title_user_want_movies))
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        UserWantThisMovieList(screen = "watch_together", navigateToMovieDetail = navigateToMovieDetail)
    }
}