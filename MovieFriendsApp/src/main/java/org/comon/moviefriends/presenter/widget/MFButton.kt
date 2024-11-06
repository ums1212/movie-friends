package org.comon.moviefriends.presenter.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.R
import org.comon.moviefriends.data.api.tmdb.APIResult
import org.comon.moviefriends.presenter.theme.Black
import org.comon.moviefriends.presenter.theme.FriendsBoxGrey
import org.comon.moviefriends.presenter.theme.FriendsTextGrey
import org.comon.moviefriends.presenter.theme.FriendsWhite
import org.comon.moviefriends.presenter.theme.KakaoColor
import org.comon.moviefriends.presenter.theme.White

@Composable
fun MFButton(clickEvent: () -> Unit, text: String) {
    Button(
        shape = RoundedCornerShape(25),
        onClick = clickEvent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 36.dp),
        border = BorderStroke(1.dp, FriendsTextGrey),
        colors = ButtonColors(
            containerColor = FriendsBoxGrey,
            contentColor = White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        )
    ) {
        Text(text)
    }
}

@Composable
fun MFButtonWantThisMovie(clickEvent: () -> Unit, text: String, isChecked: APIResult<Boolean>) {
    Button(
        shape = RoundedCornerShape(25),
        onClick = clickEvent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 36.dp),
        border = BorderStroke(1.dp, FriendsTextGrey),
        colors = ButtonColors(
            containerColor = FriendsBoxGrey,
            contentColor = White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        )
    ) {
        when(isChecked){
            is APIResult.Loading -> { ShimmerEffect(modifier = Modifier.fillMaxSize()) }
            is APIResult.Success -> {
                if(isChecked.resultData){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(text)
                        Icon(imageVector = Icons.Default.Check, "체크")
                    }
                }else{
                    Text(text)
                }
            }
            else -> {
                Text(text)
            }
        }
    }
}

@Composable
fun MFButtonWatchTogether(clickEvent: () -> Unit, isChecked: MutableState<Boolean>) {
    Button(
        shape = RoundedCornerShape(25),
        onClick = clickEvent,
        modifier = Modifier
            .width(120.dp),
        border = BorderStroke(1.dp, FriendsTextGrey),
        colors = ButtonColors(
            containerColor = FriendsBoxGrey,
            contentColor = White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        ),
        contentPadding = PaddingValues(
            start = 2.dp,
            top = 2.dp,
            end = 2.dp,
            bottom = 2.dp,
        )
    ) {
        if(isChecked.value){
            Text(stringResource(R.string.button_cancel))
        }else{
            Text(stringResource(R.string.button_watch_together))
        }
    }
}

@Composable
fun MFButtonWidthResizable(clickEvent: () -> Unit, text: String, width: Dp) {
    Button(
        shape = RoundedCornerShape(25),
        onClick = clickEvent,
        modifier = Modifier
            .width(width),
        border = BorderStroke(1.dp, FriendsTextGrey),
        colors = ButtonColors(
            containerColor = FriendsBoxGrey,
            contentColor = White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        ),
        contentPadding = PaddingValues(
            start = 2.dp,
            top = 2.dp,
            end = 2.dp,
            bottom = 2.dp,
        )
    ) {
        Text(text)
    }
}

@Composable
fun MFButtonConfirm(clickEvent: () -> Unit, text: String, width: Dp) {
    OutlinedButton (
        shape = RoundedCornerShape(25),
        onClick = clickEvent,
        modifier = Modifier
            .width(width),
        border = BorderStroke(1.dp, FriendsTextGrey),
        colors = ButtonColors(
            containerColor = FriendsWhite,
            contentColor = Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        )
    ) {
        Text(text)
    }
}

@Composable
fun KakaoLoginButton(clickEvent: () -> Unit) {
    Button(
        shape = RoundedCornerShape(12.dp),
        onClick = clickEvent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 36.dp),
        colors = ButtonColors(
            containerColor = KakaoColor,
            contentColor = Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.kakao_icon),
                contentDescription = stringResource(R.string.button_kakao_login),
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(stringResource(R.string.button_kakao_login))
        }

    }
}

@Composable
fun GoogleLoginButton(clickEvent: () -> Unit) {
    Button(
        shape = RoundedCornerShape(12.dp),
        onClick = clickEvent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 36.dp),
        colors = ButtonColors(
            containerColor = White,
            contentColor = FriendsTextGrey,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        )
    ) {
        Row {
            Image(
                painter = painterResource(R.drawable.google_icon),
                contentDescription = stringResource(R.string.button_google_login),
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(stringResource(R.string.button_google_login))
        }

    }
}

