package org.comon.moviefriends.presentation.components

import androidx.compose.material3.Badge
import androidx.compose.runtime.Composable
import org.comon.moviefriends.presentation.theme.FriendsRed
import org.comon.moviefriends.presentation.theme.FriendsWhite

@Composable
fun MFBadge(count: Int){
    Badge(
        containerColor = FriendsRed,
        contentColor = FriendsWhite,
        content = {
            MFText(text = count.toString())
        }
    )
}