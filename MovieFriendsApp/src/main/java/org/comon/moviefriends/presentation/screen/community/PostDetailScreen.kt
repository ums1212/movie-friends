package org.comon.moviefriends.presentation.screen.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.common.showSnackBar
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.firebase.UserInfo
import org.comon.moviefriends.presentation.theme.FriendsBlack
import org.comon.moviefriends.presentation.viewmodel.CommunityPostViewModel
import org.comon.moviefriends.presentation.components.MFPostTitle
import org.comon.moviefriends.presentation.components.OnDevelopMark
import org.comon.moviefriends.presentation.components.PostContent
import org.comon.moviefriends.presentation.components.PostLike
import org.comon.moviefriends.presentation.components.PostReplyInsertField
import org.comon.moviefriends.presentation.components.PostReplyList
import org.comon.moviefriends.presentation.components.ShimmerEffect

@Composable
fun PostDetailScreen(
    postId: String,
    viewModel: CommunityPostViewModel = hiltViewModel(),
    user: UserInfo? = MFPreferences.getUserInfo()
) {

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
                // 글 본문
                PostContent(postResult.resultData)
                Spacer(Modifier.padding(vertical = 8.dp))
                // 좋아요
                PostLike(viewModel, postResult.resultData, user)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                if(user!=null){
                    // 댓글 입력 필드
                    PostReplyInsertField(viewModel, coroutineScope, snackBarHost, localContext)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
                Spacer(Modifier.padding(vertical = 8.dp))
                // 댓글 리스트
                PostReplyList(viewModel, user)
            }
            is APIResult.NetworkError -> {
                OnDevelopMark()
                MFPostTitle(stringResource(R.string.network_error))
                showSnackBar(coroutineScope, snackBarHost, localContext)
            }
            else -> PostDetailShimmer()
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