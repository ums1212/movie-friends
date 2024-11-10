package org.comon.moviefriends.presenter.widget

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.presenter.theme.FriendsTextGrey
import org.comon.moviefriends.presenter.theme.FriendsWhite

@Composable
fun CommunityFab(
    navigateToWritePost: () -> Unit,
    navigateToLogin: () -> Unit
) {
    val user = MFPreferences.getUserInfo()

    FloatingActionButton(
        onClick = { clickCommunityFab(user, navigateToWritePost, navigateToLogin) },
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
    user: UserInfo?,
    navigateToWritePost: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    if(user==null){
        navigateToLogin()
        return
    }
    navigateToWritePost()
}