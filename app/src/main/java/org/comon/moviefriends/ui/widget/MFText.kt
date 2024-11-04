package org.comon.moviefriends.ui.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import org.comon.moviefriends.ui.theme.FriendsWhite

@Composable
fun MFText(text:String, modifier: Modifier = Modifier, textStyle: TextStyle = TextStyle.Default){
    Text(
        modifier = modifier,
        text = text,
        color = FriendsWhite,
        style = textStyle
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
fun MFPostListItemContent(text:String, modifier: Modifier = Modifier){
    Text(
        modifier = modifier.fillMaxWidth(),
        maxLines = 2,
        text = text,
        overflow = TextOverflow.Ellipsis,
        color = FriendsWhite,
        fontSize = 16.sp
    )
}

@Composable
fun MFPostReply(text:String, modifier: Modifier = Modifier){
    Text(
        modifier = modifier.fillMaxWidth(),
        text = text,
        color = FriendsWhite,
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