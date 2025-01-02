package org.comon.moviefriends.presenter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.comon.moviefriends.R
import org.comon.moviefriends.common.getDateString
import org.comon.moviefriends.data.entity.firebase.PostInfo
import org.comon.moviefriends.presenter.common.clickableOnce
import org.comon.moviefriends.presenter.theme.FriendsWhite

@Composable
fun PostContent(post: PostInfo){
    MFPostCategory(post.category)
    Spacer(Modifier.padding(vertical = 8.dp))
    MFPostTitle(post.title)
    MFPostDate(
        text = getDateString(post.createdDate.seconds),
        modifier = Modifier.padding(end = 8.dp)
    )
    Spacer(Modifier.padding(vertical = 8.dp))
    Row(
        modifier = Modifier
            .padding(end = 8.dp)
            .clickableOnce { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(36.dp)
                .padding(end = 4.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(post.user.profileImage)
                .crossfade(true)
                .error(R.drawable.logo)
                .build(),
            contentDescription = "회원 프로필 사진",
        )
        Column {
            MFText(post.user.nickName)
        }
    }
    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(end = 4.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(post.imageLink)
                .crossfade(true)
                .error(R.drawable.logo)
                .build(),
            contentDescription = "업로드 이미지",
        )
    }
    Text(
        modifier = Modifier.padding(12.dp),
        color = FriendsWhite,
        minLines = 10,
        text = post.content
    )
}