package org.comon.moviefriends.presenter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.firebase.UserInfo
import org.comon.moviefriends.presenter.common.clickableOnce
import org.comon.moviefriends.presenter.theme.FriendsWhite
import org.comon.moviefriends.presenter.viewmodel.CommunityPostViewModel

@Composable
fun PostLike(
    viewModel: CommunityPostViewModel,
    initialLikeCount: Int,
    user: UserInfo?
){
    val like = viewModel.postLikeState.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MFText("좋아요 : ")
        when(val likeInfo = like.value){
            APIResult.Loading -> CircularProgressIndicator(Modifier.size(24.dp))
            is APIResult.NetworkError -> CircularProgressIndicator(Modifier.size(24.dp))
            APIResult.NoConstructor -> {
                MFText("$initialLikeCount")
                Icon(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    tint = FriendsWhite,
                    imageVector = Icons.Outlined.ThumbUp,
                    contentDescription = "좋아요"
                )
            }
            is APIResult.Success -> {
                MFText("${likeInfo.resultData.likeCount}")
                if(user!=null){
                    Icon(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickableOnce {
                                viewModel.changePostLikeState()
                            },
                        tint = FriendsWhite,
                        imageVector = if (likeInfo.resultData.isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                        contentDescription = "좋아요"
                    )
                }
            }
        }
    }
}