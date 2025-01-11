package org.comon.moviefriends.presentation.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.R
import org.comon.moviefriends.presentation.components.CommunityList
import org.comon.moviefriends.presentation.components.MFTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCommunityPostScreen(
    moveToCommunityDetailScreen: (String) -> Unit,
) {
//    OnDevelopMark()
    Column(
        modifier = Modifier.padding(12.dp)
    ) {
        MFTitle(stringResource(R.string.button_profile_community_write))
        Spacer(Modifier.padding(vertical = 12.dp))
        CommunityList { postId ->
            moveToCommunityDetailScreen(postId)
        }
    }

}