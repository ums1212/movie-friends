package org.comon.moviefriends.ui.widget

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.comon.moviefriends.ui.theme.FriendsWhite

@Composable
fun MFText(text:String){
    Text(
        text = text,
        color = FriendsWhite
    )
}