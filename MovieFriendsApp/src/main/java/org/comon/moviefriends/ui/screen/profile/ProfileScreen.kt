package org.comon.moviefriends.ui.screen.profile

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.comon.moviefriends.R
import org.comon.moviefriends.model.UserInfo
import org.comon.moviefriends.ui.common.clickableOnce
import org.comon.moviefriends.ui.theme.FriendsBoxGrey
import org.comon.moviefriends.ui.theme.FriendsTextGrey
import org.comon.moviefriends.ui.theme.FriendsWhite
import org.comon.moviefriends.ui.widget.MFPostTitle

@Preview
@Composable
fun ProfileScreen(
    navigateToUserWant: () -> Unit,
    navigateToUserRate: () -> Unit,
    navigateToUserReview: () -> Unit,
    navigateToUserCommunityPost: () -> Unit,
    navigateToUserCommunityReply: () -> Unit,
) {
    val user = UserInfo(nickName = "벼랑위의당뇨")
    val context = LocalContext.current

    val scrollState = rememberScrollState()

    val wantMovieList = remember { mutableStateListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10) }
    val rateList = remember { mutableStateListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10) }
    val reviewList = remember { mutableStateListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(scrollState),
    ) {
        Spacer(modifier = Modifier.padding(vertical = 24.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .border(1.dp, color = FriendsBoxGrey, shape = CircleShape),
                model = ImageRequest.Builder(context)
                    .data(user.profileImage)
                    .crossfade(true)
                    .error(R.drawable.yoshicat)
                    .build(),
                contentDescription = "회원 프로필 사진",
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
            MFPostTitle(user.nickName)
        }
        Spacer(modifier = Modifier.padding(vertical = 24.dp))

        ProfileMenu(stringResource(R.string.button_profile_want_movie), navigateToUserWant, wantMovieList, context)
        ProfileMenu(stringResource(R.string.button_profile_rate), navigateToUserRate, rateList, context)
        ProfileMenu(stringResource(R.string.button_profile_review), navigateToUserReview, emptyList(), context)
        ProfileMenu(stringResource(R.string.button_profile_community_write), navigateToUserCommunityPost, context = null)
        ProfileMenu(stringResource(R.string.button_profile_community_reply), navigateToUserCommunityReply, context = null)
    }
}

@Composable
private fun ProfileMenu(
    title: String,
    navigateToProfileMenu: () -> Unit,
    menuList: List<Any> = emptyList(),
    context: Context?,
){
    HorizontalDivider()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickableOnce {
                navigateToProfileMenu()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MFPostTitle(title)
            Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "", tint = FriendsWhite)
        }
        if(context == null) return@Column
        if(menuList.isNotEmpty()){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                menuList.forEach { item ->
                    AsyncImage(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(72.dp)
                            .clip(CircleShape)
                            .border(1.dp, color = FriendsBoxGrey, shape = CircleShape),
                        model = ImageRequest.Builder(context)
                            .data(item)
                            .crossfade(true)
                            .error(R.drawable.yoshicat)
                            .build(),
                        contentDescription = "영화",
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }else{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ){
                Text(
                    text = "$title 가 없습니다",
                    color = FriendsTextGrey,
                )
            }
        }
    }
}
