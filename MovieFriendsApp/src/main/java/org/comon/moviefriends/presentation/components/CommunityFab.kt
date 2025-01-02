package org.comon.moviefriends.presentation.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.presentation.theme.FriendsTextGrey
import org.comon.moviefriends.presentation.theme.FriendsWhite

@Composable
fun CommunityFab(
    navigateToWritePost: () -> Unit,
    navigateToLogin: () -> Unit
) {
    FloatingActionButton(
        onClick = { clickCommunityFab(navigateToWritePost, navigateToLogin) },
        containerColor = FriendsTextGrey,
        shape = CircleShape,
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "커뮤니티 글 작성",
            tint = FriendsWhite,
        )
    }
}

fun clickCommunityFab(
    navigateToWritePost: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    if(MFPreferences.getUserInfo()==null){
        navigateToLogin()
        return
    }
    navigateToWritePost()
}