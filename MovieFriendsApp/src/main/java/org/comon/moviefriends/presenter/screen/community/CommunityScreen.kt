package org.comon.moviefriends.presenter.screen.community

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.R
import org.comon.moviefriends.presenter.theme.FriendsBlack
import org.comon.moviefriends.presenter.components.CommunityFab
import org.comon.moviefriends.presenter.components.CommunityList
import org.comon.moviefriends.presenter.components.MFPostTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    moveToCommunityDetailScreen: (String) -> Unit,
    moveToWritePostScreen: () -> Unit,
    navigateToLogin: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        containerColor = FriendsBlack,
        floatingActionButton = {
            CommunityFab(
                navigateToWritePost = moveToWritePostScreen,
                navigateToLogin = navigateToLogin
            )
        },
        topBar = {
            TopAppBar(
                expandedHeight = 52.dp,
                scrollBehavior = scrollBehavior,
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                title = {
                    Column(Modifier.fillMaxSize()) {
                        MFPostTitle(stringResource(R.string.label_menu_community))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FriendsBlack)
            )
        }
    ) { paddingValues ->
        // 글 목록
        CommunityList(paddingValues, scrollBehavior) { postId ->
            moveToCommunityDetailScreen(postId)
        }
    }
}