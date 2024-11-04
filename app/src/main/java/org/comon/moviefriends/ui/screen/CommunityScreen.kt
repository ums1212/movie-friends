package org.comon.moviefriends.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.R
import org.comon.moviefriends.ui.widget.CommunityList
import org.comon.moviefriends.ui.widget.MFText

@Composable
fun CommunityScreen(
    moveToCommunityDetailScreen: (Int) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        // 타이틀
        MFText(stringResource(R.string.label_menu_community))
        Spacer(Modifier.padding(vertical = 12.dp))
        // 공지사항
        Card(
            modifier = Modifier.fillMaxWidth()
                .height(50.dp),
            onClick = {
                moveToCommunityDetailScreen(0)
            }
        ) {
            MFText(
                stringResource(R.string.label_community_notification),
                Modifier.padding(12.dp)
            )
        }
        Spacer(Modifier.padding(vertical = 4.dp))
        // 글 목록
        CommunityList { postId ->
            moveToCommunityDetailScreen(postId)
        }
    }
}