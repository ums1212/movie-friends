package org.comon.moviefriends.presenter.widget

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.comon.moviefriends.R
import org.comon.moviefriends.data.datasource.tmdb.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.data.model.firebase.RequestChatInfo
import org.comon.moviefriends.presenter.common.clickableOnce
import org.comon.moviefriends.presenter.components.MFText

@Composable
fun ChatRoomListItem(context: Context, item: RequestChatInfo?, navigateToChannel: (String) -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            .clickableOnce {
                item?.channelUrl?.let {
                    navigateToChannel(it)
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(96.dp),
                model = ImageRequest.Builder(context)
                    .data("$BASE_TMDB_IMAGE_URL/${item?.moviePosterPath}")
                    .crossfade(true)
                    .error(R.drawable.yoshicat)
                    .build(),
                contentDescription = "작품 이미지",
                contentScale = ContentScale.Fit
            )
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(36.dp).padding(end = 8.dp),
                        model = ImageRequest.Builder(context)
                            .data("${item?.sendUser?.profileImage}")
                            .crossfade(true)
                            .error(R.drawable.yoshicat)
                            .build(),
                        contentDescription = "유저 프로필",
                        contentScale = ContentScale.Fit
                    )
                    MFText(item?.sendUser?.nickName?:"")
                }
                Spacer(Modifier.padding(vertical = 8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(36.dp).padding(end = 8.dp),
                        model = ImageRequest.Builder(context)
                            .data("${item?.receiveUser?.profileImage}")
                            .crossfade(true)
                            .error(R.drawable.yoshicat)
                            .build(),
                        contentDescription = "유저 프로필",
                        contentScale = ContentScale.Fit
                    )
                    MFText(item?.receiveUser?.nickName?:"")
                }
            }
        }
        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, "채팅방으로 이동")
    }
    HorizontalDivider()
}