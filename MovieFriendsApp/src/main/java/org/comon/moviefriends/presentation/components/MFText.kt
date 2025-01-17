package org.comon.moviefriends.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.comon.moviefriends.R
import org.comon.moviefriends.presentation.theme.FriendsBlack
import org.comon.moviefriends.presentation.theme.FriendsBoxGrey
import org.comon.moviefriends.presentation.theme.FriendsTextGrey
import org.comon.moviefriends.presentation.theme.FriendsWhite

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
fun MFTitle(text:String, modifier: Modifier = Modifier){
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

@Composable
fun WorldCupTitleText(text:String, modifier: Modifier = Modifier){
    Text(
        modifier = modifier
            .background(
                color = FriendsTextGrey.copy(alpha = 0.5f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(4.dp),
        text = text,
        color = Color.White,
        fontSize = 24.sp
    )
}

@Composable
fun WorldCupVersusText(){
    Text(
        modifier = Modifier
            .background(
                color = FriendsBlack.copy(alpha = 0.7f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(4.dp),
        text = "VS",
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    )
}