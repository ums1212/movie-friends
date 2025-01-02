package org.comon.moviefriends.presentation.components

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
import org.comon.moviefriends.data.entity.firebase.PostInfo
import org.comon.moviefriends.data.entity.firebase.UserInfo
import org.comon.moviefriends.presentation.common.clickableOnce
import org.comon.moviefriends.presentation.theme.FriendsWhite
import org.comon.moviefriends.presentation.viewmodel.CommunityPostViewModel

@Composable
fun PostLike(
    viewModel: CommunityPostViewModel,
    postInfo: PostInfo,
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
                MFText("${postInfo.likes.size}")
            }
            is APIResult.Success -> {
                MFText("${likeInfo.resultData.likeCount}")
                if(user!=null && user.id != postInfo.user.id){
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