package org.comon.moviefriends.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.comon.moviefriends.R
import org.comon.moviefriends.ui.theme.FriendsBoxGrey
import org.comon.moviefriends.ui.widget.MFPostTitle
import org.comon.moviefriends.ui.widget.MFText

@Composable
fun PostDetailScreen(communityId: Int) {
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
    ) {
        MFPostTitle(
            "제목",
            Modifier.padding(end = 8.dp)
        )
        Spacer(Modifier.padding(vertical = 8.dp))
        Row(
            modifier = Modifier
                .padding(end = 8.dp)
                .clickable {  },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.size(36.dp).padding(end = 4.dp),
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
        Spacer(Modifier.padding(vertical = 8.dp))
        MFPostTitle("내용")
        Spacer(Modifier.padding(vertical = 8.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            value = TextFieldValue(""),
            onValueChange = {},
            singleLine = false,
            textStyle = TextStyle(
                fontSize = 16.sp
            ),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = FriendsBoxGrey,
                unfocusedIndicatorColor = FriendsBoxGrey,
                focusedIndicatorColor = FriendsBoxGrey,
            )
        )
        Spacer(Modifier.padding(vertical = 8.dp))
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Preview
@Composable
fun PostDetailScreenPreview() {
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
    ) {
        MFPostTitle(
            "제목",
            Modifier.padding(end = 8.dp)
        )
        Spacer(Modifier.padding(vertical = 8.dp))
        Row(
            modifier = Modifier
                .padding(end = 8.dp)
                .clickable {  },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.size(36.dp).padding(end = 4.dp),
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
        Spacer(Modifier.padding(vertical = 8.dp))
        MFPostTitle("내용")
        Spacer(Modifier.padding(vertical = 8.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            value = TextFieldValue(""),
            onValueChange = {},
            singleLine = false,
            textStyle = TextStyle(
                fontSize = 16.sp
            ),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = FriendsBoxGrey,
                unfocusedIndicatorColor = FriendsBoxGrey,
                focusedIndicatorColor = FriendsBoxGrey,
            )
        )
        Spacer(Modifier.padding(vertical = 8.dp))
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    }


}