package org.comon.moviefriends.presenter.components

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.comon.moviefriends.R
import org.comon.moviefriends.presenter.theme.FriendsBoxGrey

@Composable
fun ProfileCircleImage(context: Context, url: Uri?){
    AsyncImage(
        modifier = Modifier
            .size(128.dp)
            .clip(CircleShape)
            .border(1.dp, color = FriendsBoxGrey, shape = CircleShape),
        model = ImageRequest.Builder(context)
            .data(url)
            .crossfade(true)
            .error(R.drawable.logo)
            .build(),
        contentDescription = "회원 프로필 사진",
        contentScale = ContentScale.Crop,
    )
}