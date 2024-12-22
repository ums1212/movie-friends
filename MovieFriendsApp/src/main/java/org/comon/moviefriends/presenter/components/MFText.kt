package org.comon.moviefriends.presenter.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.comon.moviefriends.R
import org.comon.moviefriends.presenter.theme.FriendsBlack
import org.comon.moviefriends.presenter.theme.FriendsBoxGrey
import org.comon.moviefriends.presenter.theme.FriendsTextGrey
import org.comon.moviefriends.presenter.theme.FriendsWhite

@Composable
fun MFText(text:String, modifier: Modifier = Modifier, textStyle: TextStyle = TextStyle.Default, overflow: TextOverflow = TextOverflow.Clip){
    Text(
        modifier = modifier,
        text = text,
        color = FriendsWhite,
        style = textStyle,
        overflow = overflow,
    )
}
@Composable
fun ValidationText(text:String){
    Text(
        modifier = Modifier.padding(top = 4.dp, start = 14.dp),
        text = text,
        color = colorResource(R.color.validation_red),
        style = TextStyle(fontSize = 12.sp),
    )
}

@Composable
fun MFPostCategory(text:String, modifier: Modifier = Modifier){
    Card(
        colors = CardColors(
            containerColor = FriendsBoxGrey,
            contentColor = FriendsBoxGrey,
            disabledContainerColor = FriendsBlack,
            disabledContentColor = FriendsWhite
        )
    ) {
        Text(
            modifier = modifier.padding(vertical = 4.dp, horizontal = 18.dp),
            text = text,
            color = FriendsTextGrey,
            fontSize = 16.sp
        )
    }
}

@Composable
fun MFPostTitle(text:String, modifier: Modifier = Modifier){
    Text(
        modifier = modifier,
        text = text,
        color = FriendsWhite,
        fontSize = 24.sp
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
fun MFPostReplyDate(text:String, modifier: Modifier = Modifier){
    Text(
        modifier = modifier.fillMaxWidth(),
        text = text,
        color = FriendsTextGrey,
        fontSize = 12.sp
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

@Composable
fun MFPostDate(text:String, modifier: Modifier = Modifier){
    Text(
        modifier = modifier,
        text = text,
        color = FriendsTextGrey,
        fontSize = 12.sp
    )
}