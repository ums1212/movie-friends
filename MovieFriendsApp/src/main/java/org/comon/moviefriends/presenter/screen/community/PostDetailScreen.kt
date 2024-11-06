package org.comon.moviefriends.presenter.screen.community

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.comon.moviefriends.R
import org.comon.moviefriends.presenter.common.clickableOnce
import org.comon.moviefriends.presenter.theme.FriendsBoxGrey
import org.comon.moviefriends.presenter.theme.FriendsTextGrey
import org.comon.moviefriends.presenter.theme.FriendsWhite
import org.comon.moviefriends.presenter.widget.DetailTopAppBar
import org.comon.moviefriends.presenter.widget.MFButtonWidthResizable
import org.comon.moviefriends.presenter.widget.MFPostReply
import org.comon.moviefriends.presenter.widget.MFPostTitle
import org.comon.moviefriends.presenter.widget.MFPostView
import org.comon.moviefriends.presenter.widget.MFText

@Composable
fun PostDetailScreen(
    communityId: Int,
    navigatePop: () -> Unit
) {
    val scrollState = rememberScrollState()

    val likeState = remember { mutableStateOf(false) }
    val likeCount = remember { mutableIntStateOf(0) }

    val dummyList = remember { mutableStateListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { DetailTopAppBar(navigatePop) },
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(12.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            MFPostTitle(
                "데드풀과 울버린 되게 재미있다..",
            )
            MFPostView(
                "2024-11-03 17:33",
                Modifier.padding(end = 8.dp)
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
                        .data("")
                        .crossfade(true)
                        .error(R.drawable.logo)
                        .build(),
                    contentDescription = "회원 프로필 사진",
                )
                Column {
                    MFText("유저1")
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
                        .data("")
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
                text = "데드풀과 울버린 보고 왔는데\n" +
                        "너무 유치한거 같음...\n" +
                        "\n" +
                        "님들은 재밌게 봤음?"
            )
            Spacer(Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickableOnce {
                        likeState.value = !likeState.value
                        if(likeState.value) likeCount.intValue++ else likeCount.intValue--
                    },
                ) {
                    MFText("좋아요 : ${likeCount.intValue}")
                    Icon(
                        modifier = Modifier.padding(start = 8.dp),
                        tint = FriendsWhite,
                        imageVector = if(likeState.value) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                        contentDescription = "좋아요"
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            if(true){
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
                                    Text(text = "댓글 작성", color = FriendsTextGrey, fontSize = 16.sp, modifier = Modifier.padding(4.dp).align(Alignment.CenterVertically))
                                }
                                Box(Modifier.padding(horizontal = 2.dp, vertical = 8.dp).align(Alignment.Bottom)){
                                    innerTextField()
                                }

                            }
                        }
                    )
                    MFButtonWidthResizable({
                        if(replyValue.isNotEmpty()){
                            replyValue = ""
                            dummyList.add(99)
                        }
                    }, "등록", 80.dp)
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
            Spacer(Modifier.padding(vertical = 8.dp))
            MFPostTitle("댓글 ${if(dummyList.size==0) "없음" else "(${dummyList.size})"}")
            Spacer(Modifier.padding(vertical = 8.dp))
            Column {
                dummyList.forEach { item ->
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
                                    .data("")
                                    .crossfade(true)
                                    .error(R.drawable.logo)
                                    .build(),
                                contentDescription = "회원 프로필 사진",
                            )
                            Column {
                                MFText("유저 $item")
                            }
                        }
                        // 삭제 버튼
                        IconButton(
                            onClick = { dummyList.remove(item) },
                        ) {
                            Icon(Icons.Filled.Clear, "삭제")
                        }
                    }
                    MFPostReply("댓글 놀이 ${item}등")
                    Spacer(Modifier.padding(vertical = 8.dp))
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }

    @Composable
    fun PostDetailScreenPreview() {
        val scrollState = rememberScrollState()

        val likeState = remember { mutableStateOf(false) }
        val likeCount = remember { mutableIntStateOf(0) }

        val dummyList = remember { mutableStateListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10) }

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
        ) {
            MFPostTitle(
                "데드풀과 울버린 되게 재미있다..",
            )
            MFPostView(
                "2024-11-03 17:33",
                Modifier.padding(end = 8.dp)
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
                        .data("")
                        .crossfade(true)
                        .error(R.drawable.logo)
                        .build(),
                    contentDescription = "회원 프로필 사진",
                )
                Column {
                    MFText("유저1")
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
                        .data("")
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
                text = "데드풀과 울버린 보고 왔는데\n" +
                        "너무 유치한거 같음...\n" +
                        "\n" +
                        "님들은 재밌게 봤음?"
            )
            Spacer(Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickableOnce {
                        likeState.value = !likeState.value
                        if(likeState.value) likeCount.intValue++ else likeCount.intValue--
                    },
                ) {
                    MFText("좋아요 : ${likeCount.intValue}")
                    Icon(
                        modifier = Modifier.padding(start = 8.dp),
                        tint = FriendsWhite,
                        imageVector = if(likeState.value) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                        contentDescription = "좋아요"
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            if(true){
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
                                    Text(text = "댓글 작성", color = FriendsTextGrey, fontSize = 16.sp, modifier = Modifier.padding(4.dp).align(Alignment.CenterVertically))
                                }
                                Box(Modifier.padding(horizontal = 2.dp, vertical = 8.dp).align(Alignment.Bottom)){
                                    innerTextField()
                                }

                            }
                        }
                    )
                    MFButtonWidthResizable({
                        if(replyValue.isNotEmpty()){
                            replyValue = ""
                            dummyList.add(99)
                        }
                    }, "등록", 80.dp)
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
            Spacer(Modifier.padding(vertical = 8.dp))
            MFPostTitle("댓글 ${if(dummyList.size==0) "없음" else "(${dummyList.size})"}")
            Spacer(Modifier.padding(vertical = 8.dp))
            Column {
                dummyList.forEach { item ->
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
                                    .data("")
                                    .crossfade(true)
                                    .error(R.drawable.logo)
                                    .build(),
                                contentDescription = "회원 프로필 사진",
                            )
                            Column {
                                MFText("유저 $item")
                            }
                        }
                        // 삭제 버튼
                        IconButton(
                            onClick = { dummyList.remove(item) },
                        ) {
                            Icon(Icons.Filled.Clear, "삭제")
                        }
                    }
                    MFPostReply("댓글 놀이 ${item}등")
                    Spacer(Modifier.padding(vertical = 8.dp))
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }


}