package org.comon.moviefriends.presentation.screen.community

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sendbird.uikit.compose.SendbirdUikitCompose
import com.sendbird.uikit.core.data.model.UikitCurrentUserInfo
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.entity.firebase.UserInfo
import org.comon.moviefriends.presentation.components.MFTitle
import org.comon.moviefriends.presentation.components.MFText
import org.comon.moviefriends.presentation.components.OnDevelopMark
import org.comon.moviefriends.presentation.viewmodel.ChatViewModel
import org.comon.moviefriends.presentation.widget.ChatRoomListItem

@Composable
fun ChatRoomListScreen(
    navigateToChannel: (String) -> Unit,
    user: UserInfo? = MFPreferences.getUserInfo(),
    viewModel: ChatViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    val chatList = viewModel.chatList.collectAsStateWithLifecycle()

    // Prepare user information.
    LaunchedEffect(Unit) {
        SendbirdUikitCompose.prepare(
            UikitCurrentUserInfo(
                userId = user?.sendBirdId ?: "",
                authToken = user?.sendBirdToken ?: ""
            )
        )
        user?.let {
            viewModel.getUserInfo(it)
            viewModel.loadChatList(it.id)
        }
    }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        MFTitle(stringResource(R.string.label_chat_room_lst))
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
                        ChatRoomListItem(
                            context = context,
                            item = item,
                            navigateToChannel = navigateToChannel
                        )
                    }
                }
            }
        }

    }
}