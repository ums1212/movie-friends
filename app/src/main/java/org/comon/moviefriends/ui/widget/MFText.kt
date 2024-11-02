package org.comon.moviefriends.ui.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import org.comon.moviefriends.ui.theme.FriendsBlack
import org.comon.moviefriends.ui.theme.FriendsTextGrey
import org.comon.moviefriends.ui.theme.FriendsWhite

@Composable
fun MFText(text:String, modifier: Modifier = Modifier){
    Text(
        modifier = modifier,
        text = text,
        color = FriendsWhite
    )
}

@Composable
fun MFPostTitle(text:String, modifier: Modifier = Modifier){
    Text(
        modifier = modifier,
        text = text,
        color = FriendsWhite,
        fontSize = 18.sp
    )
}

@Composable
fun MFPostContent(text:String, modifier: Modifier = Modifier){
    Text(
        modifier = modifier.fillMaxWidth(),
        maxLines = 2,
        text = text,
        overflow = TextOverflow.Ellipsis,
        color = FriendsTextGrey,
        fontSize = 16.sp
    )
}

@Composable
fun MFPostView(text:String, modifier: Modifier = Modifier){
    Text(
        modifier = modifier,
        text = text,
        color = FriendsWhite,
        fontSize = 14.sp
    )
}