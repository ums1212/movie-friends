package org.comon.moviefriends.ui.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.R
import org.comon.moviefriends.ui.widget.CommunityList
import org.comon.moviefriends.ui.widget.MFPostTitle

@Preview
@Composable
fun ProfileCommunityReplyScreen(
    moveToCommunityDetailScreen: (Int) -> Unit,
) {
//    OnDevelopMark()
    Column {
        MFPostTitle(stringResource(R.string.button_profile_community_reply))
        Spacer(Modifier.padding(vertical = 12.dp))
        CommunityList { postId ->
            moveToCommunityDetailScreen(postId)
        }
    }
}