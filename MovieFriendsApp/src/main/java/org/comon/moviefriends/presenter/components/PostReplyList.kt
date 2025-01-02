package org.comon.moviefriends.presenter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.comon.moviefriends.R
import org.comon.moviefriends.common.getTimeDiff
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.firebase.UserInfo
import org.comon.moviefriends.presenter.viewmodel.CommunityPostViewModel

@Composable
fun PostReplyList(
    viewModel: CommunityPostViewModel,
    user: UserInfo?
){
    val reply = viewModel.getAllReplyState.collectAsStateWithLifecycle()

    when(val replyResult = reply.value){
        APIResult.Loading -> ShimmerEffect(Modifier.fillMaxWidth().height(80.dp))
        is APIResult.Success -> {
            MFPostTitle("댓글 ${if(replyResult.resultData.isEmpty()) "없음" else "(${replyResult.resultData.size})"}")
            Spacer(Modifier.padding(vertical = 8.dp))
            if(replyResult.resultData.isEmpty()) return
            Column(Modifier.fillMaxWidth()) {
                replyResult.resultData.forEach{ replyInfo ->
                    if(replyInfo==null)return@forEach
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // 유저 프로필
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(end = 4.dp),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(replyInfo.user.profileImage)
                                    .crossfade(true)
                                    .error(R.drawable.logo)
                                    .build(),
                                contentDescription = "회원 프로필 사진",
                            )
                            Column {
                                MFText(replyInfo.user.nickName)
                            }
                        }
                        if(user!=null && user.id == replyInfo.user.id){
                            // 삭제 버튼
                            IconButton(
                                onClick = { viewModel.deleteReply(replyInfo.id) },
                            ) {
                                Icon(Icons.Filled.Clear, "삭제")
                            }
                        }
                    }
                    Spacer(Modifier.padding(vertical = 8.dp))
                    MFPostReply(replyInfo.content)
                    Spacer(Modifier.padding(vertical = 8.dp))
                    MFPostReplyDate(getTimeDiff(replyInfo.createdDate.seconds))
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
        is APIResult.NetworkError -> {
            MFPostTitle(stringResource(R.string.network_error))
        }
        else -> ShimmerEffect(Modifier.fillMaxWidth().height(80.dp))
    }
}