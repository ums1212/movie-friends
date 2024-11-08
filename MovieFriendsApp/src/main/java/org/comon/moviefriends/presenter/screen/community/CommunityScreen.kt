package org.comon.moviefriends.presenter.screen.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
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
import org.comon.moviefriends.presenter.widget.CommunityList
import org.comon.moviefriends.presenter.widget.MFPostTitle
import org.comon.moviefriends.presenter.widget.MFText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    moveToCommunityDetailScreen: (Int) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Box(modifier = Modifier.background(FriendsBlack).fillMaxSize()){
        Scaffold(
            topBar = {
                TopAppBar(
                    expandedHeight = 112.dp,
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    title = {
                        Column(Modifier.fillMaxSize()) {
                            MFPostTitle(stringResource(R.string.label_menu_community))
                            Spacer(Modifier.padding(vertical = 12.dp))
                            // 공지사항
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    moveToCommunityDetailScreen(0)
                                }
                            ) {
                                MFText(
                                    stringResource(R.string.label_community_notification),
                                    Modifier.padding(12.dp)
                                )
                            }
                        }
                    },
                )
            }
        ) { paddingValues ->
            // 글 목록
            CommunityList(paddingValues, scrollBehavior) { postId ->
                moveToCommunityDetailScreen(postId)
            }
        }
    }
}