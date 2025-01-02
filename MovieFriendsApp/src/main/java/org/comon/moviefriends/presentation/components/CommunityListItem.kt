package org.comon.moviefriends.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.common.getTimeDiff
import org.comon.moviefriends.data.entity.firebase.PostInfo
import org.comon.moviefriends.presentation.common.clickableOnce
import org.comon.moviefriends.presentation.theme.FriendsBoxGrey

@Composable
fun CommunityListItem(
    post: PostInfo,
    onNavigateToPostDetail: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickableOnce { onNavigateToPostDetail() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = FriendsBoxGrey
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 16.dp
        )
    ){
        Column(
            modifier = Modifier.padding(12.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            MFPostTitle(post.title)
            Spacer(Modifier.padding(vertical = 4.dp))
            MFPostListItemContent(post.content)
            Spacer(Modifier.padding(vertical = 4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MFPostDate(getTimeDiff(post.createdDate.seconds))
                MFPostView("조회수 : ${post.viewCount} 좋아요 : ${post.likes.size} 댓글 : ${post.reply.size}")
            }
        }
    }
}