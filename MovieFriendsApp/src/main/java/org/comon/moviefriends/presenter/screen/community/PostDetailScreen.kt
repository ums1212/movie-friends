package org.comon.moviefriends.presenter.screen.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.common.getDateString
import org.comon.moviefriends.common.getTimeDiff
import org.comon.moviefriends.common.showSnackBar
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.presenter.common.clickableOnce
import org.comon.moviefriends.presenter.theme.FriendsBlack
import org.comon.moviefriends.presenter.theme.FriendsBoxGrey
import org.comon.moviefriends.presenter.theme.FriendsTextGrey
import org.comon.moviefriends.presenter.theme.FriendsWhite
import org.comon.moviefriends.presenter.viewmodel.CommunityPostViewModel
import org.comon.moviefriends.presenter.widget.MFButtonAddReply
import org.comon.moviefriends.presenter.widget.MFPostCategory
import org.comon.moviefriends.presenter.widget.MFPostDate
import org.comon.moviefriends.presenter.widget.MFPostReply
import org.comon.moviefriends.presenter.widget.MFPostReplyDate
import org.comon.moviefriends.presenter.widget.MFPostTitle
import org.comon.moviefriends.presenter.widget.MFText
import org.comon.moviefriends.presenter.widget.ShimmerEffect

@Composable
fun PostDetailScreen(
    postId: String,
    viewModel: CommunityPostViewModel = hiltViewModel()
) {
    val user = MFPreferences.getUserInfo()

    LaunchedEffect(Unit) {
        viewModel.setPostId(postId)
        viewModel.setIsUpdate(false)
        viewModel.addViewCount()
        viewModel.getPost()
        viewModel.getPostLikeState()
        viewModel.getReplyList()
    }

    val scrollState = rememberScrollState()
    val snackBarHost = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val localContext = LocalContext.current
    val post = viewModel.postState.collectAsStateWithLifecycle()
    val like = viewModel.postLikeState.collectAsStateWithLifecycle()
    val reply = viewModel.getAllReplyState.collectAsStateWithLifecycle()

    Column(
        Modifier
            .padding(12.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(FriendsBlack),
    ) {
        when(val postResult = post.value){
            is APIResult.Success -> {
                if(postResult.resultData == null){
                    PostDetailShimmer()
                    showSnackBar(coroutineScope, snackBarHost, localContext)
                    return@Column
                }
                MFPostCategory(postResult.resultData.category)
                Spacer(Modifier.padding(vertical = 8.dp))
                MFPostTitle(postResult.resultData.title)
                MFPostDate(
                    text = getDateString(postResult.resultData.createdDate.seconds),
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
                            .data(postResult.resultData.user.profileImage)
                            .crossfade(true)
                            .error(R.drawable.logo)
                            .build(),
                        contentDescription = "회원 프로필 사진",
                    )
                    Column {
                        MFText(postResult.resultData.user.nickName)
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
                            .data(postResult.resultData.imageLink)
                            .crossfade(true)
                            .error(R.drawable.logo)
                            .build(),
                        contentDescription = "회원 프로필 사진",
                    )
                }
                Text(
                    modifier = Modifier.padding(12.dp),
                    color = FriendsWhite,
                    minLines = 10,
                    text = postResult.resultData.content
                )
                Spacer(Modifier.padding(vertical = 8.dp))
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
                            MFText("${postResult.resultData.likes.size}")
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
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                if(user!=null){
                    var replyValue by remember { mutableStateOf("") }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .weight(1f),
                            value = replyValue,
                            onValueChange = {
                                replyValue = it
                            },
                            singleLine = false,
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                color = FriendsWhite
                            ),
                            cursorBrush = SolidColor(FriendsTextGrey),
                            decorationBox = { innerTextField ->
                                Surface(
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .height(38.dp)
                                        .align(Alignment.Bottom),
                                    color = FriendsBoxGrey,
                                    shape = RoundedCornerShape(12.dp),
                                ) {
                                    if(replyValue.isEmpty()){
                                        Text(text = "댓글 작성", color = FriendsTextGrey, fontSize = 16.sp, modifier = Modifier
                                            .padding(4.dp)
                                            .align(Alignment.CenterVertically))
                                    }
                                    Box(
                                        Modifier
                                            .padding(horizontal = 2.dp, vertical = 8.dp)
                                            .align(Alignment.Bottom)){
                                        innerTextField()
                                    }

                                }
                            }
                        )
                        MFButtonAddReply(
                            insertState = viewModel.insertReplyState.collectAsStateWithLifecycle(),
                            showErrorSnackBar = { showSnackBar(coroutineScope, snackBarHost, localContext) },
                            clickEvent = {
                                viewModel.insertReply(replyValue)
                                replyValue = ""
                            }
                        )
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
            is APIResult.NetworkError -> {
                PostDetailShimmer()
                showSnackBar(coroutineScope, snackBarHost, localContext)
            }
            else -> PostDetailShimmer()
        }

        Spacer(Modifier.padding(vertical = 8.dp))
        when(val postResult = reply.value){
            APIResult.Loading -> ShimmerEffect(Modifier.fillMaxWidth().height(80.dp))
            is APIResult.Success -> {
                MFPostTitle("댓글 ${if(postResult.resultData.isEmpty()) "없음" else "(${postResult.resultData.size})"}")
                Spacer(Modifier.padding(vertical = 8.dp))
                if(postResult.resultData.isEmpty()) return@Column
                Column(Modifier.fillMaxWidth()) {
                    postResult.resultData.forEach{ reply ->
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
                                        .data(reply?.user?.profileImage)
                                        .crossfade(true)
                                        .error(R.drawable.logo)
                                        .build(),
                                    contentDescription = "회원 프로필 사진",
                                )
                                Column {
                                    MFText(reply?.user?.nickName ?: "정보 없음")
                                }
                            }
                            if(user!=null && user.id == reply?.user?.id){
                                // 삭제 버튼
                                IconButton(
                                    onClick = { viewModel.deleteReply(reply.id) },
                                ) {
                                    Icon(Icons.Filled.Clear, "삭제")
                                }
                            }
                        }
                        Spacer(Modifier.padding(vertical = 8.dp))
                        MFPostReply(reply?.content ?: "정보 없음")
                        Spacer(Modifier.padding(vertical = 8.dp))
                        MFPostReplyDate(getTimeDiff(reply?.createdDate?.seconds ?: 0))
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
            else -> {}
        }
    }
    SnackbarHost(snackBarHost)
}

@Composable
fun PostDetailShimmer(){
    ShimmerEffect(
        Modifier
            .fillMaxWidth()
            .height(100.dp))
    Spacer(Modifier.padding(vertical = 8.dp))
    ShimmerEffect(
        Modifier
            .fillMaxWidth()
            .height(100.dp))
    HorizontalDivider()
    Spacer(Modifier.padding(vertical = 4.dp))
    ShimmerEffect(
        Modifier
            .fillMaxWidth()
            .height(40.dp))
    Spacer(Modifier.padding(vertical = 2.dp))
    ShimmerEffect(
        Modifier
            .fillMaxWidth()
            .height(40.dp))
    Spacer(Modifier.padding(vertical = 2.dp))
    ShimmerEffect(
        Modifier
            .fillMaxWidth()
            .height(40.dp))
}