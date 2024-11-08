package org.comon.moviefriends.presenter.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.comon.moviefriends.R
import org.comon.moviefriends.data.model.UserInfo
import org.comon.moviefriends.presenter.common.clickableOnce

@Composable
fun UserWantListItem(user: UserInfo, distance: Int){
    Row(
        modifier = Modifier
            .wrapContentSize(unbounded = true)
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .clickableOnce {  },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier.size(48.dp).padding(end = 4.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(user.profileImage)
                .crossfade(true)
                .error(R.drawable.logo)
                .build(),
            contentDescription = "회원 프로필 사진",
            contentScale = ContentScale.Fit,
        )
        Column {
            MFText(user.nickName)
            MFText("${distance}m")
        }
    }
}