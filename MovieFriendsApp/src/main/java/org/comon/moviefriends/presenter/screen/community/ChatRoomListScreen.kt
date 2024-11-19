package org.comon.moviefriends.presenter.screen.community

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.sendbird.uikit.compose.SendbirdUikitCompose
import com.sendbird.uikit.core.data.model.UikitCurrentUserInfo
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.datasource.tmdb.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.presenter.common.clickableOnce
import org.comon.moviefriends.presenter.viewmodel.ChatViewModel
import org.comon.moviefriends.presenter.widget.MFPostTitle
import org.comon.moviefriends.presenter.widget.MFText
import org.comon.moviefriends.presenter.widget.OnDevelopMark

@Composable
fun ChatRoomListScreen(
    navigateToChannel: (String) -> Unit,
) {

    val user = MFPreferences.getUserInfo()
    val viewModel: ChatViewModel = hiltViewModel()
    val chatList = viewModel.chatList.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Prepare user information.
    LaunchedEffect(Unit) {
        SendbirdUikitCompose.prepare(
            UikitCurrentUserInfo(
                userId = user?.sendBirdId ?: "",
                authToken = user?.sendBirdToken ?: ""
            )
        )
        user?.id?.let {
            viewModel.loadChatList(it)
        }
    }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        MFPostTitle("대화 중인 채팅방")
        when(val list = chatList.value){
            APIResult.Loading -> LinearProgressIndicator()
            is APIResult.NetworkError -> OnDevelopMark()
            APIResult.NoConstructor -> LinearProgressIndicator()
            is APIResult.Success -> {
                if(list.resultData.isEmpty()) {
                    MFText(stringResource(R.string.no_data))
                    return@Column
                }
                Spacer(Modifier.padding(vertical = 8.dp))
                HorizontalDivider()
                LazyColumn(Modifier.fillMaxSize()) {
                    items(list.resultData){ item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                                .clickableOnce {
                                    item?.channelUrl?.let {
                                        navigateToChannel(it)
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(96.dp),
                                model = ImageRequest.Builder(context)
                                    .data("$BASE_TMDB_IMAGE_URL/${item?.moviePosterPath}")
                                    .crossfade(true)
                                    .error(R.drawable.yoshicat)
                                    .build(),
                                contentDescription = "작품 이미지",
                                contentScale = ContentScale.Fit
                            )
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .size(36.dp).padding(end = 8.dp),
                                        model = ImageRequest.Builder(context)
                                            .data("${item?.sendUser?.profileImage}")
                                            .crossfade(true)
                                            .error(R.drawable.yoshicat)
                                            .build(),
                                        contentDescription = "유저 프로필",
                                        contentScale = ContentScale.Fit
                                    )
                                    MFText(item?.sendUser?.nickName?:"")
                                }
                                Spacer(Modifier.padding(vertical = 8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .size(36.dp).padding(end = 8.dp),
                                        model = ImageRequest.Builder(context)
                                            .data("${item?.receiveUser?.profileImage}")
                                            .crossfade(true)
                                            .error(R.drawable.yoshicat)
                                            .build(),
                                        contentDescription = "유저 프로필",
                                        contentScale = ContentScale.Fit
                                    )
                                    MFText(item?.receiveUser?.nickName?:"")
                                }
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }

    }
}