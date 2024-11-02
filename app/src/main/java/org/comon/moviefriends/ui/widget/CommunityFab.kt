package org.comon.moviefriends.ui.widget

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import org.comon.moviefriends.ui.theme.FriendsBoxGrey
import org.comon.moviefriends.ui.theme.FriendsTextGrey
import org.comon.moviefriends.ui.theme.FriendsWhite

@Composable
fun CommunityFab(navigateToWritePost: () -> Unit) {
    FloatingActionButton(
        onClick = navigateToWritePost,
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