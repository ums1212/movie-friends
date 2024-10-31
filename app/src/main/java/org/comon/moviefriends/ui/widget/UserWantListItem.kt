package org.comon.moviefriends.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.comon.moviefriends.R

@Preview
@Composable
fun UserWantListItem(){
    Row(
        modifier = Modifier
            .padding(end = 8.dp)
            .clickable {  },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier.size(36.dp).padding(end = 4.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data("")
                .crossfade(true)
                .error(R.drawable.logo)
                .build(),
            contentDescription = "회원 프로필 사진",
        )
        Column {
            MFText("유저1")
            MFText("500m")
        }
    }
}