package org.comon.moviefriends.presenter.components

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import org.comon.moviefriends.common.showSnackBar
import org.comon.moviefriends.presenter.theme.FriendsBoxGrey
import org.comon.moviefriends.presenter.theme.FriendsTextGrey
import org.comon.moviefriends.presenter.theme.FriendsWhite
import org.comon.moviefriends.presenter.viewmodel.CommunityPostViewModel

@Composable
fun PostReplyInsertField(
    viewModel: CommunityPostViewModel,
    coroutineScope: CoroutineScope,
    snackBarHost: SnackbarHostState,
    localContext: Context
){
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
}