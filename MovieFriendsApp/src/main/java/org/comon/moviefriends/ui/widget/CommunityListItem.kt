package org.comon.moviefriends.ui.widget

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.ui.theme.FriendsBoxGrey

@Composable
fun CommunityListItem(onNavigateToPostDetail: () -> Unit) {
    // post: PostInfo
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { onNavigateToPostDetail() },
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
            //        MFText(post.title)
            MFPostTitle("글 제목")
            Spacer(Modifier.padding(vertical = 12.dp))
//        MFText(post.content)
            MFPostListItemContent("글 내용")
            Spacer(Modifier.padding(vertical = 12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
//            MFText(post.createdDate)
//            MFText("조회수 : ${post.viewCount} 좋아요 : ${post.likes.size} 댓글 : ${post.reply.size}")
                MFPostView("2시간 전")
                MFPostView("조회수 : 1 좋아요 : 1 댓글 : 1")
            }
        }
    }
}