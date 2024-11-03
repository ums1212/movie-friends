package org.comon.moviefriends.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.R

/** 이 영화를 보고 싶은 사람 목록 화면 */
@Preview
@Composable
fun UserWantThisMovieList(){

    // 더미 데이터
    val userList = listOf(
        "유저1", "유저2", "유저3", "유저4", "유저5",
        "유저6", "유저7", "유저8", "유저9", "유저10"
    )

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(24.dp)
    ) {
        MFText(stringResource(R.string.title_user_want_this_movie))
        LazyColumn(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize()
        ) {
            items(userList){ user ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UserWantListItem()
                    MFButtonWidthResizable({}, stringResource(R.string.button_watch_together), 120.dp)
                }
                Spacer(Modifier.padding(bottom = 8.dp))
            }
        }
    }
}